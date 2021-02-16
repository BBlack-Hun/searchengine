package com.mayfarm.Search.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mayfarm.Search.vo.Criteria;
import com.mayfarm.Search.vo.ParamVO;

@Repository
public class EsDAO {
	
	@Inject 
	RestHighLevelClient restClient;
	@Inject
	HttpServletRequest request;
	@Inject
	ObjectMapper objectMapper;
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
		int page = paramVO.getPage();
		int listSize = paramVO.getListSize();
		int from = (page ==1) ? 0 : listSize * (page - 1);
		
		// 엘라스틱 초기화
		SearchRequest searchRequest = new SearchRequest("mois");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		
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
	 * @param cri
	 * @param str
	 * @param Category
	 * @return LAW 결과 반환
	 */
	private SearchRequest getLawRequest(ParamVO paramVO) {
			
		// 값 세팅
		int page = paramVO.getPage();
		int listSize = paramVO.getListSize();
		int from = (page ==1) ? 0 : listSize * (page - 1);
		
		// 엘라스틱 초기화
		SearchRequest searchRequest = new SearchRequest("laws");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		
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
	 * @param cri
	 * @param str
	 * @param Category
	 * @return NEWS 결과 반환
	 */
	private SearchRequest getNewsRequest(ParamVO paramVO) {

		// 값 세팅
		int page = paramVO.getPage();
		int listSize = paramVO.getListSize();
		int from = (page ==1) ? 0 : listSize * (page - 1);
		
		// 엘라스틱 초기화
		SearchRequest searchRequest = new SearchRequest("news");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		
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
	// 로그 생성
		
}
