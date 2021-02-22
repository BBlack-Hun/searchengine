package com.mayfarm.core.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.lucene.search.TermQuery;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.MultiSearchResponse.Item;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.search.MatchQuery;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.bucket.terms.IncludeExclude;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ElasticSearchUtil {
	
	
	// MatchPhraseQUeryBuilder 배열 생성
	public static MatchPhraseQueryBuilder[] getMatchPhraseQueryBuilders(String[] fields, String search, int slop) {
		MatchPhraseQueryBuilder[] matchQuery = new MatchPhraseQueryBuilder[fields.length];
		
		for (int i = 0; i < fields.length; i++) {
			matchQuery[i] = QueryBuilders.matchPhraseQuery(fields[i], search).slop(slop);
		}
		return matchQuery;
	}

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
		map.put("query", search);
		map.put("oquery", osearch);
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
		map.put("query", search);
		map.put("oquery", osearch);
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
		AggregationBuilder aggregationBuilder = AggregationBuilders.terms("top").field("query").size(size);

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
		// 수동추천 index
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
		multiSearchRequest.add(searchRequestForAuto);
		multiSearchRequest.add(searchRequestForManual);
		multiSearchResponse = restClient.msearch(multiSearchRequest, RequestOptions.DEFAULT);
		Item[] item = multiSearchResponse.getResponses();
		
		// **********************자동추천검색어******************************
		SearchResponse searchResponseForAuto = item[0].getResponse();
		Map<String, Aggregation> aggsMapForAuto = searchResponseForAuto.getAggregations().asMap();
		Terms termsForAuto = (Terms) aggsMapForAuto.get("auto");
		for (Terms.Bucket bucket : termsForAuto.getBuckets()) {
			synchronized (autoRecommSet) {
				autoRecommSet.add(bucket.getKeyAsString());
			}
		}
		// ******************************************************************
		
		// **********************수동추천검색어******************************
		SearchResponse searchResponseForManual = item[1].getResponse();
		
		if (searchResponseForManual.getAggregations() != null) {
			Terms aggrForManual = searchResponseForManual.getAggregations().get("manual");
			for (Terms.Bucket entry : aggrForManual.getBuckets()) {
				synchronized (manualRecommSet) {
					manualRecommSet.add(entry.getKeyAsString());
				}
			}
		}
		// ******************************************************************
		
		// 자동 추천, 수동추천 더하기
		autoRecommSet.addAll(manualRecommSet);
		result.addAll(autoRecommSet);
		
		autoRecommSet = null;
		manualRecommSet = null;
		return ArrayUtil.arrayListCut(result, listSize);
		
	}
	
	// 검색어 자동완성 가져오기
	public static List<String> getKeywordSuggestion(RestHighLevelClient restClient, String keyword) throws IOException {
		List<String> list = new ArrayList<String>();
		if (StringUtils.isBlank(keyword)) {
			return list;
		}
		// 자동완성 index
		SearchRequest searchRequest = new SearchRequest("autocomplete-dict");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("keyword", keyword).operator(Operator.AND);
		String[] sortField = new String[] {"len", "keyword.keyword"};
		for (String field : sortField) {
			searchSourceBuilder.sort(new FieldSortBuilder(field).order(SortOrder.ASC));
		}
		searchSourceBuilder.query(matchQueryBuilder);
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = restClient.search(searchRequest, RequestOptions.DEFAULT);
		
		for (SearchHit hit : searchResponse.getHits().getHits()) {
			list.add(hit.getSourceAsMap().get("keyword")+ "");
		}
		
		return list;
	}

	// 불쿼리에 도메인 must
	public static void boolMustDomainTerm(BoolQueryBuilder boolQueryBuilder, String domain) {
		if (!StringUtils.isBlank(domain) && !domain.equals("*")) {
			TermQueryBuilder termQueryBuilderForDomain = QueryBuilders.termQuery("domain", domain);
			boolQueryBuilder.must(termQueryBuilderForDomain);
		}
	}
	
	
	/**
	 * 
	 *
	 * 하이라이트 관련 유틸 
	 * 
	 */
	
	// 일반 하이라이트 필드를 sourceMap에 덮어쓰기
	public static void coverHighlightField(Map<String, Object> sourceMap, Map<String, HighlightField> highlightFields) {
		for (String field : highlightFields.keySet()) {
			HighlightField highlightField = highlightFields.get(field);
			Text[] texts = highlightField.getFragments();
			String value = texts[0].string();
			sourceMap.put(field, value);
		}
	}
	
	// return Source 하이라이트 생성
	public static void sourceToHighlight(Map<String, Object> source, String[] highlightFields, String[] keywords, String preTags, String postTags) {
		for (String highlightField : highlightFields) {
			String[] fields = highlightField.split("\\.+");
			int fieldsLen = fields.length;
			if (source.get(highlightField) instanceof List) {
				List<String> list = (ArrayList<String>)source.get(highlightField);
				int i = 0;
				for (String content : new ArrayList<>(list)) {
					if(!StringUtils.isBlank(content)) {
						content = ElasticSearchUtil.makeHighlight(content, keywords, preTags, postTags);
						list.set(i, content);
					}
					i++;
				}
			} else if(source.get(highlightField) instanceof String) {
				String refine = ElasticSearchUtil.makeHighlight(String.valueOf(source.get(highlightField)), keywords, preTags, postTags);
				source.put(highlightField, refine);
			}
		}
	}
	
	// 하이라이트 필드 생성
	public static String makeHighlight(String content, String[] keywords, String preTags, String postTags) {
		
		keywords = getKeywordForHighlight(keywords);
		String fieldWithOutTag = getFieldWithOutTag(content, preTags, postTags);
		Set<String> wordSet = new HashSet<>(Arrays.asList(keywords));
		Set<String> wordSetByHighLightField = getWordSet(content, preTags, postTags);
		wordSet.removeAll(wordSetByHighLightField);
		
		Map<String, List<Integer>> wordIndexMap = getWordIndexMap(content, preTags, postTags);
		Map<String, List<Integer>> wordIndexMapByWithOutTagField = getWordIndexMap(fieldWithOutTag.toUpperCase(), wordSet.toArray(new String[wordSet.size()]));
		wordIndexMap.putAll(wordIndexMapByWithOutTagField);
		
		List<Integer> indexList = getIndexList(wordIndexMap);
		Map<Integer, Integer> indexWordLengthMap = getIndexWordLengthMap(wordIndexMap);
		
		String highlightField = getHighlightField(fieldWithOutTag, indexList, indexWordLengthMap, preTags, postTags);
		return getHighlightFieldPretty(highlightField, preTags, postTags);
	}
	
	// 하이라이트를 원하는 단어리스트를 메소드에 알맞게 수정
	private static String[] getKeywordForHighlight(String[] keywords) {
		Set<String> keywordSet = new HashSet<>();
		for (String keyword : keywords) {
			Set<String> set = Arrays.stream(keyword.split("\\s+")) // \\s+는 공백, 탭 등의 문자가 하나 이상 있으면 몽창 묶어서 split()해준다.
					.map(x -> {
						return x.toUpperCase();
					})
					.collect(Collectors.toSet());
			keywordSet.addAll(set);
		}
		
		return keywordSet.toArray(new String[keywordSet.size()]);
	}
	
	// 하이라이트가 된 필드에서 하이라이트 단어 추출
	private static Set<String> getWordSet(String field, String preTags, String postTags) {
		Set<String> wordSet = new HashSet<String>();
		int preTagIndex = -1;
		while ((preTagIndex = field.indexOf(preTags, preTagIndex + 1)) > -1 ) {
			int preTagsEndIndex = preTagIndex + preTags.length();
			int postTagIndex = field.indexOf(postTags, preTagsEndIndex);
			String word = field.substring(preTagsEndIndex, postTagIndex);
			wordSet.add(word);
		}
		return wordSet;
	}
	
	// 하이라이트가 된 필드에서 뽑은 {하이랑트 단어 : index} 맵
	private static Map<String, List<Integer>> getWordIndexMap(String field, String preTags, String postTags) {
		Map<String, List<Integer>> wordIndexMap = new HashMap<String, List<Integer>>();
		int preTagIndex = -1;
		int error = 0;
		while ((preTagIndex = field.indexOf(preTags, preTagIndex + 1)) > -1) {
			int preTagsEndIndex = preTagIndex + preTags.length();
			int postTagIndex = field.indexOf(postTags, preTagsEndIndex);
			String word = field.substring(preTagsEndIndex, postTagIndex);
			if (!wordIndexMap.containsKey(word)) {
				wordIndexMap.put(word, new ArrayList<Integer>());
			}
			List<Integer> indexList = wordIndexMap.get(word);
			indexList.add(preTagIndex - error);
			error += preTags.length() + postTags.length();
		}
		return wordIndexMap;
	}
	
	// 필드에서 내가 집접 하이라이트 할 단어로 뽑은 {하이라이트 단어 : index} 맵
	private static Map<String, List<Integer>> getWordIndexMap(String field, String[] words) {
		Map<String, List<Integer>> wordIndexMap = new HashMap<String, List<Integer>>();
		for (String word : words) {
			String tempField = field;
			int index = -1;
			while ((index = tempField.indexOf(word, index + 1)) > -1) {
				if (!wordIndexMap.containsKey(word)) {
					wordIndexMap.put(word,  new ArrayList<Integer>());
				}
				List<Integer> indexList = wordIndexMap.get(word);
				indexList.add(index);
			}
		}
		return wordIndexMap;
	}
	
	// 하이라이트가 된 필드에서 태그 제거
	private static String getFieldWithOutTag(String field, String preTags, String postTags) {
		field = field.replace(preTags, "");
		field = field.replace(postTags, "");
		return field;
	}
	
	// {하이라이트 단어 : index} 으로 뽑은 인덱스별 단어길이 맵
	private static Map<Integer, Integer> getIndexWordLengthMap(Map<String, List<Integer>> wordIndexMap) {
		Map<Integer, Integer> indexWordLengthMap = new HashMap<Integer, Integer>();
		for (String word : wordIndexMap.keySet()) {
			int wordLength = word.length();
			List<Integer> indexList = wordIndexMap.get(word);
			for (Integer index : indexList) {
				indexWordLengthMap.put(index, wordLength);
			}
		}
		return indexWordLengthMap;
	}
	
	// {하이라이트 단어 : index} 으로 뽑은 인덱스 리스트
	private static List<Integer> getIndexList(Map<String, List<Integer>> wordIndexMap) {
		List<Integer> indexList = new ArrayList<Integer>();
		for (String word : wordIndexMap.keySet()) {
			indexList.addAll(wordIndexMap.get(word));
		}
		Collections.sort(indexList);
		return indexList;
	}
	
	// 하이라트 태그 제거된 필드 뽑은 하이라이트 필드
	private static String getHighlightField(String field, List<Integer> indexList, Map<Integer, Integer> indexWordLengthMap, String preTags, String postTags) {
		List<String> fieldList = new ArrayList<String>(Arrays.asList(field.split("")));
		for (int index: indexList) {
			int gap = indexWordLengthMap.get(index) -1;
			String preWord = fieldList.get(index);
			fieldList.set(index,  preTags+ preWord);
			fieldList.set(index+gap,  fieldList.get(index+gap) + postTags);
		}
		return String.join("", fieldList);
	}
	
	// 하이라이트 필드 이쁘게 표현
	private static String getHighlightFieldPretty(String field, String preTags, String postTags) {
		// 이해 안됨
		List<String> prettyList = Arrays.stream(field.split("\\(\r\n|\r|\n|\n\r")).filter( p -> {
			return p.contains(preTags);
		}).collect(Collectors.toList());
		
		if (prettyList.size() == 0) {
			return field;
		}
		
		StringBuffer stringBuffer = new StringBuffer();
		int distance = 100;
		int maxSize = 300;
		int postTagsLength = postTags.length();
		for (String pretty : prettyList) {
			
			String tempPretty = pretty;
			tempPretty = tempPretty.replaceAll("("+preTags+")|("+postTags+")", "");
			
			if (tempPretty.length() > maxSize) {
				int preTagIndex = pretty.indexOf(preTags);
				int postTagIndex = pretty.lastIndexOf(postTags);
				if (preTagIndex > distance ) {
					int sub = preTagIndex - distance;
					pretty = pretty.substring(sub);
					pretty = "..." + pretty;
					postTagIndex = postTagIndex - sub + 3;
				}
				if ( (pretty.length() - (postTagIndex + postTagsLength)) > distance) {
					pretty = pretty.substring(0, postTagIndex + postTagsLength + distance);
					pretty = pretty + "...";
				}
			}
			stringBuffer.append(pretty);
			stringBuffer.append(" ");
		}
		return stringBuffer.toString();
	}
	
	/**
	 * 
	 *
	 * 하이라이트 관련 유틸 끝 
	 * 
	 */
}
