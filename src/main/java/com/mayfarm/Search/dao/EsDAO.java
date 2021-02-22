package com.mayfarm.Search.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;
import javax.management.Query;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.RegexpQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Repository;

import com.mayfarm.Search.vo.ParamVO;
import com.mayfarm.core.utils.ElasticSearchUtil;
import com.mayfarm.core.utils.RefineUtil;
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
	private final int slop = 1;
	private final String searchDateFormat = "yyyy-MM-dd";
	
	private void setSearch(ParamVO paramVO) throws IOException {
		String search = paramVO.getSearch();
		String osearch = paramVO.getOsearch();
		String exactSearch = paramVO.getExactSearch();
		String includeSearch = paramVO.getIncludeSearch();
		String excludeSearch = paramVO.getExcludeSearch();
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
		String startDate = paramVO.getStartDate();
		String endDate = paramVO.getEndDate();
		
		int page = paramVO.getPage();
		int listSize = paramVO.getListSize();
		int from = (page ==1) ? 0 : listSize * (page - 1);
		String field = paramVO.getField();
		
		// 엘라스틱 초기화
		SearchRequest searchRequest = new SearchRequest("mois");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		
		// 검색 필드
		String [] fields = SearchUtil.getSearchFieldMOIS(field);
		
		// 정렬 ( 이름순으로 정렬, Default는 정확도순)
		if (sort.equals("최신순")) {
			searchSourceBuilder.sort(new FieldSortBuilder("regdt").order(SortOrder.DESC));
		}
		
		// 기간
		if (!StringUtils.isBlank(startDate) && !StringUtils.isBlank(endDate)) {
			RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("regdt").gte(startDate).lte(endDate).format(searchDateFormat);
			boolQueryBuilder.must(rangeQueryBuilder);
		}
		
		// must 내부의 and 조건절 추가를 위한 추가 bool 쿼리 선언
//		BoolQueryBuilder semiboolQueryBuilder = QueryBuilders.boolQuery();
		
		// JTBC가 무조건 들어간 카테고리만 검색
//		TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("violt_ctgr_cd_nm", "mois");
//		boolQueryBuilder.must(termQueryBuilder);
		
		/*
		 * // title MatchQueryBuilder matchQueryBuilder =
		 * QueryBuilders.matchQuery("title", paramVO.getSearch());
		 * boolQueryBuilder.must(semiboolQueryBuilder.should(matchQueryBuilder));
		 * 
		 * // stitle MatchQueryBuilder matchQueryBuilder2 =
		 * QueryBuilders.matchQuery("stitle", paramVO.getSearch());
		 * boolQueryBuilder.must(semiboolQueryBuilder.should(matchQueryBuilder2));
		 * 
		 * // Content MatchQueryBuilder matchQueryBuilder3 =
		 * QueryBuilders.matchQuery("content", paramVO.getSearch());
		 * boolQueryBuilder.must(semiboolQueryBuilder.should(matchQueryBuilder3));
		 */
		
		// 검색 쿼리 생성
		setSearchQuery(paramVO, boolQueryBuilder, fields);
		
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
		String startDate = paramVO.getStartDate();
		String endDate = paramVO.getEndDate();
		
		int page = paramVO.getPage();
		int listSize = paramVO.getListSize();
		int from = (page ==1) ? 0 : listSize * (page - 1);
		String field = paramVO.getField();
		
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
		
		// 기간
		if (!StringUtils.isBlank(startDate) && !StringUtils.isBlank(endDate)) {
			RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("date").gte(startDate).lte(endDate).format(searchDateFormat);
			boolQueryBuilder.must(rangeQueryBuilder);
		}
		
		/*
		 * // must 내부의 and 조건절 추가를 위한 추가 bool 쿼리 선언 BoolQueryBuilder
		 * semiboolQueryBuilder = QueryBuilders.boolQuery();
		 * 
		 * // JTBC가 무조건 들어간 카테고리만 검색 // TermQueryBuilder termQueryBuilder =
		 * QueryBuilders.termQuery("violt_ctgr_cd_nm", "mois"); //
		 * boolQueryBuilder.must(termQueryBuilder);
		 * 
		 * // title MatchQueryBuilder matchQueryBuilder =
		 * QueryBuilders.matchQuery("title", paramVO.getSearch());
		 * boolQueryBuilder.must(semiboolQueryBuilder.should(matchQueryBuilder));
		 * 
		 * // stitle MatchQueryBuilder matchQueryBuilder2 =
		 * QueryBuilders.matchQuery("stitle", paramVO.getSearch());
		 * boolQueryBuilder.must(semiboolQueryBuilder.should(matchQueryBuilder2));
		 * 
		 * // Content MatchQueryBuilder matchQueryBuilder3 =
		 * QueryBuilders.matchQuery("content", paramVO.getSearch());
		 * boolQueryBuilder.must(semiboolQueryBuilder.should(matchQueryBuilder3));
		 */
		
		// 검색 쿼리 생성
		setSearchQuery(paramVO, boolQueryBuilder, fields);
		
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
		String startDate = paramVO.getStartDate();
		String endDate = paramVO.getEndDate();
		
		int page = paramVO.getPage();
		int listSize = paramVO.getListSize();
		int from = (page ==1) ? 0 : listSize * (page - 1);
		String field = paramVO.getField();
		
		// 엘라스틱 초기화
		SearchRequest searchRequest = new SearchRequest("news");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		
		// 검색 필드
		String [] fields = SearchUtil.getSearchFieldNEWS(field);
		
		// 정렬 ( 이름순으로 정렬, Default는 정확도순)
		if (sort.equals("최신순")) {
			searchSourceBuilder.sort(new FieldSortBuilder("date").order(SortOrder.DESC));
		}
		
		// 기간
		if (!StringUtils.isBlank(startDate) && !StringUtils.isBlank(endDate)) {
			RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("date").gte(startDate).lte(endDate).format(searchDateFormat);
			boolQueryBuilder.must(rangeQueryBuilder);
		}
		
		/*
		 * // must 내부의 and 조건절 추가를 위한 추가 bool 쿼리 선언 BoolQueryBuilder
		 * semiboolQueryBuilder = QueryBuilders.boolQuery();
		 * 
		 * // JTBC가 무조건 들어간 카테고리만 검색 // TermQueryBuilder termQueryBuilder =
		 * QueryBuilders.termQuery("violt_ctgr_cd_nm", "mois"); //
		 * boolQueryBuilder.must(termQueryBuilder);
		 * 
		 * // title MatchQueryBuilder matchQueryBuilder =
		 * QueryBuilders.matchQuery("title", paramVO.getSearch());
		 * boolQueryBuilder.must(semiboolQueryBuilder.should(matchQueryBuilder));
		 * 
		 * // Content(뉴스에선 text 사용) MatchQueryBuilder matchQueryBuilder3 =
		 * QueryBuilders.matchQuery("text", paramVO.getSearch());
		 * boolQueryBuilder.must(semiboolQueryBuilder.should(matchQueryBuilder3));
		 */
		// 검색 쿼리 생성
		setSearchQuery(paramVO, boolQueryBuilder, fields);
		
		searchSourceBuilder.query(boolQueryBuilder);
		searchSourceBuilder.from(from);
		searchSourceBuilder.size(listSize);
		// 테스트 출력
//		System.out.println(searchSourceBuilder.toString());
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
	
	private void setSearchQuery(ParamVO paramVO, BoolQueryBuilder boolQueryBuilder, String[] fields) {
		
		// 값 세팅
		String search = paramVO.getSearch();
		String exactSearch = paramVO.getExactSearch();
		String includeSearch = paramVO.getIncludeSearch();
		String excludeSearch = paramVO.getExcludeSearch();
		
		BoolQueryBuilder boolQueryBuilderForSearch = QueryBuilders.boolQuery();
		BoolQueryBuilder boolQueryBuilderForExact = QueryBuilders.boolQuery();
		BoolQueryBuilder boolQueryBuilderForInclude = QueryBuilders.boolQuery();
		BoolQueryBuilder boolQueryBuilderForExclude = QueryBuilders.boolQuery();
		
		// 상세검색
		// 정확히 일치
		if (StringUtils.isNoneBlank(exactSearch)) {
			String[] exactArray = exactSearch.split(",+");
			for (String exact : exactArray) {
				if (StringUtils.isBlank(exact)) {
					continue;
				}
				exact = RefineUtil.escapeString(exact);
				BoolQueryBuilder boolQueryBilderForExactInner = QueryBuilders.boolQuery();
				// 일반영역
				for (String f : fields) {
					if (f.contains("ngram")) {
						continue;
					} else {
						f = f + ".keyword";
					}
					RegexpQueryBuilder regexpQueryBuilder = QueryBuilders.regexpQuery(f, ".*" + exact + ".*");
					 boolQueryBilderForExactInner.should(regexpQueryBuilder);
				}
				boolQueryBuilderForExact.must(boolQueryBilderForExactInner);
			}
			boolQueryBuilder.must(boolQueryBuilderForExact);
		}
		
		// 반드시 포함
		if (StringUtils.isNoneBlank(includeSearch)) {
			String[] includeSearchArray = includeSearch.split(",+");
			for (String include : includeSearchArray) {
				if (StringUtils.isBlank(include)) {
					continue;
				}
				include = RefineUtil.escapeString(include);
				BoolQueryBuilder boolQueryBuilderForIncludeQueryInner = QueryBuilders.boolQuery();
				// 일반 영역
				MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(include, fields)
						.operator(Operator.AND);
				boolQueryBuilderForIncludeQueryInner.should(multiMatchQueryBuilder);
				boolQueryBuilderForInclude.must(boolQueryBuilderForIncludeQueryInner);
			}
			boolQueryBuilder.must(boolQueryBuilderForInclude);
		}
		
		// 제외 단어
		if (StringUtils.isNoneBlank(excludeSearch)) {
			String[] excludeSearchArray = excludeSearch.split(",+");
			for (String exclude : excludeSearchArray) {
				if (StringUtils.isBlank(excludeSearch)) {
					continue;
				}
				exclude = RefineUtil.escapeString(exclude);
				BoolQueryBuilder boolQeuryBuilderForExcludeQueryInner = QueryBuilders.boolQuery();
				// 일반 영역
				MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(exclude, fields)
						.operator(Operator.AND);
				boolQeuryBuilderForExcludeQueryInner.should(multiMatchQueryBuilder);
				boolQueryBuilderForExclude.must(boolQeuryBuilderForExcludeQueryInner);
			}
			boolQueryBuilder.mustNot(boolQueryBuilderForExclude);
		}
		
		// 검색
		BoolQueryBuilder boolQueryBuilderForSearch_inner = QueryBuilders.boolQuery();
		
		MatchPhraseQueryBuilder[] MatchPhraseQueryBuilder = ElasticSearchUtil.getMatchPhraseQueryBuilders(
				fields, search, slop);
		for (MatchPhraseQueryBuilder matchPhraseQueryBuilder : MatchPhraseQueryBuilder) {
			boolQueryBuilderForSearch_inner.should(matchPhraseQueryBuilder);
		}
		boolQueryBuilderForSearch.must(boolQueryBuilderForSearch_inner);
		boolQueryBuilder.must(boolQueryBuilderForSearch);
	}
	
}
