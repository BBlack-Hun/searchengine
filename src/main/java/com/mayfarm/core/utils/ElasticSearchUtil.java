package com.mayfarm.core.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.lucene.search.TermQuery;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.IncludeExclude;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ElasticSearchUtil {

	// 로그생성
	public static void createSearchLog(HttpServletRequest request, RestHighLevelClient restClient,
			Map<String, Object> map, String search, String osearch, String category, String domain, long total)
			throws IOException {

		ObjectMapper objectMapper = new ObjectMapper();

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
		Date date = new Date();
		if (map == null) {
			map = new HashMap<String, Object>();
		}

		// 기본값
		String created = simpleDateFormat.format(date);
		map.put("search", search);
		map.put("osearch", osearch);
		map.put("domain", domain);
		map.put("category", category);
		map.put("total", total);
		map.put("createdDate", created);

		String command = objectMapper.writeValueAsString(map);

		Request r = new Request("POST", "search-log" + "/_doc");
		NStringEntity nStringEntity = new NStringEntity(command, ContentType.APPLICATION_JSON);
		r.setEntity(nStringEntity);
		restClient.getLowLevelClient().performRequest(r);

	}

	// 로그생성 2
	public static void createSearchLog(HttpServletRequest request, SearchResponse response,
			RestHighLevelClient restClient, Map<String, Object> map, String search, String osearch, String category,
			String domain) throws IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
		Date date = new Date();
		if (map == null) {
			map = new HashMap<String, Object>();
		}

		// 기본값
		String created = simpleDateFormat.format(date);
		long total = response.getHits().getTotalHits().value;
		map.put("search", search);
		map.put("osearch", osearch);
		map.put("domain", domain);
		map.put("category", category);
		map.put("total", total);
		map.put("createDate", created);

		String command = objectMapper.writeValueAsString(map);

		Request r = new Request("POST", "search-log" + "/_doc");
		NStringEntity nStringEntity = new NStringEntity(command, ContentType.APPLICATION_JSON);
		r.setEntity(nStringEntity);
		restClient.getLowLevelClient().performRequest(r);
	}

	// 현재 인기 검색어 추출
	public static List<String> getSearchTopList(RestHighLevelClient restClient, int year, int month, int day, int size,
			String domain) throws IOException {

		List<String> topList = new ArrayList<>();
		SearchRequest searchRequest = new SearchRequest("search-log");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		SearchResponse searchResponse = null;
		BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

		String format = "yyyy-MM-dd";

		Calendar calendar = new GregorianCalendar(Locale.KOREA);
		// 나중에 DateUtil 소스 분석하기!
		String from = DateUtil.calendarCalculator(calendar, year, month, day, format);
		String to = DateUtil.getNow(format); // 현재 날짜

		// 해당 기간만
		RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("createdDate").gte(from).lte(to).format(format);
		boolQueryBuilder.must(rangeQueryBuilder);

		// 검색 결과가 0인 것은 제외
		TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("total", 0);
		boolQueryBuilder.mustNot(termQueryBuilder);

		// 도메인
		boolMustDomainTerm(boolQueryBuilder, domain);

		// 인기 Term
		AggregationBuilder aggregationBuilder = AggregationBuilders.terms("top").field("search").size(size);

		searchSourceBuilder.query(boolQueryBuilder);
		searchSourceBuilder.aggregation(aggregationBuilder);
		searchRequest.source(searchSourceBuilder);
		searchResponse = restClient.search(searchRequest, RequestOptions.DEFAULT);

		// 결과 리턴
		if (searchResponse.getAggregations() != null) {
			Terms aggr = searchResponse.getAggregations().get("top");
			for (Terms.Bucket entry : aggr.getBuckets()) {
				topList.add(entry.getKeyAsString());
			}
		}

		return topList;

	}

	// 마지막 인기검색어 로그 추출
	public static List<String> getSearchLogTopList(RestHighLevelClient restClient, String domain) throws IOException {

		List<String> topList = new ArrayList<>();
		SearchRequest searchRequest = new SearchRequest("search-top-log");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		SearchResponse searchResponse = null;

		FieldSortBuilder sort = new FieldSortBuilder("createdDate").order(SortOrder.DESC);
		searchSourceBuilder.sort(sort);
		searchSourceBuilder.size(1);
		searchRequest.source(searchSourceBuilder);
		searchResponse = restClient.search(searchRequest, RequestOptions.DEFAULT);

		for (SearchHit hit : searchResponse.getHits().getHits()) {
			Object obj = hit.getSourceAsMap().get("topList");
			if (obj instanceof List) {
				List<String> tops = (List<String>) obj;
				topList.addAll(tops);
			} else if (obj instanceof String) {
				topList.add(String.valueOf(obj));
			}
		}
		return topList;
	}

	/*
	 * 질문
	 * // 로그 생성은 언제 하는가???? 
	 * // 인기검색어 로그 생성
	 */	
	public static void createSearchTopLog(List<String> topList, RestHighLevelClient restClient, String domain)
			throws IOException {
		if (topList == null && topList.size() == 0) {
			return;
		}

		String format = "yyyy-MM-dd HH:mm:ss";
		String created = DateUtil.getNow(format);

		Map<String, Object> map = new HashMap<>();
		map.put("createdDate", created);
		map.put("domain", domain);
		map.put("topList", topList);

		ObjectMapper objectMapper = new ObjectMapper();
		String command = objectMapper.writeValueAsString(map);

		Request request = new Request("POST", "search-top-log" + "/_doc");
		NStringEntity nStrintEntity = new NStringEntity(command, ContentType.APPLICATION_JSON);
		request.setEntity(nStrintEntity);
		Response response = restClient.getLowLevelClient().performRequest(request);
	}

	// 인기검색어 리스트 가져오기
	public static List<Map<String, Integer>> getSearchTopListAndRank(List<String> nowSearchTopList,
			List<String> logSearchTopList) {

		// 인기검색어에 순위 붙이기
		int rank = 1;
		List<Map<String, Integer>> nowSearchTop = new ArrayList<Map<String, Integer>>();
		for (String topWord : nowSearchTopList) {
			Map<String, Integer> map = new HashMap<String, Integer>();
			map.put(topWord, rank++);
			nowSearchTop.add(map);
		}

		// 인기검색어에 순위 붙이기
		rank = 1;
		List<Map<String, Integer>> logSearchTop = new ArrayList<Map<String, Integer>>();
		for (String topWord : logSearchTopList) {
			Map<String, Integer> map = new HashMap<String, Integer>();
			map.put(topWord, rank++);
			logSearchTop.add(map);
		}

		// index
		int index = 0;
		// flag
		boolean is = false;
		// 순위차이
		int gap = 0;

		for (Map<String, Integer> topWordMap : new ArrayList<>(nowSearchTop)) {
			for (String topWord : new HashMap<>(topWordMap).keySet()) { // 인기검색어 목록

				// 빈 단어 제외
				if (StringUtils.isBlank(topWord)) {
					nowSearchTop.remove(index);
				}
				for (Map<String, Integer> logWordMap : logSearchTop) {
					for (String logWord : logWordMap.keySet()) { // 인기검색어 로그의 인기검색어 목록
						if (topWord.equals(logWord)) { // 같은 단어가 있으면
							is = true;
							int topWordVal = topWordMap.get(topWord); // 인기검색어 순위
							int logWordVal = logWordMap.get(logWord); // 로그인기검색어 순위
							gap = logWordVal - topWordVal;
						}
					}
				}
				if (is) { // 같은 단어가 있으면
					topWordMap.put(topWord, gap); // 그 차이를 값으로 넣어주고
					is = false; // flag를 초기화
					gap = 0; // gap 초기화
				} else { // 같은 단어가 없으면 새롭게 들어온 검색어니까
					topWordMap.put(topWord, 999); // 999를 넣어 new임을 알아본다.
				}
			}
			index++;
		}
		return nowSearchTop;

	}
	
	// 자동추천검색어 리스트(로그기준)
	public static List<String> getAutoRecommList(RestHighLevelClient restClient, String keyword, String domain, int minDocCount, int listSize) throws IOException {
		List<String> result = new ArrayList<>();
		if (StringUtils.isBlank(keyword)) {
			return result;
		}
		LinkedHashSet<String> autoRecommSet = new LinkedHashSet<>(); // 자동추천검색어
		LinkedHashSet<String> manualRecommSet = new LinkedHashSet<>(); // 수동추천검색어
		MultiSearchRequest multiSearchRequest = new MultiSearchRequest();
		MultiSearchResponse multiSearchResponse = null;
		
		// **********************자동추천검색어(로그)************************
		SearchRequest searchRequestForAuto = new SearchRequest("search-log");
		SearchSourceBuilder searchSourceBuilderForAuto = new SearchSourceBuilder();
		BoolQueryBuilder boolQueryBuilderForAuto = QueryBuilders.boolQuery();
		boolMustDomainTerm(boolQueryBuilderForAuto, domain);
		TermQueryBuilder termQueryBuilderForAuto = QueryBuilders.termQuery("osearch", keyword);
		boolQueryBuilderForAuto.must(termQueryBuilderForAuto);
		
		IncludeExclude ie = new IncludeExclude(null, new String[] {keyword});
		AggregationBuilder aggregationBuilderForAuto = AggregationBuilders.terms("auto").field("search").size(50).minDocCount(minDocCount)
				.includeExclude(ie);
		
		searchSourceBuilderForAuto.query(boolQueryBuilderForAuto);
		searchSourceBuilderForAuto.aggregation(aggregationBuilderForAuto);
		searchSourceBuilderForAuto.size(0);
		searchRequestForAuto.source(searchSourceBuilderForAuto);
		// ******************************************************************
		
		// **********************수동추천검색어******************************
		SearchRequest searchRequestForManual = new SearchRequest("recomm-manual-dict");
		SearchSourceBuilder searchSourceBuilderForManual = new SearchSourceBuilder();
		TermQueryBuilder termQueryBuilderFormanual = QueryBuilders.termQuery("recomm",  keyword);
		AggregationBuilder aggregationbuilderForManual = AggregationBuilders.terms("manual").field("recomm")
				.order(BucketOrder.aggregation("date", false));
		AggregationBuilder subAggregationBuilderForManual = AggregationBuilders.max("date").field("updatedDate");
		aggregationbuilderForManual.subAggregation(subAggregationBuilderForManual);
		searchSourceBuilderForManual.query(termQueryBuilderFormanual);
		searchSourceBuilderForManual.aggregation(aggregationbuilderForManual);
		searchSourceBuilderForManual.size(0);
		searchRequestForManual.source(searchSourceBuilderForManual);
		// ******************************************************************
		
		//검색
	}

	// 불쿼리에 도메인 must
	public static void boolMustDomainTerm(BoolQueryBuilder boolQueryBuilder, String domain) {
		if (!StringUtils.isBlank(domain) && !domain.equals("*")) {
			TermQueryBuilder termQueryBuilderForDomain = QueryBuilders.termQuery("domain", domain);
			boolQueryBuilder.must(termQueryBuilderForDomain);
		}
	}
}
