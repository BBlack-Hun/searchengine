package com.mayfarm.Search.dao;

import java.io.IOException;

import javax.inject.Inject;

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

import com.mayfarm.Search.vo.Criteria;

@Repository
public class EsDAO {
	
	@Inject 
	RestHighLevelClient restClient;
	
	public MultiSearchResponse TSearch(String str, Criteria cri, String Category) throws IOException {
		
		// 통합 검색을 위한 MultiSearch 초기화
		MultiSearchRequest multiSerachRequest = new MultiSearchRequest();
		
		// MOIS 
		SearchRequest searchRequest_mois = getMoisRequest(cri, str, Category);
		multiSerachRequest.add(searchRequest_mois);
		// LAW	
		SearchRequest searchRequest_law = getLawRequest(cri, str, Category);
		multiSerachRequest.add(searchRequest_law);
		// News
		SearchRequest searchRequest_news = getNewsRequest(cri, str, Category);
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
	public SearchResponse Msearch(String str, Criteria cri, String Category) throws IOException {
		SearchRequest searchRequest = getMoisRequest(cri, str, Category);
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
	public SearchResponse Lsearch(String str, Criteria cri, String Category) throws IOException {
		SearchRequest searchRequest = getLawRequest(cri, str, Category);
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
	public SearchResponse Nsearch(String str, Criteria cri, String Category) throws IOException {
		SearchRequest searchRequest = getNewsRequest(cri, str, Category);
		return restClient.search(searchRequest, RequestOptions.DEFAULT);
	}
	
	private SearchRequest getMoisRequest(Criteria cri, String str, String Category) {
		
		// 변수 초기화 (pagination)
		int listSize = cri.getPerPageNum();
		
		// 카테고리가 빈값일 경우,
		if (Category =="통합검색") {
			listSize = 4;
		} else {
			listSize = (listSize < 10) ? 10 : listSize;
		}
		
		// 값 세팅
		int page = cri.getPage();
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
		MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("title", str);
		boolQueryBuilder.must(semiboolQueryBuilder.should(matchQueryBuilder));
		
		// stitle
		MatchQueryBuilder matchQueryBuilder2 = QueryBuilders.matchQuery("stitle", str);
		boolQueryBuilder.must(semiboolQueryBuilder.should(matchQueryBuilder2));
		
		// Content
		MatchQueryBuilder matchQueryBuilder3 = QueryBuilders.matchQuery("content", str);
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
	
	private SearchRequest getLawRequest(Criteria cri, String str, String Category) {
		
		// 변수 초기화 (pagination)
		int listSize = cri.getPerPageNum();
		
		// 카테고리가 빈값일 경우,
		if (Category =="통합검색") {
			listSize = 4;
		} else {
			listSize = (listSize < 10) ? 10 : listSize;
		}
		
		// 값 세팅
		int page = cri.getPage();
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
		MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("title", str);
		boolQueryBuilder.must(semiboolQueryBuilder.should(matchQueryBuilder));
		
		// stitle
		MatchQueryBuilder matchQueryBuilder2 = QueryBuilders.matchQuery("stitle", str);
		boolQueryBuilder.must(semiboolQueryBuilder.should(matchQueryBuilder2));
		
		// Content
		MatchQueryBuilder matchQueryBuilder3 = QueryBuilders.matchQuery("content", str);
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
	
	private SearchRequest getNewsRequest(Criteria cri, String str, String Category) {
		
		// 변수 초기화 (pagination)
		int listSize = cri.getPerPageNum();
		
		// 카테고리가 빈값일 경우,
		if (Category =="통합검색") {
			listSize = 4;
		} else {
			listSize = (listSize < 10) ? 10 : listSize;
		}
		
		// 값 세팅
		int page = cri.getPage();
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
		MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("title", str);
		boolQueryBuilder.must(semiboolQueryBuilder.should(matchQueryBuilder));
		
		// Content(뉴스에선 text 사용)
		MatchQueryBuilder matchQueryBuilder3 = QueryBuilders.matchQuery("text", str);
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
}
