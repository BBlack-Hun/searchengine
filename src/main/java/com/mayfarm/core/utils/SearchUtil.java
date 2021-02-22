package com.mayfarm.core.utils;

public class SearchUtil {
	
	
	// MOIS 필드_검색
	public static String[] getSearchFieldMOIS(String field) {
		String[] fields = null;
		
		switch(field) {
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
	// LAW 필드_검색
	public static String[] getSearchFieldLAW(String field) {
		String[] fields = null;
		switch(field) {
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
	// NEWS 필드_검색
	public static String[] getSearchFieldNEWS(String field) {
		String[] fields = null;
		switch(field) {
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
		default:
			fields = new String[] {
					"title",
					"text",
					};
			break;
		}
		return fields;
	}
}
