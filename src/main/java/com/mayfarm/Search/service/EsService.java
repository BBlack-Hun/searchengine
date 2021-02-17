package com.mayfarm.Search.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.MultiSearchResponse.Item;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.stereotype.Service;

import com.mayfarm.Search.dao.EsDAO;
import com.mayfarm.Search.vo.EsVO;
import com.mayfarm.Search.vo.ParamVO;

@Service
public class EsService {
	
	@Inject
	private EsDAO dao;
	
	
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
			list_mois.add(hit.getSourceAsMap());
		}
		
		// LAW 결과 정제
		List<Map<String, Object>> list_law = new ArrayList<Map<String, Object>>();
		for (SearchHit hit : searchResponse_law.getHits().getHits()) {
			list_law.add(hit.getSourceAsMap());
		}
		
		// News 결과 정제
		List<Map<String, Object>> list_news = new ArrayList<Map<String, Object>>();
		for (SearchHit hit : searchResponse_news.getHits().getHits()) {
			list_news.add(hit.getSourceAsMap());
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
		
		returnMap.put("stotal", semiTotal);
		returnMap.put("MOIS", list_mois);
		returnMap.put("LAW", list_law);
		returnMap.put("NEWS", list_news);
		returnMap.put("total", total);
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
			list_mois.add(hit.getSourceAsMap());
		}
		// 전체 검색 결과 수
		long total = searchResponse.getHits().getTotalHits().value;
		
		// 로그 생성
		dao.createSearchLog(searchResponse, paramVO);
		
		returnMap.put("total", total);
		returnMap.put("MOIS", list_mois);
		return returnMap;
	}
	
	public Map<String, Object> LSearch(ParamVO paramVO) throws IOException {
		
		// 반환을 위한 Map
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		// 데이터를 가져온다.
		SearchResponse searchResponse = dao.Lsearch(paramVO);
		
		// MOIS 결과 정제
		List<Map<String, Object>> list_law = new ArrayList<Map<String, Object>>();
		for (SearchHit hit : searchResponse.getHits().getHits()) {
			list_law.add(hit.getSourceAsMap());
		}
		
		// 전체 검색 결과 수
		long total = searchResponse.getHits().getTotalHits().value;
		
		// 로그 생성
		dao.createSearchLog(searchResponse, paramVO);
		
		returnMap.put("total", total);
		returnMap.put("LAW", list_law);
		return returnMap;
	}
	
	public Map<String, Object> NSearch(ParamVO paramVO) throws IOException {
		
		// 반환을 위한 Map
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		// 데이터를 가져온다.
		SearchResponse searchResponse = dao.Nsearch(paramVO);
		
		// MOIS 결과 정제
		List<Map<String, Object>> list_news = new ArrayList<Map<String, Object>>();
		for (SearchHit hit : searchResponse.getHits().getHits()) {
			list_news.add(hit.getSourceAsMap());
		}
		
		// 전체 검색 결과 수
		long total = searchResponse.getHits().getTotalHits().value;
		
		// 로그 생성
		dao.createSearchLog(searchResponse, paramVO);
		
		returnMap.put("total", total);
		returnMap.put("NEWS", list_news);
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
	
}
