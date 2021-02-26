package com.mayfarm.core.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class SearchUtil {
	
	
	// MOIS 필드_검색
	public static String[] getSearchFieldMOIS(String field) {
		String[] fields = null;
		
		switch(field) {
			case "제목":
				fields = new String[] { "title", "title.ngram", "stitle", "stitle.ngram" };
				break;
			case "내용":
				fields = new String[] { "content", "content.ngram" };
				break;
			case "첨부파일명":
				fields = new String[] { "linkname", "linkname.ngram" };
				break;
			case "첨부파일_내용":
				fields = new String[] { "_file.content", "_file.content.ngram" };
				break;
			default:
				fields = new String[] {
						"title", "title.ngram",
						"stitle", "stitle.ngram",
						"content", "content.ngram",
						"linkname", "linkname.ngram",
						"_file.content", "_file.content.ngram"
				};
				break;
		}
		
		return fields;
	}
	
	// LAW 필드_검색
	public static String[] getSearchFieldLAW(String field) {
		String[] fields = null;
		switch(field) {
			case "제목":
				fields = new String[] { "title", "title.ngram", "stitle", "stitle.ngram" };
				break;
			case "내용":
				fields = new String[] { "content", "content.ngram" };
				break;
			case "첨부파일명":
				fields = new String[] { "linkname", "linkname.ngram" };
				break;
			case "첨부파일_내용":
				fields = new String[] { "_file.content", "_file.content.ngram" };
				break;
			default:
				fields = new String[] {
						"title", "title.ngram",
						"stitle", "stitle.ngram",
						"content", "content.ngram",
						"linkname", "linkname.ngram",
						"_file.content", "_file.content.ngram"
				};
				break;
		}
		
		return fields;
	}
	// NEWS 필드_검색
	public static String[] getSearchFieldNEWS(String field) {
		String[] fields = null;
		switch(field) {
			case "제목":
				fields = new String[] { "title" };
				break;
			case "내용":
				fields = new String[] { "text" };
				break;
			default:
				fields = new String[] {
						"title",
						"text",
						};
				break;
		}
		
		return fields;
	}
	
	
	// MOIS 필드_하이라이트
	public static String[] getSearchFieldMOIS_highlight(String field) {
		String[] fields = null;
		switch(field) {
		case "제목":
			fields = new String[] { "title", "title.ngram", "stitle", "stitle.ngram" };
			break;
		case "내용":
			fields = new String[] { "content", "content.ngram" };
			break;
		case "첨부파일명":
			fields = new String[] { "linkname" };
			break;
		case "첨부파일_내용":
			fields = new String[] { "_file.content", "_file.content.ngram" };
			break;
		default:
			fields = new String[] {
					"title", "title.ngram",
					"stitle", "stitle.ngram",
					"content", "content.ngram",
					"linkname"
					};
			break;
		}
		return fields;
	}
	
	// LAW 필드_하이라이트
	public static String[] getSearchFieldLAW_highlight(String field) {
		String[] fields = null;
		switch(field) {
		case "제목":
			fields = new String[] { "title", "title.ngram", "stitle", "stitle.ngram" };
			break;
		case "내용":
			fields = new String[] { "content", "content.ngram" };
			break;
		case "첨부파일명":
			fields = new String[] { "linkname" };
			break;
		case "첨부파일_내용":
			fields = new String[] { "_file.content", "_file.content.ngram" };
			break;
		default:
			fields = new String[] {
					"title", "title.ngram",
					"stitle", "stitle.ngram",
					"content", "content.ngram",
					"linkname"
					};
			break;
		}
		return fields;
	}
	// NEWS 필드_하이라이트
	public static String[] getSearchFieldNEWS_highlight(String field) {
		String[] fields = null;
		switch(field) {
		case "제목":
			fields = new String[] { "title" };
			break;
		case "내용":
			fields = new String[] { "text" };
			break;
		default:
			fields = new String[] {
					"title",
					"text",
					};
			break;
		}
		return fields;
	}
	
	// 결과 내 재검색 값 쿼리값들 세팅
	public static String appendSearch(String search, String osearch, String format) {
		Set<String> set = new LinkedHashSet<>();
		List<String> oList = Arrays.asList(osearch.split(format));
		for (String o : oList) {
			synchronized (set) {
				set.add(o.trim());
			}
		}
		List<String> qList = Arrays.asList(search.split(format));
		for (String q : qList) {
			synchronized (set) {
				set.add(q.trim());
			}
		}
		List<String> list = new ArrayList<>();
		list.addAll(set);
		set = null;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			if (StringUtils.isBlank(list.get(i))) {
				continue;
			}
			sb.append(list.get(i).trim());
			if (i < list.size() -1 ) {
				sb.append(format);
			}
		}
		return sb.toString();
	}
}
