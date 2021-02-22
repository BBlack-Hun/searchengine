package com.mayfarm.core.utils;

public class RefineUtil {
	
	// 자바 이스케이프
	public static String escapeString (String str) {
		return str.replaceAll("[-\\[\\]{}()*+?.,\\\\\\\\^$|#\\\\s]","\\\\$0");
	}
}
