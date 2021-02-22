/**
 * 첫 페이지에서 검색구현
 * Fsearch.js
 */

// 현재 검색어
var search

// 초기값 세팅
var search = document.getElementById("paramVO_search").value;

// 엔터 검색
var search_enter = function(){
	if(event.keyCode == 13){
		search_btn();
	}
}

// 버튼 검색
var search_btn = function() {
	// 태그 네임의 search의 값을 가져온다.
	search = document.getElementById("searchWord").value;
	
	searchAll();
}

// 검색

var searchAll = function() {
	var form = document.getElementById('form_search');
	// form으로 부터 해당하는 정보를 받아온다.
	form.querySelector('input[name=search]').value = search;
	// controlller로 제출
	form.submit();
}