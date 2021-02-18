package com.mayfarm.Search.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Repository;

import com.mayfarm.Search.vo.ParamVO;
import com.mayfarm.core.utils.ElasticSearchUtil;
import com.mayfarm.core.utils.SearchUtil;

@Repository
public class EsDAO {
	
	@Inject 
	RestHighLevelClient restClient;
	@Inject
	HttpServletRequest request;
	@Inject
	Properties properties;
	
	private final String domain = "DICT_0";
	
	private void setSearch(ParamVO paramVO) throws IOException {
		String search = paramVO.getSearch();
		String osearch = paramVO.getOsearch();
		// 변수 초기화 (pagination)
		int listSize = paramVO.getListSize();
		
		if(!paramVO.getCategory().equals("통합검색")) {
			listSize = (listSize < 10) ? 10 : listSize;
			paramVO.setListSize(listSize);
		} else {
			paramVO.setListSize(4);
		}
	}
	
	public MultiSearchResponse TSearch(ParamVO paramVO) throws IOException {
		setSearch(paramVO);
		// 통합 검색을 위한 MultiSearch 초기화
		MultiSearchRequest multiSerachRequest = new MultiSearchRequest();
		
		// MOIS 
		SearchRequest searchRequest_mois = getMoisRequest(paramVO);
		multiSerachRequest.add(searchRequest_mois);
		// LAW	
		SearchRequest searchRequest_law = getLawRequest(paramVO);
		multiSerachRequest.add(searchRequest_law);
		// News
		SearchRequest searchRequest_news = getNewsRequest(paramVO);
		multiSerachRequest.add(searchRequest_news);
		
		return restClient.msearch(multiSerachRequest, RequestOptions.DEFAULT);
	}
	/**
	 * MOIS 기사 리퀘스트 호출 및 검색결과 반환
	 * @param str
	 * @param cri
	 * @param Category
	 * @return MOIS 기사 검색 결과
	 * @throws IOException
	 */
	public SearchResponse Msearch(ParamVO paramVO) throws IOException {
		setSearch(paramVO);
		SearchRequest searchRequest = getMoisRequest(paramVO);
		return restClient.search(searchRequest, RequestOptions.DEFAULT);
	}
	/**
	 * Law 법률 리퀘스트 호출 및 검색결과 반환
	 * @param str
	 * @param cri
	 * @param Category
	 * @return Law 법률 검색 결과 반환
	 * @throws IOException
	 */
	public SearchResponse Lsearch(ParamVO paramVO) throws IOException {
		setSearch(paramVO);
		SearchRequest searchRequest = getLawRequest(paramVO);
		return restClient.search(searchRequest, RequestOptions.DEFAULT);
	}
	
	/**
	 * News 기사 리퀘스트 호출 및 검색결과 반환
	 * @param str
	 * @param cri
	 * @param Category
	 * @return News 기사 검색 결과 반환
	 * @throws IOException
	 */
	public SearchResponse Nsearch(ParamVO paramVO) throws IOException {
		setSearch(paramVO);
		SearchRequest searchRequest = getNewsRequest(paramVO);
		return restClient.search(searchRequest, RequestOptions.DEFAULT);
	}
	/**
	 * 
	 * @param cri
	 * @param str
	 * @param Category
	 * @return MOIS 결과 반환
	 */
	private SearchRequest getMoisRequest(ParamVO paramVO) {
		
		// 값 세팅
		String sort = paramVO.getSort();
		String field = paramVO.getField();
		
		int page = paramVO.getPage();
		int listSize = paramVO.getListSize();
		int from = (page ==1) ? 0 : listSize * (page - 1);
		
		// 엘라스틱 초기화
		SearchRequest searchRequest = new SearchRequest("mois");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		
		// 검색 필드
		String [] fields = SearchUtil.getSearchFieldLAW(field);
		
		// 정렬 ( 이름순으로 정렬, Default는 정확도순)
		if (sort.equals("최신순")) {
			searchSourceBuilder.sort(new FieldSortBuilder("regdt").order(SortOrder.DESC));
		}
		
		// must 내부의 and 조건절 추가를 위한 추가 bool 쿼리 선언
		BoolQueryBuilder semiboolQueryBuilder = QueryBuilders.boolQuery();
		
		// JTBC가 무조건 들어간 카테고리만 검색
//		TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("violt_ctgr_cd_nm", "mois");
//		boolQueryBuilder.must(termQueryBuilder);
		
		// title
		MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("title", paramVO.getSearch());
		boolQueryBuilder.must(semiboolQueryBuilder.should(matchQueryBuilder));
		
		// stitle
		MatchQueryBuilder matchQueryBuilder2 = QueryBuilders.matchQuery("stitle", paramVO.getSearch());
		boolQueryBuilder.must(semiboolQueryBuilder.should(matchQueryBuilder2));
		
		// Content
		MatchQueryBuilder matchQueryBuilder3 = QueryBuilders.matchQuery("content", paramVO.getSearch());
		boolQueryBuilder.must(semiboolQueryBuilder.should(matchQueryBuilder3));
		
		// 정렬 ( 이름순으로 정렬, Default는 정확도순)
		//searchSourceBuilder.sort(new FieldSortBuilder("no").order(SortOrder.ASC));
		searchSourceBuilder.sort(new FieldSortBuilder("_score").order(SortOrder.DESC));
		
		searchSourceBuilder.query(boolQueryBuilder);
		searchSourceBuilder.from(from);
		searchSourceBuilder.size(listSize);
		// 하이라이트 적용
//		searchSourceBuilder.highlighter(makeHighLightField());
		// 테스트 출력
		System.out.println(searchSourceBuilder.toString());
		searchRequest.source(searchSourceBuilder);
		return searchRequest;
		
	}
	/**
	 * 
	 * @param paramVO
	 * @return LAW 결과 반환
	 */
	private SearchRequest getLawRequest(ParamVO paramVO) {
			
		// 값 세팅
		String sort = paramVO.getSort();
		String field = paramVO.getField();
		
		int page = paramVO.getPage();
		int listSize = paramVO.getListSize();
		int from = (page ==1) ? 0 : listSize * (page - 1);
		
		// 엘라스틱 초기화
		SearchRequest searchRequest = new SearchRequest("laws");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		
		// 검색 필드
		String [] fields = SearchUtil.getSearchFieldLAW(field);
		
		// 정렬 ( 이름순으로 정렬, Default는 정확도순)
		if (sort.equals("최신순")) {
			searchSourceBuilder.sort(new FieldSortBuilder("date").order(SortOrder.DESC));
		}
				
		
		// must 내부의 and 조건절 추가를 위한 추가 bool 쿼리 선언
		BoolQueryBuilder semiboolQueryBuilder = QueryBuilders.boolQuery();
		
		// JTBC가 무조건 들어간 카테고리만 검색
//		TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("violt_ctgr_cd_nm", "mois");
//		boolQueryBuilder.must(termQueryBuilder);
		
		// title
		MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("title", paramVO.getSearch());
		boolQueryBuilder.must(semiboolQueryBuilder.should(matchQueryBuilder));
		
		// stitle
		MatchQueryBuilder matchQueryBuilder2 = QueryBuilders.matchQuery("stitle", paramVO.getSearch());
		boolQueryBuilder.must(semiboolQueryBuilder.should(matchQueryBuilder2));
		
		// Content
		MatchQueryBuilder matchQueryBuilder3 = QueryBuilders.matchQuery("content", paramVO.getSearch());
		boolQueryBuilder.must(semiboolQueryBuilder.should(matchQueryBuilder3));
		
		
		// 소스 빌더에 값 넣기
		searchSourceBuilder.query(boolQueryBuilder);
		searchSourceBuilder.from(from);
		searchSourceBuilder.size(listSize);
		searchRequest.source(searchSourceBuilder);
		// 테스트 출력
//		System.out.println(searchSourceBuilder.toString());
		return searchRequest;
	}
	/**
	 * 
	 * @param cri
	 * @param str
	 * @param Category
	 * @return NEWS 결과 반환
	 */
	private SearchRequest getNewsRequest(ParamVO paramVO) {

		// 값 세팅
		String sort = paramVO.getSort();
		String field = paramVO.getField();
		
		int page = paramVO.getPage();
		int listSize = paramVO.getListSize();
		int from = (page ==1) ? 0 : listSize * (page - 1);
		
		// 엘라스틱 초기화
		SearchRequest searchRequest = new SearchRequest("news");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		
		// 검색 필드
		String [] fields = SearchUtil.getSearchFieldLAW(field);
		
		// 정렬 ( 이름순으로 정렬, Default는 정확도순)
		if (sort.equals("최신순")) {
			searchSourceBuilder.sort(new FieldSortBuilder("date").order(SortOrder.DESC));
		}
		
		// must 내부의 and 조건절 추가를 위한 추가 bool 쿼리 선언
		BoolQueryBuilder semiboolQueryBuilder = QueryBuilders.boolQuery();
		
		// JTBC가 무조건 들어간 카테고리만 검색
//		TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("violt_ctgr_cd_nm", "mois");
//		boolQueryBuilder.must(termQueryBuilder);
		
		// title
		MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("title", paramVO.getSearch());
		boolQueryBuilder.must(semiboolQueryBuilder.should(matchQueryBuilder));
		
		// Content(뉴스에선 text 사용)
		MatchQueryBuilder matchQueryBuilder3 = QueryBuilders.matchQuery("text", paramVO.getSearch());
		boolQueryBuilder.must(semiboolQueryBuilder.should(matchQueryBuilder3));
		
		// 정렬 ( 이름순으로 정렬, Default는 정확도순)
		//searchSourceBuilder.sort(new FieldSortBuilder("no").order(SortOrder.ASC));
		searchSourceBuilder.sort(new FieldSortBuilder("_score").order(SortOrder.DESC));
		
		searchSourceBuilder.query(boolQueryBuilder);
		searchSourceBuilder.from(from);
		searchSourceBuilder.size(listSize);
		// 하이라이트 적용
//		searchSourceBuilder.highlighter(makeHighLightField());
		// 테스트 출력
		System.out.println(searchSourceBuilder.toString());
		searchRequest.source(searchSourceBuilder);
		return searchRequest;
	}
	/**
	 * 검색 로그 생성 및 엘라스틱 서치 PUT
	 * @param paramVO
	 * @param total
	 * @throws IOException
	 */
	public void createSearchLog(ParamVO paramVO, long total) throws IOException {
		
		// 값 세팅
		String search = paramVO.getSearch();
		String osearch = paramVO.getOsearch();
		String category = paramVO.getCategory();
		
		Map<String, Object> map = new HashMap<>();
		
		ElasticSearchUtil.createSearchLog(request, restClient, map, search, osearch, category, domain, total);
	}
	
	/**
	 * 검색 로그 생성 및 엘라스틱 서치 PUT
	 * @param response
	 * @param paramVO
	 * @throws IOException
	 */
	public void createSearchLog(SearchResponse response, ParamVO paramVO) throws IOException {
		
		// 값 세팅
		String search = paramVO.getSearch();
		String osearch = paramVO.getOsearch();
		String category = paramVO.getCategory();
		
		Map<String, Object> map = new HashMap<>();
		
		ElasticSearchUtil.createSearchLog(request, response, restClient, map, search, osearch, category, domain);
	}
	/**
	 * 인기검색어 호출, 인기검색어 목록 반환
	 * @return 인기검색어 목록
	 * @throws IOException
	 */
	public List<Map<String, Integer>> getSearchTopWordList() throws IOException {
		int year = 0;
		int month = 0;
		int day = -7;
		int size = 20;
		// 현재 인기 검색어
		List<String> nowSearchTopList = ElasticSearchUtil.getSearchTopList(restClient, year, month, day, size, domain);
		// 마지막 인기 검색어
		List<String> logSearchTopList = ElasticSearchUtil.getSearchLogTopList(restClient, domain);
		return ElasticSearchUtil.getSearchTopListAndRank(nowSearchTopList, logSearchTopList);
	}
	
	/**
	 * 연관검색어 호출, 연관검색어 목록 반환
	 * @param search 검색어
	 * @param minDocCount 최소 노출 횟수
	 * @param size 목록 사이즈
	 * @return 연관검색어 목록
	 * @throws IOException
	 */
	public List<String> getAutoRecommList(String search, int minDocCount, int size) throws IOException {
		return ElasticSearchUtil.getAutoRecommList(restClient, search, domain, minDocCount, size);
	}
	
	/**
	 * 통합검색 검색어 자동완성 호출, 자동완성 반환 
	 * @param search 검색어
	 * @return 통합검색 검색어 자동완성 목록
	 * @throws IOException
	 */
	public List<String> getAutoComplateSearch(String search) throws IOException {
		return ElasticSearchUtil.getKeywordSuggestion(restClient, search);
	}
}
