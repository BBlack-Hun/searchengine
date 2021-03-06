<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="utf-8">
	<title>수습과제::통합검색::전체</title>
	<link rel="stylesheet" type="text/css" href="resources/css/style.css">
	<link rel="stylesheet" type="text/css" href="resources/css/search.css">
	<script src="https://code.jquery.com/jquery-3.5.1.js" integrity="sha256-QWo7LDvxbWT2tbbQ97B53yJnYU3WhH/C8ycbRAkjPDc=" crossorigin="anonymous"></script>
</head>
<body>
	<div class="totalSearch">
		<div class="s-header"><!--s-header/s-->
			<div class="titleArea">
				<p class="title">
					통합검색
				</p>
			</div>
			<div class="searchArea">
				<div class="schBarWrap">
					<div class="schBar">
						<div class="iptWrap">
							<div class="iptBar">
								<script type="text/javascript">
									$(function() {
										/* input */
										$('.iptBar').each(function(index, item) {
											$(item).find('label').click(function() {
												$(this).next('input').focus();
											});
											$(item).find('input').keydown(function() {
												$(this).prev('label').hide();
											});
											$(item).find('input').blur(function() {
												if (!this.value) $(this).prev('label').show();
											});
										});
									});
								</script>
								<!--  검색라인 -->
								<input type="text" id="searchWord" name="search" class="ipt" placeholder="검색어를 입력해 주세요."
								value ="${index.str}" onkeypress="search_enter();">
								<div class="schArrow">
									<a href="#" title="검색창 위 화살표" class="selected"><img src="resources/img/sch_arrow_up.png" alt="검색창 위 화살표"></a>
									<a href="#" title="검색창 아래 화살표" class="unselected"><img src="resources/img/sch_arrow_down.png" alt="검색창 아래 화살표"></a>
								</div>
							</div>
						</div>
						<a href="javascript:void(0);" class="btnSch" onclick="search_btn();">검색</a>
					</div>
					<!-- 일반 검색 -->
					<form action="index" method="get" id='form_search'>
						<input type="hidden" name="search">
						<input type="hidden" name="osearch">
					</form>
					<!-- 상세 검색 -->
					<form action="index" method="get" id="form_search_option">
						<input type="hidden" name="search">
						<input type="hidden" name="osearch">
						<input type="hidden" name="category">
						<input type="hidden" name="page">
						<input type="hidden" name="perPageNum">
						<input type="hidden" name="sort">
						<input type="hidden" name="field">
					</form>
					<textarea id="paramVO_search" style="display: none;">${index.str}</textarea>
					<textarea id="paramVO_osearch" style="display: none;">${index.ostr}</textarea>
					<textarea id="paramVO_category" style="display: none;">${index.Category}</textarea>
					<textarea id="paramVO_page" style="display: none;">${index.paramVO.page}</textarea>
					<textarea id="paramVO_perPageNum" style="display: none;">${index.paramVO.listSize}</textarea>
					<textarea id="paramVO_sort" style="display: none;">${index.paramVO.sort}</textarea>
					<textarea id="paramVO_field" style="display: none;">${index.paramVO.field}</textarea>
				</div>
				<div class="schAuto">
					<ul class="schlist">
						<c:forEach items="${index.autoC}" var="AC">
							<li class="schitem"><a href="javascript:void(0);"><span class="blue_bold">${AC}</span></a></li>
						</c:forEach>
					</ul>
					<div class="autoBtm">
						<div class="word_all">
							<a href="javascript:void(0);" class="first_word">첫 단어 보기</a>
							<a href="javascript:void(0);" class="last_word">끝 단어 보기</a>
						</div>
						<a href="javascript:void(0);" class="btnClose" onclick="$(this).closest('.schAuto').hide();">자동완성 끄기</a>
					</div>
				</div>
				<div class="schCheckBox">				
					<span class="checkBtn">
						<input type="checkbox" id="schCheckBox" name="chkSearch" class="fCheck">
						<label for="schCheckBox">결과 내 재검색</label>
					</span>
				</div>
				<div class="schCheckArea">
					<span class="checkBtn">
						<input type="checkbox" id="schCheckBox2" name="chkSearch" class="fCheck">
						<label for="schCheckBox2">전체</label>
					</span>
					<span class="checkBtn">
						<input type="checkbox" id="schCheckBox3" name="chkSearch" class="fCheck">
						<label for="schCheckBox3">제목</label>
					</span>
					<span class="checkBtn">
						<input type="checkbox" id="schCheckBox4" name="chkSearch" class="fCheck">
						<label for="schCheckBox4">본문</label>
					</span>
					<span class="checkBtn">
						<input type="checkbox" id="schCheckBox5" name="chkSearch" class="fCheck">
						<label for="schCheckBox5">첨부파일명</label>
					</span>
					<span class="checkBtn">
						<input type="checkbox" id="schCheckBox6" name="chkSearch" class="fCheck">
						<label for="schCheckBox6">첨부파일 내용</label>
					</span>
				</div>
			</div>
			<script type="text/javascript">
				/* 포틀릿 width가 1150이하로 작아질 때 배경겹침 현상 제거 */
				$(document).ready(function(){
					$(window).resize(throttle(100, function(e) {
						resizeContents();
					}));
					resizeContents();
				});
				function resizeContents() {
					var searchBarW = $('.s-header').width();
					if(searchBarW >= 1150) {
						$('.s-header .titleArea').removeClass('min-width');
					} else {
						$('.s-header .titleArea').addClass('min-width');
					}
				}
				function throttle(ms, fn) {
					var allow = true;
					function enable() {
						allow = true;
					}
					return function(e) {
						if(allow) {
							allow = false;
							setTimeout(enable, ms);
							fn.call(this, e);
						}
					}
				}
			</script>
		</div><!--s-header/e-->
		<div class="s-container"><!--s-container/s-->
			<!--ct-left/s-->
			<div class="ct-left">
				<div class="lnbDiv">
					<ul class="lnbList">
						<li class="lnbItem"><!--add class:selected-->
							<a href="javascript:void(0)" class="Item" value="통합검색" onclick="search_Category(this);"><span class="title">전체</span><span class="num">${index.total}</span></a>
						</li>
						<li class="lnbItem">
							<a href="javascript:void(0);" class="Item" value="MOIS" onclick="search_Category(this);"><span class="title">정부기관</span><span class="num">${index.elastic.stotal.item0}</span></a>
							<ul class="sub_list">
								<li class="sub_item selected">
									<a href="javascript:void(0);" class="sub_info"><span class="title">행정자치부</span><span class="num">${index.elastic.stotal.item0}</span></a>
								</li>
							</ul>
						</li>
						<li class="lnbItem">
							<a href="javascript:void(0);" class="Item" value="LAW" onclick="search_Category(this);"><span class="title">국가법령/규칙</span><span class="num">${index.elastic.stotal.item1}</span></a>
							<ul class="sub_list">
								<li class="sub_item selected">
									<a href="javascript:void(0);" class="sub_info" ><span class="title">법령</span><span class="num">${index.elastic.stotal.item1}</span></a>
								</li>
							</ul>
						</li>
						<li class="lnbItem">
							<a href="javascript:void(0);" class="Item" value="NEWS" onclick="search_Category(this);"><span class="title">해외뉴스</span><span class="num">${index.elastic.stotal.item2}</span></a>
							<ul class="sub_list">
								<li class="sub_item selected">
									<a href="javascript:void(0);" class="sub_info"><span class="title">중국</span><span class="num">${index.elastic.stotal.item2}</span></a>
								</li>
							</ul>
						</li>
						<script type="text/javascript">//서브메뉴 슬라이드
						$(function(){
							$(".lnbDiv .lnbList .lnbItem .sub_list").slideUp(0);
							$(".lnbDiv .lnbList .lnbItem").click(function(){
								$(".lnbDiv .lnbList > li").removeClass('selected');
								$(this).addClass('selected');
							});
						});
						</script>
					</ul>
				</div>
				<div class="detailSearch">
					<div class="detailSearch_top">
						<p class="detail_title">상세검색</p>
						<p class="detail_info">여러 개의 단어를 입력할 때는 <span>쉼표(,)</span>로 구분해주세요</p>
					</div>
					<div class="detailSearch_middle">
						<ul class="fund_list">
							<li class="fund_item">
								<p class="fund_txt">정확히 일치하는 문장</p>
								<input class="funfd_btn" placeholder="남북협력기금">
							</li>
							<li class="fund_item">
								<p class="fund_txt">입력된 단어가 포함</p>
								<input class="funfd_btn" placeholder="남북협력기금">
							</li>
							<li class="fund_item">
								<p class="fund_txt">입력된 단어를 제회</p>
								<input class="funfd_btn" placeholder="남북협력기금">
							</li>
						</ul>
					</div>
					<div class="detailSearch_bottom">
						<div class="btn_wrap">
							<div class="refresh_wrap">
								<a href="javascript:void(0);" class="refresh"><span>초기화</span></a>
							</div>
							<div class="search_wrap">
								<a href="javascript:void(0);" class="search"><span>검색</span></a>
							</div>
						</div>
					</div>
				</div>
				<div class="periodDiv">
					<div class="titArea">
						<p class="tit">기간</p>
					</div>
					<div class="ctArea">
						<div class="data">
							<div class="set">
								<div class="barWrap">
									<div class="bar">
										<span class="handle" style="left:100%;">Handle</span><!--txt on된 영역의 % 입력-->
										<span class="bgSlt" style="width:100%;"></span><!--txt on된 영역의 % 입력-->
									</div>
								</div>
								<div class="txtWrap">
									<a class="txt" style="left:0%;">1주</a>
									<a class="txt on" style="left:33.3333%;">6개월</a><!--add class:on-->
									<a class="txt" style="left:66.6666%;">1년</a>
									<a class="txt" style="left:100%;">전체</a>
								</div>
							</div>
							<div class="period">
								<div class="iptWrap"><input type="text" readonly="" class="startDate" value="2020.08.21"></div>
								<span class="bar">~</span>
								<div class="iptWrap"><input type="text" readonly="" class="endDate" value="2020.09.21"></div>
							</div>
							<a href="#" class="btnSltDate">날짜적용</a>
						</div>
					</div>
				</div>
			</div><!--ct-left/e-->