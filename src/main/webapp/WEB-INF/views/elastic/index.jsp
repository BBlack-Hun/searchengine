<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
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
						<input type="hidden" name="sort">
						<input type="hidden" name="startDate">
						<input type="hidden" name="endDate">
						<input type="hidden" name="exactSearch">
						<input type="hidden" name="includeSearch">
						<input type="hidden" name="excludeSearch">
						<input type="hidden" name="page">
						<input type="hidden" name="perPageNum">
						<input type="hidden" name="re">
						<input type="hidden" name="field">
						<input type="hidden" name="reSearch">
						<input type="hidden" name="reexactSearch">
						<input type="hidden" name="reincludeSearch">
						<input type="hidden" name="reexcludeSearch">
						<input type="hidden" name="max">
					</form>
					<textarea id="paramVO_search" style="display: none;">${index.str}</textarea>
					<textarea id="paramVO_osearch" style="display: none;">${index.ostr}</textarea>
					<textarea id="paramVO_category" style="display: none;">${index.Category}</textarea>
					<textarea id="paramVO_sort" style="display: none;">${index.paramVO.sort}</textarea>
					<textarea id="paramVO_startDate" style="display: none;">${index.paramVO.startDate}</textarea>
					<textarea id="paramVO_endDate" style="display: none;">${index.paramVO.endDate}</textarea>
					<textarea id="paramVO_exactSearch" style="display: none;">${index.paramVO.exactSearch}</textarea>
					<textarea id="paramVO_includeSearch" style="display: none;">${index.paramVO.includeSearch}</textarea>
					<textarea id="paramVO_excludeSearch" style="display: none;">${index.paramVO.excludeSearch}</textarea>
					<textarea id="paramVO_page" style="display: none;">${index.paramVO.page}</textarea>
					<textarea id="paramVO_perPageNum" style="display: none;">${index.paramVO.listSize}</textarea>
					<textarea id="paramVO_re" style="display: none;">${index.paramVO.re}</textarea>
					<textarea id="paramVO_field" style="display: none;">${index.paramVO.field}</textarea>
					<textarea id="paramVO_max" style="display: none;">${index.paramVO.max}</textarea>
					<textarea id="paramVO_reSearch" style="display: none;">${index.paramVO.reSearch}</textarea>
					<textarea id="paramVO_reexactSearch" style="display: none;">${index.paramVO.reexactSearch}</textarea>
					<textarea id="paramVO_reincludeSearch" style="display: none;">${index.paramVO.reincludeSearch}</textarea>
					<textarea id="paramVO_reexcludeSearch" style="display: none;">${index.paramVO.reexcludeSearch}</textarea>
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
						<input type="checkbox" id="re" name="re" value class="fCheck" <c:if test="${index.paramVO.re eq true }">checked </c:if>>
						<label for="re">결과 내 재검색</label>
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
								<p class="fund_txt">정확히 일치</p>
								<input class="funfd_btn" id = "exactSearch" placeholder="정확하게 일치하는 단어/문장">
							</li>
							<li class="fund_item">
								<p class="fund_txt">포함하는 단어</p>
								<input class="funfd_btn" id = "includeSearch" placeholder="포함하는 단어를 입력하세요.">
							</li>
							<li class="fund_item">
								<p class="fund_txt">제외하는 단어</p>
								<input class="funfd_btn" id = "excludeSearch" placeholder="제외하는 단어를 입력하세요.">
							</li>
						</ul>
					</div>
					<div class="detailSearch_bottom">
						<div class="btn_wrap">
							<div class="refresh_wrap">
								<a href="javascript:void(0);" class="refresh" cat="통합 검색" onclick="initValDetailFilter(this);"><span>초기화</span></a>
							</div>
							<div class="search_wrap">
								<a href="javascript:void(0);" class="search" onclick="search_btn_detail();"><span>검색</span></a>
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
										<span class="handle" style="left:${index.paramVO.max}%;">Handle</span><!--txt on된 영역의 % 입력-->
										<span class="bgSlt" style="width:${index.paramVO.max}%;"></span><!--txt on된 영역의 % 입력-->
									</div>
								</div>
								<div class="txtWrap">
									<a class="txt" style="left:0%;" onclick="date_btn(this);">1주</a>
									<a class="txt" style="left:33.3333%;" onclick="date_btn(this);">6개월</a><!--add class:on-->
									<a class="txt" style="left:66.6666%;" onclick="date_btn(this);">1년</a>
									<a class="txt" style="left:100%;" onclick="date_btn(this);">전체</a>
								</div>
							</div>
							<div class="period">
								<div class="iptWrap"><input type="text" id="startDate" class="startDate" placeholder="YYYY.MM.DD" value="${index.paramVO.startDate}"></div>
								<span class="bar">~</span>
								<div class="iptWrap"><input type="text" id="endDate" class="endDate" placeholder="YYYY.MM.DD" value="${index.paramVO.endDate}"></div>
							</div>
							<a href="javascript:void(0);" class="btnSltDate" onclick="date_btn(this);">날짜적용</a>
						</div>
					</div>
				</div>
			</div><!--ct-left/e-->
			<!-- 헤더로 분리할 부분 --><!-- 헤더로 분리할 부분 --><!-- 헤더로 분리할 부분 --><!-- 헤더로 분리할 부분 --><!-- 헤더로 분리할 부분 --><!-- 헤더로 분리할 부분 -->
			<!--ct-center/s-->
			<div class="ct-center">
				<!--resultTopDiv/s-->
				<div class="resultTopDiv">
					<p class="result"><b>'${index.str}<c:forEach items="${index.paramVO.reSearch}" var="reSch"><c:if test="${index.paramVO.re eq true }">+${reSch}</c:if>'</c:forEach></b>에 대하여 총 <b>${index.total}</b>건이 검색되었습니다.</p>
					<div class="viewTab">
						<script type="text/javascript">
							$(function() {
								$('.viewTab li a').click(function() {
									$('.viewTab li').removeClass('selected');
									$(this).parents('li').addClass('selected');
								});
							});
						</script>
						<ul>
							<li <c:if test="${index.paramVO.sort eq '정확도순' }">class="selected"</c:if>><a href="javascript:void(0);" value="정확도순" class="sort_btn" onclick="sort_btn(this);">정확도순</a></li><!--add class:selected-->
							<li <c:if test="${index.paramVO.sort eq '최신순'}" >class="selected"</c:if>><a href="javascript:void(0);" value="최신순" class="sort_btn" onclick="sort_btn(this);">최신순</a></li>
						</ul>
					</div>
				</div><!--resultTopDiv/e-->
				<!--portalDiv/s-->
				<c:if test="${index.elastic.stotal.item0 >= 1}">
				<div class="portalDiv">
					<p class="portalTit">행정자치부<b>${index.elastic.stotal.item0}</b>건</p>
					<div class="portalCt">
						<ul class="list">
							<c:forEach items="${index.elastic.MOIS}" var="MOIS">
								<li class="listItem">
									
									<div class="titAreaWrap">
										<div class="titArea">
											<a href="javascript:void(0);" class="tit">${MOIS.title}</a>
										</div>
									</div>
									<div class="infoArea">
										<span>공개</span><span class="bar">|</span><span>${MOIS.writer}</span><span class="bar">|</span><span>${MOIS.regdt}</span><span class="bar">|</span><span>${MOIS.stitle }</span>
									</div>
									<div class="contentArea">
										${MOIS.content }                          
									</div>
									<div class="fileArea">
										<div class="file">
											<img src="resources/img/ico/file/hwp.gif" class="fileIco">
											<a href="${MOIS.link}" class="fileLink">${MOIS.linkname}</a>
											<a href="javascript:void(0);" class="btnFileView">미리보기</a>
										</div>
										<%-- <div class="FileView_mordal">
											<div class="FileView_top">
												<p class="view_title">미리보기</p>
												<a href="javascript:void(0);" class="close_btn"></a>
											</div>
											<div class="FileView_bottom">
												<p class="view_info">
													${MOIS.content }
												</p>
											</div>
										</div> --%>
									</div>
									<script type="text/javascript"> //미리보기 modal창 fadeOut
										$(function(){
											$(".FileView_mordal").fadeOut(0);
										});
									</script>
								</li>			
							</c:forEach>
						</ul>
					</div>
					<c:if test="${index.elastic.stotal.item0 > 4 }">
						<a href="javascript:void(0);" class="btnMore" value="MOIS" onclick="search_Category(this)">더보기</a>
					</c:if>
				</div><!--resultDiv/e-->
				</c:if>
				<!--resultDiv/s-->
				<c:if test="${index.elastic.stotal.item1 >= 1 }">
				<div class="resultDiv">
					<p class="resultTit">법령<b>${index.elastic.stotal.item1}</b>건</p>
					<div class="resultCt">
						<ul class="list">
							<c:forEach items="${index.elastic.LAW}" var="LAW">
								<li class="listItem">
									<div class="titAreaWrap">
										<div class="titArea">
											<a href="javascript:void(0);" class="tit">${LAW.title}</a>
										</div>
									</div>
									<div class="infoArea">
										<span>공개</span><span class="bar">|</span><span>${LAW.froms}</span><span class="bar">|</span><span>${LAW.date}</span><span class="bar">|</span><span>${LAW.stitle }</span>
									</div>
									<div class="contentArea">
										${LAW.content }                          
									</div>
									<div class="fileArea">
										<div class="file">
											<img src="resources/img/ico/file/hwp.gif" class="fileIco">
											<a href="${LAW.link}" class="fileLink">${LAW.linkname}.hwp</a>
											<a href="javascript:void(0);" class="btnFileView">미리보기</a>
										</div>
										<!-- <div class="FileView_mordal">
											<div class="FileView_top">
												<p class="view_title">미리보기</p>
												<a href="javascript:void(0);" class="close_btn"></a>
											</div>
											<div class="FileView_bottom">
												<p class="view_info">
													제1조(목적) 이 영은 「10ㆍ27법난 피해자의 명예회복 등에 관한 법률」에서 위임된 사항과 그 시행에 필요한 사항을 규정함을 목적으로 한다.
													제2조(10ㆍ27법난피해자명예회복심의위원회의 구성 및 운영) ① 「10ㆍ27법난 피해자의 명예회복 등에 관한 법률」(이하 “법”이라 한다) 
													제3조에 따른 10ㆍ27법난피해자명예회복심의위원회(이하 “위원회”라 한다)는 문화체육관광부장관이 임명하는 제1호부터 제3호까지의 위원과 문화체육관광부장관이 성별을 고려하여 위촉하는 7명 이내의 제4호의 위원으로 구성한다. 
													<개정 2011. 11. 11., 2013. 3. 23., 2015. 1. 6., 2016. 6. 8.>
												</p>
											</div>
										</div> -->
									</div>
									<script type="text/javascript"> //미리보기 modal창 fadeOut
										$(function(){
											$(".FileView_mordal").fadeOut(0);
										});
									</script>
								</li>			
							</c:forEach>
						</ul>
					</div>
					<c:if test="${index.elastic.stotal.item1 > 4}">
						<a href="javascript:void(0);" class="btnMore" value="LAW" onclick="search_Category(this)">더보기</a>
					</c:if>
				</div>
				</c:if>
				<!--resultDiv/e-->
				<!--resultDiv/s-->
				<c:if test="${index.elastic.stotal.item2 >= 1}">
				<div class="resultDiv">
					<p class="resultTit">중국<b>${index.elastic.stotal.item2}</b>건</p>
					<div class="resultCt">
						<ul class="list">
							<c:forEach items="${index.elastic.NEWS}" var="NEWS">
								<li class="listItem">
									<div class="titAreaWrap">
										<div class="titArea">
											<a href="javascript:void(0);" class="tit">${NEWS.title}</a>
										</div>
									</div>
									<div class="infoArea">
										<span>공개</span><span class="bar">|</span><span>${NEWS.writer}</span><span class="bar">|</span><span>${NEWS.date}</span><span class="bar">|</span><span>${NEWS.stitle }</span>
									</div>
									<div class="contentArea">
										${NEWS.text }                          
									</div>
								</li>			
							</c:forEach>
						</ul>
					</div>
					<c:if test="${index.elastic.stotal.item2 > 4}">
						<a href="javascript:void(0);" class="btnMore" value="NEWS" onclick="search_Category(this)">더보기</a>
					</c:if>
				</div>
				</c:if>
				<!--resultDiv/e-->
			</div><!--ct-center/e-->
			<!-- footer로 분리할 부분 --><!-- footer로 분리할 부분 --><!-- footer로 분리할 부분 --><!-- footer로 분리할 부분 --><!-- footer로 분리할 부분 --><!-- footer로 분리할 부분 -->
			<!--ct-right/s-->
			<div class="ct-right">
				<div class="hitDiv">
					<div class="titArea">
						<p class="tit">인기 검색어</p>
					</div>
					<div class="ctArea">
						<div class="data">
							<ul class="hitList">							
								<c:forEach items="${index.searchResult}" begin="0" end="4" var="SR" varStatus="status">
									<c:forEach items="${SR}" var="SRR">
										<c:if test="${status.count <= 3}">
											<li class="hitItem top3"><!--add class:top3-->
												<span class="i">${status.count}</span>
													<a href="javascript:void(0);" title="${SRR.getKey()}" onclick="search_btn_click(this)">${SRR.getKey()}</a>
													<span style="display:none;">${SRR.getKey()}</span>
<!-- 												<span class="rank new">new</span>add class:up -->
													<c:if test="${SRR.getValue() < 0 }">
														<span class="rank down">${SRR.getValue()}</span><!--add class:up-->
													</c:if>
													<c:if test="${SRR.getValue() > 0 }">
														<span class="rank up">${SRR.getValue()}</span><!--add class:up-->
													</c:if>
													<c:if test="${SRR.getValue() == 999 }">
														<span class="rank new">${SRR.getValue()}</span><!--add class:up-->
													</c:if>
													<c:if test="${SRR.getValue() == 0 }">
														<span class="rank">-</span><!--add class:up-->
													</c:if>
											</li>
										</c:if>
										<c:if test="${status.count > 3}">
											<li class="hitItem"><!--add class:top3-->
												<span class="i">${status.count}</span>
													<a href="javascript:void(0);" title="${SRR.getKey()}" onclick="search_btn_click(this);">${SRR.getKey()}</a>
													<span style="display:none;">${SRR.getKey()}</span>
<!-- 												<span class="rank new">new</span>add class:up -->
												<c:if test="${SRR.getValue() < 0 }">
													<span class="rank down">${SRR.getValue()}</span><!--add class:up-->
												</c:if>
												<c:if test="${SRR.getValue() > 0 }">
													<span class="rank up">${SRR.getValue()}</span><!--add class:up-->
												</c:if>
												<c:if test="${SRR.getValue() == 999 }">
													<span class="rank new">new</span><!--add class:up-->
												</c:if>
												<c:if test="${SRR.getValue() == 0 }">
													<span class="rank">-</span><!--add class:up-->
												</c:if>
											</li>
										</c:if>
									</c:forEach>
								</c:forEach>
							</ul>
						</div>
					</div>
				</div>
				<div class="historyDiv"><!--RIGHT > historyDiv/s-->
					<div class="titArea">
						<p class="tit">연관 검색어</p>
					</div>
					<div class="ctArea">
						<div class="data">
							<ul class="historyList">
								<c:forEach items="${index.elastic.autoRecommList }" var="autoR">
									<li class="historyItem"> <!-- select라는 옵션 추가 가능! -->
										<span class="item">
											<a href="javascript:void(0);" class="txt" title="${autoR}" onclick="search_btn_click(this)">${autoR}</a>
											<span style="display:none;">${autoR}</span>
										</span>
									</li>
								</c:forEach>
							</ul>
						</div>
					</div>
				</div>
			</div><!--ct-right/e-->
		</div><!--s-container/e-->
	</div>
	<!-- 스크립트 영역 -->
	<script src="resources/js/Msearch.js" ></script>
</body>
</html>
