/**
 * cookie.js
 */

var CookieUtils = function(){
	
	// 쿠키생성 (쿠키이름, 검색어, 유지기간)
	var setCookie = function(cookieName, value, day)  { 
		var date = new Date();
		date.setTime(date.getTime() + day * 60 * 60 * 24 * 1000);
		document.cookie = cookieName + '=' + value + ';expires=' + date.toUTCString() + ';path=/';
	}
	
	// 해당 이름의 쿠키를 가져옴 없으면 생성
	var getCookie = function(cookieName) {
		var value = document.cookie.match('(^|;) ?' + cookieName + '=([^;]*)(;|$)');
		if (value) { // 쿠키값이 존재한다면
			return value[2];	// 쿠기값 반환
		} else {
			setCookie(cookieName, encodeURIComponent('[]'), 1); // 쿠기가 없다면 1일짜리 쿠키 배열을 만들어서
		}
		return getCookie(cookieName);	// 다시 쿠키 생성
	} 
	
	// 해당 이름 쿠키 완전 삭제 (쿠키 지속시간을 현재 시간으로 바꿔버림)
	var delCookie = function(cookieName) { 
		var date = new Date();
		document.cookie = cookieName + "= " + "; expires=" + date.toUTCString() + "; path=/";
	}
	
	// 검색할때 쿠키를 add, (쿠키이름, 검색어, 배열 최대 사이즈)
	var addCookie = function(cookieName, value, cookieSize) {
		if (! value || value.trim() === "") return ;
		
		// 쿠키를 가져와
		let j = getCookie(cookieName);
		// 디코딩해서
		let obj = JSON.parse(decodeURIComponent(j));
		// 이미 일치하는 쿠키가 있다면
		if (obj.indexOf(value) !== -1) {
			// 해당 쿠키를 제거 (그래야 최신 검색으로 올라감) (이미 있을땐, 변화 하는게 싫으면 바로 return으로 수정)
			obj.splice(obj.indexOf(value), 1);
		}
		if (obj.length >= cookieSize) obj.splice(0, 1); // 사이즈가 넘어가면 0번 인덱스 삭제
		if (obj.indexOf(value) === -1 ) obj.push(value); // 일차하는게 없으면 obj에 푸쉬
		var ret = encodeURIComponent(JSON.stringify(obj)); // obj를 인코딩해서
		setCookie(cookieName, ret); // 쿠키 생성
	}
	return {
		setCookie : setCookie,
		getCookie : getCookie,
		delCookie : delCookie,
		addCookie : addCookie
	}
	
}();