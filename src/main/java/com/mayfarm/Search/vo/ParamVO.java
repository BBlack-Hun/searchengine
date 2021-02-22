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
	private String sort = "정확도순";
	private String startDate;
	private String endDate;
	private String exactSearch = "";	// 정확하게 일치
	private String includeSearch = "";	// 포함
	private String excludeSearch = "";	// 제외
	private Boolean re = false;
	
	private Integer page = 1;
	private Integer listSize = 4;
	private float max = 100;			// 기간bar 수정을 위한 변수
	private String reSearch = "";
	private String reexactSearch = "";
	private String reincludeSearch = "";
	private String reexcludeSearch = "";
	private String field = "전체";
	
	// IOSTUDIO
	// 아래 index의 역할을 아직 잘 모르겠다.
	/* private Set<String> index = new HashSet<>(); */
}
