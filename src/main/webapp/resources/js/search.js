/**
 * search.js
 */

// 검색어, 이전 검색어, 카테고리
var search, osearch, category;
// 페이지, 검색 목록 사이즈
var page, perPageNum;
// 정렬, 시작날짜, 종료날짜, 검색 영역
var sort, startDate, endDate, field;
// 기간
var max;


// 초기값 세팅
var search = document.getElementById("paramVO_search").value;
var osearch = document.getElementById("paramVO_osearch").value;
var category = document.getElementById("paramVO_category").value;
var startDate = document.getElementById("paramVO_startDate").value;
var endDate = document.getElementById("paramVO_endDate").value;
var sort = document.getElementById("paramVO_sort").value;
var page = document.getElementById("paramVO_page").value;
var perPageNum = document.getElementById("paramVO_perPageNum").value;
var field = document.getElementById("paramVO_field").value;
var max = document.getElementById("paramVO_max").value;

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
// 인기, 연관, 내가 검색한 검색어에서 검색하기
var search_btn_click = function(e) {
	search = e.nextElementSibling.textContent;
	osearch = null;
	searchAll();
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
	form.querySelector('input[name=startDate]').value = startDate;
	form.querySelector('input[name=endDate]').value = endDate;
	form.querySelector('input[name=sort]').value = sort;
	form.querySelector('input[name=page]').value = page;
	form.querySelector('input[name=perPageNum]').value = perPageNum;
	form.querySelector('input[name=field]').value = field;
	form.querySelector('input[name=max]').value = max;
	form.submit();
}

// 더보기 또는 카테고리를 눌렀을때, 각 카테고리 별 리스트로 이동
var search_Category = function(btn) {
	resetParam('search_category');
	category = btn.getAttribute('value');
	searchAll(true)
}

// 정렬 변경
var sort_btn = function(btn) {
	page = 1;
	sort = btn.getAttribute('value');
	
	switch(sort) {
		case '최신순':
			searchAll(true);
			break;
		default:
			searchAll(true);
	}
}

// 영역 클릭

// 기간 클릭 및 적용
var date_btn = function(btn) {
	var range = btn.innerText;
	
	if (range === "날짜적용") {
		var tempStartDate = document.getElementById("startDate").value;
		var tempEndDate = document.getElementById('endDate').value;
		var is = validationDate(tempStartDate, tempEndDate);
		if(!is) {
			return false;
		} else {
			page = 1;
			startDate = tempStartDate;
			endDate = tempEndDate;
			searchAll(true);
		}
		
	}
	
	page = 1;
	endDate = getToday();
	switch (range) {
		case "전체":
			startDate = null;
			endDate = null;
			max = 100;
			break;
		case "1주":
			startDate = dateCalculator(endDate, -7, 'd');
			max = 0;
			break;
		case "6개월":
			startDate = dateCalculator(endDate, -6, 'm');
			max = 33.3333;
			break;
		case "1년":
			startDate = dateCalculator(endDate, -1, 'y');
			max = 66.6666;
			break;
	}
	searchAll(true);
}

var click_date = function(e) {
	
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
		startDate = null;
		endDate = null;
		field = "전체";
		break;
	}
}
/**
 * 날짜 관련 유틸
 */
// 날짜 계산기
function dateCalculator(sDate, nNum, type) {
	// 문자열 추출하는 메서드 substr
	var yy = parseInt(sDate.substr(0, 4), 10);
	var mm = parseInt(sDate.substr(5, 2), 10);
	var dd = parseInt(sDate.substr(8), 10);
	
	// type을 검사하여 해당하는 내용 실행
	if (type == 'd') {
		d = new Date(yy, mm - 1, dd + nNum);
	} else if (type == 'm') {
		d = new Date(yy, (mm - 1) + (nNum), dd);
	} else if (type == 'y') {
		d = new Date(yy + nNum, mm - 1, dd);
	}
	
	yy = d.getFullYear();
	mm = d.getMonth() + 1;
	// 10보다 작을 경우 앞에 0을 붙여줌
	mm = (mm < 10) ? "0" + mm : mm;
	dd = d.getDate();
	dd = (dd < 10) ? "0" + dd : dd;
	
	return '' + yy + '-' + mm + '-' + dd;
}

// 오늘 날짜 구하기
function getToday() {
	var date = new Date();
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	var day = date.getDate();
	if (month < 10) {
		month = "0" + month;
	}
	if (day < 10) {
		day = "0" + day;
	}
	return year + "-" + month + "-" + day;
}

// 시작 날짜와 끝 날짜 비교
function validationDate(start, end) {
	if(!start) {
		alert("시작일을 입력해 주세요!");
		return false;
	} else if (!end) {
		alert("종료일을 선택해 주세요.");
		return false;
	}
	var today = parseInt(inputDateSplit(getToday()));
	var startDate = parseInt(inputDateSplit(start));
	var endDate = parseInt(inputDateSplit(end));
	
	if (startDate > today || endDate > today) {
		alert("미래 날짜는 지정 할 수 없습니다.");
		return false;
	} else if (startDate > endDate) {
		alert("시작일이 종료일보다 이 후 일수는 없습니다.");
		return false;
	} else {
		return true;
	}
}

var inputDateSplit = function(obj) {
	var dateArray = obj.split("-");
	return dateArray[0] + dateArray[1] + dateArray[2];
}/**
 * 
 */