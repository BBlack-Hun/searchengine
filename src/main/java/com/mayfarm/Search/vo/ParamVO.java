package com.mayfarm.Search.vo;

import lombok.Data;

@Data
public class ParamVO {
	
	// 문서 검색시 사용
	private String search = "";
	private String osearch = "";
	private String category = "통합검색";
	private Integer page = 1;
	private Integer listSize = 4;
}
