package com.mayfarm.core.utils;

import java.util.List;

public class ArrayUtil {
	//리스트 사이즈 조절
	public static List arrayListCut (List list, int cutLength) {
		if(list != null && list.size() > cutLength) {
			List returnList = list.subList(0, cutLength);
			list = null;
			return returnList;
		}
		return list;
	}
}
