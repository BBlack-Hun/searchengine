package com.mayfarm.Search.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.MultiSearchResponse.Item;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mayfarm.Search.dao.EsDAO;
import com.mayfarm.Search.vo.EsVO;
import com.mayfarm.Search.vo.ParamVO;
import com.mayfarm.core.utils.ElasticSearchUtil;
import com.mayfarm.core.utils.SearchUtil;

@Service
public class EsService {
	
	@Inject
	private EsDAO dao;
	
	private String preTags = "<strong style ='font-size:1.0em; color: red;'>";
	private String postTags = "</strong>";
	private final String splitFormat =",";
	private final int autoRecommMindocCount = 5;
	
	
	public Map<String, Object> TSearch(ParamVO paramVO) throws IOException {

		// 카테고리별 검색 결과 수 저장을 위한 Map 
		Map<String, Object> semiTotal = new HashMap<String, Object>();
		
		// 반환을 위한 Map
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		MultiSearchResponse multiSearchResponse = dao.TSearch(paramVO);
		Item items[] = multiSearchResponse.getResponses();
		
		// MOIS
		SearchResponse searchResponse_mois = items[0].getResponse();
		// LAW
		SearchResponse searchResponse_law = items[1].getResponse();
		// News
		SearchResponse searchResponse_news = items[2].getResponse();
		
		// MOIS 결과 정제
		List<Map<String, Object>> list_mois = new ArrayList<Map<String, Object>>();
		for (SearchHit hit : searchResponse_mois.getHits().getHits()) {
			Map<String, Object> sourceMap = hit.getSourceAsMap();
			
			// 하이라이트
			ElasticSearchUtil.sourceToHighlight(sourceMap, SearchUtil.getSearchFieldMOIS_highlight(paramVO.getField()), getHighlightKeywordArray(paramVO), preTags, postTags);
			list_mois.add(sourceMap);
		}
		
		// LAW 결과 정제
		List<Map<String, Object>> list_law = new ArrayList<Map<String, Object>>();
		for (SearchHit hit : searchResponse_law.getHits().getHits()) {
			Map<String, Object> sourceMap = hit.getSourceAsMap();
			
			// 하이라이트
			ElasticSearchUtil.sourceToHighlight(sourceMap, SearchUtil.getSearchFieldLAW_highlight(paramVO.getField()), getHighlightKeywordArray(paramVO), preTags, postTags);
			list_law.add(sourceMap);
		}
		
		// News 결과 정제
		List<Map<String, Object>> list_news = new ArrayList<Map<String, Object>>();
		try {
			for (SearchHit hit : searchResponse_news.getHits().getHits()) {
				Map<String, Object> sourceMap = hit.getSourceAsMap();
				
				//하이라이트
				ElasticSearchUtil.sourceToHighlight(sourceMap, SearchUtil.getSearchFieldNEWS_highlight(paramVO.getField()), getHighlightKeywordArray(paramVO), preTags, postTags);
				list_news.add(sourceMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		// 전체 검색 결과 수
		long total = 0;
		int cnt = 0;
		for (Item item : items) {
			long stotal = 0;
			SearchResponse response = item.getResponse();
			total += response.getHits().getTotalHits().value;
			// 각 카테고리별 검색 결과수 
			stotal = response.getHits().getTotalHits().value;
			// Map에 put
			semiTotal.put("item"+cnt,stotal);
			cnt +=1;
		}
		
		// 로그 생성
		if (!StringUtils.isBlank(paramVO.getSearch())) {
			dao.createSearchLog(paramVO, total);
		}
		
		// 연관 검색어
		List<String> autoRecommList = new ArrayList<>();
		if (!StringUtils.isBlank(paramVO.getSearch())) {
			autoRecommList = dao.getAutoRecommList(paramVO.getSearch(), autoRecommMindocCount, 7);
		}
		
		returnMap.put("stotal", semiTotal);
		returnMap.put("MOIS", list_mois);
		returnMap.put("LAW", list_law);
		returnMap.put("NEWS", list_news);
		returnMap.put("total", total);
		returnMap.put("autoRecommList", autoRecommList);
		return returnMap;
	}
	
	public Map<String, Object> MSearch(ParamVO paramVO) throws IOException {
		
		// 반환을 위한 Map
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		// 데이터를 가져온다.
		SearchResponse searchResponse = dao.Msearch(paramVO);
		
		// MOIS 결과 정제
		List<Map<String, Object>> list_mois = new ArrayList<Map<String, Object>>();
		for (SearchHit hit : searchResponse.getHits().getHits()) {
			Map<String, Object> sourceMap = hit.getSourceAsMap();
			
			//하이라이트
			ElasticSearchUtil.sourceToHighlight(sourceMap, SearchUtil.getSearchFieldMOIS_highlight(paramVO.getField()), getHighlightKeywordArray(paramVO), preTags, postTags);
			list_mois.add(sourceMap);
			
		}
		// 전체 검색 결과 수
		long total = searchResponse.getHits().getTotalHits().value;
		
		// 로그 생성
		dao.createSearchLog(searchResponse, paramVO);
		
		// 연관 검색어
		List<String> autoRecommList = new ArrayList<>();
		if (!StringUtils.isBlank(paramVO.getSearch())) {
			autoRecommList = dao.getAutoRecommList(paramVO.getSearch(), autoRecommMindocCount, 7);
		}
		
		returnMap.put("total", total);
		returnMap.put("MOIS", list_mois);
		returnMap.put("autoRecommList", autoRecommList);
		return returnMap;
	}
	
	public Map<String, Object> LSearch(ParamVO paramVO) throws IOException {
		
		// 반환을 위한 Map
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		// 데이터를 가져온다.
		SearchResponse searchResponse = dao.Lsearch(paramVO);
		
		// LAW 결과 정제
		List<Map<String, Object>> list_law = new ArrayList<Map<String, Object>>();
		for (SearchHit hit : searchResponse.getHits().getHits()) {
			Map<String, Object> sourceMap = hit.getSourceAsMap();
			
			// 하이라이트
			ElasticSearchUtil.sourceToHighlight(sourceMap, SearchUtil.getSearchFieldLAW_highlight(paramVO.getField()), getHighlightKeywordArray(paramVO), preTags, postTags);
			list_law.add(sourceMap);
		}
		
		// 전체 검색 결과 수
		long total = searchResponse.getHits().getTotalHits().value;
		
		// 로그 생성
		dao.createSearchLog(searchResponse, paramVO);

		// 연관 검색어
		List<String> autoRecommList = new ArrayList<>();
		if (!StringUtils.isBlank(paramVO.getSearch())) {
			autoRecommList = dao.getAutoRecommList(paramVO.getSearch(), autoRecommMindocCount, 7);
		}
		
		returnMap.put("total", total);
		returnMap.put("LAW", list_law);
		returnMap.put("autoRecommList", autoRecommList);
		return returnMap;
	}
	
	public Map<String, Object> NSearch(ParamVO paramVO) throws IOException {
		
		// 반환을 위한 Map
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		// 데이터를 가져온다.
		SearchResponse searchResponse = dao.Nsearch(paramVO);
		
		// NEWS 결과 정제
		List<Map<String, Object>> list_news = new ArrayList<Map<String, Object>>();
		for (SearchHit hit : searchResponse.getHits().getHits()) {
			Map<String, Object> sourceMap = hit.getSourceAsMap();
			
			// 하이라이트
			ElasticSearchUtil.sourceToHighlight(sourceMap, SearchUtil.getSearchFieldNEWS_highlight(paramVO.getField()), getHighlightKeywordArray(paramVO), preTags, postTags);
			list_news.add(sourceMap);
		}
		
		// 전체 검색 결과 수
		long total = searchResponse.getHits().getTotalHits().value;
		
		// 로그 생성
		dao.createSearchLog(searchResponse, paramVO);
		
		// 연관 검색어
		List<String> autoRecommList = new ArrayList<>();
		if (!StringUtils.isBlank(paramVO.getSearch())) {
			autoRecommList = dao.getAutoRecommList(paramVO.getSearch(), autoRecommMindocCount, 7);
		}
		
		returnMap.put("total", total);
		returnMap.put("NEWS", list_news);
		returnMap.put("autoRecommList", autoRecommList);
		return returnMap;
	}
	
	/**
	 * 인기검색어 호출 서비스
	 * 인기검색어 DAO 호출 인기검색어 목록 반환
	 * @return 인기검색어 목록
	 * @throws IOException
	 */
	public List<Map<String, Integer>> getSearchTopWordList() throws IOException {
		return dao.getSearchTopWordList();
	}
	
	/**
	 * 통합검색 검색어 자동완성 서비스
	 * @param search 검색어
	 * @return 자동완성 단어 목록
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public List<String> getAutocompleteSearch(String search) throws JsonProcessingException, IOException {
		return dao.getAutoComplateSearch(search);
	}
	
	/**
	 * 하이라이트를 할 단어 배열 반환
	 * @param search
	 * @return 하이라이트를 할 단어 배열
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	private String[] getHighlightKeywordArray(ParamVO paramVO) {
		String search = paramVO.getSearch();
		String exactSearch = paramVO.getExactSearch();
		String includeSearch = paramVO.getIncludeSearch();
		String[] reSearch = paramVO.getReSearch().split(splitFormat);
		String[] reexactSearch = paramVO.getReexactSearch().split(splitFormat);
		String[] reincludeSearch = paramVO.getReincludeSearch().split(splitFormat); 
		
		Set<String> highlightKeywords = new HashSet<>();
		highlightKeywords.add(search);
		highlightKeywords.add(exactSearch);
		highlightKeywords.add(includeSearch);
		highlightKeywords.addAll(Arrays.asList(reSearch));
		highlightKeywords.addAll(Arrays.asList(reexactSearch));
		highlightKeywords.addAll(Arrays.asList(reincludeSearch));
		
		for (String word : new HashSet<>(highlightKeywords)) {
			if (StringUtils.isBlank(word)) {
				highlightKeywords.remove(word);
			}
		}
		
		return highlightKeywords.toArray(new String[highlightKeywords.size()]);
	}
}
