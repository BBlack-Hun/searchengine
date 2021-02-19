/**
 * search.js
 */

// 검색어, 이전 검색어, 카테고리
var search, osearch, category;
// 페이지, 검색 목록 사이즈
var page, perPageNum
// 정렬, 검색 영역
var sort, field


// 초기값 세팅
var search = document.getElementById("paramVO_search").value;
var osearch = document.getElementById("paramVO_osearch").value;
var category = document.getElementById("paramVO_category").value;
var page = document.getElementById("paramVO_page").value;
var perPageNum = document.getElementById("paramVO_perPageNum").value;
var sort = document.getElementById("paramVO_sort").value;
var field = document.getElementById("paramVO_field").value;

// 엔터 검색
var search_enter = function(){
	if(event.keyCode == 13){
		search_btn();
	}
}

// 버튼 검색
var search_btn = function() {
	// 태그 네임이 saerch의 값을 가져온다.
	search = document.getElementById("searchWord").value;
	osearch = document.getElementById("paramVO_search").value;
	resetParam('search');
	
	if (category && category != '통합검색') {
		searchAll(true);
	} else {
		searchAll();
	}
		
}

//검색
var searchAll = function(obj){
	if (obj) {
		search_option();
	} else {
		search_default();
	}
}

// 일반 검색
var search_default = function(){
	var form = document.getElementById('form_search');
	// form으로 부터 해당하는 정보를 받아온다.
	form.querySelector('input[name=search]').value = search;
	form.querySelector('input[name=osearch]').value = osearch;
	// controlller로 제출
	form.submit();
}

// 상세 검색
var search_option = function() {
	var form = document.getElementById('form_search_option');
	// form으로 부터 해당하는 정보를 받아온다.
	form.querySelector('input[name=search]').value = search;
	form.querySelector('input[name=osearch]').value = osearch;
	form.querySelector('input[name=category]').value = category;
	form.querySelector('input[name=page]').value = page;
	form.querySelector('input[name=perPageNum]').value = perPageNum;
	form.querySelector('input[name=sort]').value = sort;
	form.querySelector('input[name=field]').value = field;
	form.submit();
}

// 정렬 변경
var sort_btn = function(btn) {
	page = 1;
	sort = btn.getAttribute('value');
	alert(sort);
	
	switch(sort) {
		case '최신순':
			searchAll(true);
			break;
		default:
			searchAll(true);
	}
}

// 더보기 또는 카테고리를 눌렀을때, 각 카테고리 별 리스트로 이동
var search_Category = function(btn) {
	resetParam('search_category');
	category = btn.getAttribute('value');
	searchAll(true)
}

// 페이지 번호 클릭
var ClickPagiNation = function() {
	var pagination = document.getElementsByClassName("pglist");
	if (pagination) {
		for (var i =0; i < pagination.length; i++) {
			pagination[i].addEventListener('click', function(){
				page = $(this).attr("move_pg");
				searchAll(true);	
			}, false);
			
		}
	}
}
// 파라미터 초기화
var resetParam = function(where) {
	switch(where) {
	case 'search':
		page = 1;
		sort = '정확도순';
		break;
	case 'search_category':
		page = 1;
		perPageNum = 10;
		sort = '정확도순';
		field = "전체";
		break;
	case 'search_option':
		page = 1;
		perPageNum = 10;
		field = "전체";
		break;
	case 'default':
		page = 1;
		perPageNum = 1;
		sort = '정확도순';
		field = "전체";
		break;
	}
}