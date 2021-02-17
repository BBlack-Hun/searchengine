package com.mayfarm.Search.vo;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;

@Data
public class ParamVO {
	
	// 문서 검색시 사용
	private String search = "";
	private String osearch = "";
	private String category = "통합검색";
	private Integer page = 1;
	private Integer listSize = 4;
	
	// IOSTUDIO
	// 아래 index의 역할을 아직 잘 모르겠다.
	/* private Set<String> index = new HashSet<>(); */
}
