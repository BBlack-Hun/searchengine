<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<html>
<head>
	<script src="https://code.jquery.com/jquery-3.5.1.js" integrity="sha256-QWo7LDvxbWT2tbbQ97B53yJnYU3WhH/C8ycbRAkjPDc=" crossorigin="anonymous"></script>
	<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js" integrity="sha384-B4gt1jrGC7Jh4AgTPSdUtOBvfO8shuf57BaghqFfPlYxofvL8/KUEfYiJOMMV+rV" crossorigin="anonymous"></script>	 
	<title>Home</title>
</head>
<body>
	<div class="boxContainer">
		<table class="elementsContainer">
			<tr>
				<td>
					<input type="text"  id="searchWord" name="search" placeholder="검색할 내용을 입력해주세요!" class="serach" onkeypress="search_enter();">
				</td>
				<td>
					<a href='javascript:void(0);' onclick="search_btn();"><span class="material-icons">검색</span>
					</a>
				</td>
			</tr>
		</table>
		<form action="index" method="get" id="form_search">
			<input type="hidden" name="search">
		</form>
		<textarea id="paramVO_search" style="display:none;">${paramVO.search}</textarea>
	</div>
	<!-- 스크립트 영역 -->
	<script src="resources/js/Fsearch.js" ></script>
</body>
</html>
