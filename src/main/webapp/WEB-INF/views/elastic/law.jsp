<!doctype html>
<html lang="ko">
<head>
	<meta charset="utf-8">
	<title>통일부::통합검색::전체</title>
	<link href="../style.css" rel="stylesheet">
	<link href="search.css" rel="stylesheet">
	<script type="text/javascript" src="../jquery-1.11.0.min.js"></script>
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
							<label for="searchWord" class="lbl">검색어를 입력해 주세요.</label>
							<input type="text" id="searchWord" class="ipt">
							<div class="schArrow">
								<a href="#" title="검색창 위 화살표" class="selected"><img src="images/sch_arrow_up.png" alt="검색창 위 화살표"></a>
								<a href="#" title="검색창 아래 화살표" class="unselected"><img src="images/sch_arrow_down.png" alt="검색창 아래 화살표"></a>
							</div>
						</div>
					</div>
					<a href="#" class="btnSch">검색</a>
				</div>
			</div>
			<div class="schAuto">
				<ul class="schlist">
					<li class="schitem"><a href="javascript:void(0);"><span class="blue_bold">남북협력기금</span></a></li>
					<li class="schitem"><a href="javascript:void(0);"><span class="blue_bold">남북협력기금</span> 운용</a></li>
					<li class="schitem"><a href="javascript:void(0);"><span class="blue_bold">남북협력기금</span> 운용관리</a></li>
					<li class="schitem"><a href="javascript:void(0);"><span class="blue_bold">남북협력기금</span> 운용관리규정</a></li>
					<li class="schitem"><a href="javascript:void(0);"><span class="blue_bold">남북협력기금</span> 지원</a></li>
					<li class="schitem"><a href="javascript:void(0);"><span class="blue_bold">남북협력기금</span> 내역</a></li>
					<li class="schitem"><a href="javascript:void(0);"><span class="blue_bold">남북협력기금</span></a></li>
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
						<a href="#" class="Item"><span class="title">전체</span><span class="num">122</span></a>
					</li>
					<li class="lnbItem">
						<a href="#" class="Item"><span class="title">통일행정포털</span><span class="num">8</span></a>
						<ul class="sub_list">
							<li class="sub_item selected">
								<a href="#" class="sub_info"><span class="title">게시판</span><span class="num">4</span></a>
							</li>
							<li class="sub_item">
								<a href="#" class="sub_info"><span class="title">언론브리핑</span><span class="num">11</span></a>
							</li>
						</ul>
					</li>
					<li class="lnbItem">
						<a href="#" class="Item"><span class="title">지식 관리 시스템</span><span class="num">90</span></a>
						<ul class="sub_list">
							<li class="sub_item selected">
								<a href="#" class="sub_info"><span class="title">지식관리</span><span class="num">4</span></a>
							</li>
							<li class="sub_item ">
								<a href="#" class="sub_info"><span class="title">업무관리</span><span class="num">11</span></a>
							</li>
							<li class="sub_item">
								<a href="#" class="sub_info"><span class="title">혁신관리</span><span class="num">10</span></a>
							</li>
							<li class="sub_item">
								<a href="#" class="sub_info"><span class="title">자원관리</span><span class="num">10</span></a>
							</li>
							<li class="sub_item">
								<a href="#" class="sub_info"><span class="title">소통관리</span><span class="num">9</span></a>
							</li>
						</ul>
					</li>
					<li class="lnbItem">
						<a href="#" class="Item"><span class="title">온나라</span><span class="num">8</span></a>
						<ul class="sub_list">
							<li class="sub_item selected">
								<a href="#" class="sub_info"><span class="title">문서</span><span class="num">4</span></a>
							</li>
							<li class="sub_item">
								<a href="#" class="sub_info"><span class="title">지식</span><span class="num">11</span></a>
							</li>
							<li class="sub_item">
								<a href="#" class="sub_info"><span class="title">이음</span><span class="num">10</span></a>
							</li>
						</ul>
					</li>
					<li class="lnbItem">
						<a href="#" class="Item"><span class="title">국가법령정보센터</span><span class="num">21</span></a>
						<ul class="sub_list">
							<li class="sub_item selected">
								<a href="#" class="sub_info"><span class="title">법령</span><span class="num">4</span></a>
							</li>
							<li class="sub_item">
								<a href="#" class="sub_info"><span class="title">자치법규</span><span class="num">11</span></a>
							</li>
							<li class="sub_item">
								<a href="#" class="sub_info"><span class="title">행정규칙</span><span class="num">10</span></a>
							</li>
							<li class="sub_item">
								<a href="#" class="sub_info"><span class="title">판례·해석례등</span><span class="num">10</span></a>
							</li>
							<li class="sub_item">
								<a href="#" class="sub_info"><span class="title">별표·서식</span><span class="num">10</span></a>
							</li>
							<li class="sub_item">
								<a href="#" class="sub_info"><span class="title">학칙·규정</span><span class="num">10</span></a>
							</li>
							<li class="sub_item">
								<a href="#" class="sub_info"><span class="title">그밖의 정보</span><span class="num">10</span></a>
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
		<!--ct-center/s-->
		<div class="ct-center">
			<!--resultTopDiv/s-->
			<div class="resultTopDiv">
				<p class="result"><b>'남북협력기금'</b>에 대하여 총 <b>1,822</b>건이 검색되었습니다.</p>
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
						<li class="selected"><a href="javascript:void(0);">정확도순</a></li><!--add class:selected-->
						<li><a href="javascript:void(0);">최신순</a></li>
					</ul>
				</div>
			</div><!--resultTopDiv/e-->
			<!--portalDiv/s-->
			<div class="portalDiv">
				<p class="portalTit">통일행정포털<b>122</b>건</p>
				<div class="portalCt">
					<ul class="list">
						<li class="listItem">
							<div class="titAreaWrap">
								<div class="titArea">
									<a href="javascript:void(0);" class="tit">200906_(보도자료) 제316차 <b>남북</b>교류<b>협력</b> 추진협의, 세계식량계획(WFP) ‘영유아, 여성 지원사업＇ 등에 기금 지원</a>
								</div>
							</div>
							<div class="infoArea">
								<span>공개</span><span class="bar">|</span><span>이수현 (공보담당관)</span><span class="bar">|</span><span>2020.08.06</span><span class="bar">|</span><span>위치:통일행정포털>게시판>언론브리핑포도자료</span>
							</div>
							<div class="contentArea">
								한반도 평화와 변영을 위한 <b>남북협력기금</b>의 활용방안 연구, 통일교육원 훈련원, 비핵형화시대를 견일할 남북사회문화교륙 협력 추진 결과를 보과합니다.
								한반도 평화와 변영을 위한 <b>남북협력기금</b>의 활용방안 연구, 통일교육원 훈련원, 비핵형화시대를 견일할 남북사회문화교륙 협력 추진 결과를 보과합니다.                          
							</div>
						</li>
						<li class="listItem">
							<div class="titAreaWrap">
								<div class="titArea">
									<a href="javascript:void(0);" class="tit">200906_(보도자료) 제316차 <b>남북</b>교류<b>협력</b> 추진협의, 세계식량계획(WFP) ‘영유아, 여성 지원사업＇ 등에 기금 지원</a>
								</div>
							</div>
							<div class="infoArea">
								<span>공개</span><span class="bar">|</span><span>이수현 (공보담당관)</span><span class="bar">|</span><span>2020.08.06</span><span class="bar">|</span><span>위치:통일행정포털>게시판>언론브리핑포도자료</span>
							</div>
							<div class="contentArea">
								한반도 평화와 변영을 위한 <b>남북협력기금</b>의 활용방안 연구, 통일교육원 훈련원, 비핵형화시대를 견일할 남북사회문화교륙 협력 추진 결과를 보과합니다.
								한반도 평화와 변영을 위한 <b>남북협력기금</b>의 활용방안 연구, 통일교육원 훈련원, 비핵형화시대를 견일할 남북사회문화교륙 협력 추진 결과를 보과합니다.                          
							</div>
						</li>
						<li class="listItem">
							<div class="titAreaWrap">
								<div class="titArea">
									<a href="javascript:void(0);" class="tit">200906_(보도자료) 제316차 <b>남북</b>교류<b>협력</b> 추진협의, 세계식량계획(WFP) ‘영유아, 여성 지원사업＇ 등에 기금 지원</a>
								</div>
							</div>
							<div class="infoArea">
								<span>공개</span><span class="bar">|</span><span>이수현 (공보담당관)</span><span class="bar">|</span><span>2020.08.06</span><span class="bar">|</span><span>위치:통일행정포털>게시판>언론브리핑포도자료</span>
							</div>
							<div class="contentArea">
								한반도 평화와 변영을 위한 <b>남북협력기금</b>의 활용방안 연구, 통일교육원 훈련원, 비핵형화시대를 견일할 남북사회문화교륙 협력 추진 결과를 보과합니다.
								한반도 평화와 변영을 위한 <b>남북협력기금</b>의 활용방안 연구, 통일교육원 훈련원, 비핵형화시대를 견일할 남북사회문화교륙 협력 추진 결과를 보과합니다.                          
							</div>
						</li>
					</ul>
				</div>
				<a href="javascript:void(0);" class="btnMore">더보기</a>
			</div><!--resultDiv/e-->
			<!--resultDiv/s-->
			<div class="resultDiv">
				<p class="resultTit">지식관리<b>90</b>건</p>
				<div class="resultCt">
					<ul class="list">
						<li class="listItem">
							<div class="titAreaWrap">
								<div class="titArea">
									<a href="javascript:void(0);" class="tit">2019년 국과장급 국내장기훈련 결과보고서</a>
								</div>
							</div>
							<div class="infoArea">
								<span>공개</span><span class="bar">|</span><span>홍정님 (공보담당관)</span><span class="bar">|</span><span>2020.08.06</span><span class="bar">|</span><span>위치:통일행정포털>게시판>언론브리핑포도자료</span>
							</div>
							<div class="contentArea">
								한반도 평화와 변영을 위한 <b>남북협력기금</b>의 활용방안 연구, 통일교육원 훈련원, 비핵형화시대를 견일할 남북사회문화교륙 협력 추진 결과를 보과합니다.
								한반도 평화와 변영을 위한 <b>남북협력기금</b>의 활용방안 연구, 통일교육원 훈련원, 비핵형화시대를 견일할 남북사회문화교륙 협력 추진 결과를 보과합니다.                          
							</div>
							<div class="fileArea">
								<div class="file">
									<img src="images/ico/file/hwp.gif" class="fileIco">
									<a href="javascript:void(0);" class="fileLink">파일명이 들어갑니다.hwp</a>
									<a href="javascript:void(0);" class="btnFileView">미리보기</a>
								</div>
								<div class="FileView_mordal">
									<div class="FileView_top">
										<p class="view_title">미리보기</p>
										<a href="javascript:void(0);" class="close_btn"></a>
									</div>
									<div class="FileView_bottom">
										<p class="view_info">
											위한 법제 마련에 착수하였다. 그 결과 1989년에 <b>남북</b>교류<b>협력</b>에 관한 기본지침을 제정한데 이어 1990년에는 남북교류협력에 관한 법률과 남북협력기금법이 제정되었다. 제1절 <b>남
											북협력기금</b> 조성  남북협력기금의재원은 정부풀연금, 정부외출연 위한 법제 마련에 착수하였다.위한 법제 마련에 착수하였다. 그 결과 1989년에 <b>남북</b>교류<b>협력</b>에 관한 기본지침을
											제정한데 이어 1990년에는 남북교류협력에 관한 법률과 남북협력기금법이 제정되었다. 제1절 <b>남북협력기금</b> 조성  남북협력기금의재원은 정부풀연금, 정부외출연 위한 법제 마련에 착수하였다.
											위한 법제 마련에 착수하였다. 그 결과 1989년에 <b>남북</b>교류<b>협력</b>에 관한 기본지침을
											제정한데 이어 1990년에는 남북교류협력에 관한 법률과 남북협력기금법이 제정되었다. 제1절 <b>남북협력기금</b> 조성  남북협력기금의재원은 정부풀연금, 정부외출연 위한 법제 마련에 착수하였다.
										</p>
									</div>
								</div>
							</div>
						</li>
						<li class="listItem">
							<div class="titAreaWrap">
								<div class="titArea">
									<a href="javascript:void(0);" class="tit">2019년 국과장급 국내장기훈련 결과보고서</a>
								</div>
							</div>
							<div class="infoArea">
								<span>공개</span><span class="bar">|</span><span>홍정님 (공보담당관)</span><span class="bar">|</span><span>2020.08.06</span><span class="bar">|</span><span>위치:통일행정포털>게시판>언론브리핑포도자료</span>
							</div>
							<div class="contentArea">
								한반도 평화와 변영을 위한 <b>남북협력기금</b>의 활용방안 연구, 통일교육원 훈련원, 비핵형화시대를 견일할 남북사회문화교륙 협력 추진 결과를 보과합니다.
								한반도 평화와 변영을 위한 <b>남북협력기금</b>의 활용방안 연구, 통일교육원 훈련원, 비핵형화시대를 견일할 남북사회문화교륙 협력 추진 결과를 보과합니다.                          
							</div>
							<div class="fileArea">
								<div class="file">
									<img src="images/ico/file/hwp.gif" class="fileIco">
									<a href="javascript:void(0);" class="fileLink">파일명이 들어갑니다.hwp</a>
									<a href="javascript:void(0);" class="btnFileView">미리보기</a>
								</div>
								<div class="FileView_mordal">
									<div class="FileView_top">
										<p class="view_title">미리보기</p>
										<a href="javascript:void(0);" class="close_btn"></a>
									</div>
									<div class="FileView_bottom">
										<p class="view_info">
											위한 법제 마련에 착수하였다. 그 결과 1989년에 <b>남북</b>교류<b>협력</b>에 관한 기본지침을 제정한데 이어 1990년에는 남북교류협력에 관한 법률과 남북협력기금법이 제정되었다. 제1절 <b>남
											북협력기금</b> 조성  남북협력기금의재원은 정부풀연금, 정부외출연 위한 법제 마련에 착수하였다.위한 법제 마련에 착수하였다. 그 결과 1989년에 <b>남북</b>교류<b>협력</b>에 관한 기본지침을
											제정한데 이어 1990년에는 남북교류협력에 관한 법률과 남북협력기금법이 제정되었다. 제1절 <b>남북협력기금</b> 조성  남북협력기금의재원은 정부풀연금, 정부외출연 위한 법제 마련에 착수하였다.
											위한 법제 마련에 착수하였다. 그 결과 1989년에 <b>남북</b>교류<b>협력</b>에 관한 기본지침을
											제정한데 이어 1990년에는 남북교류협력에 관한 법률과 남북협력기금법이 제정되었다. 제1절 <b>남북협력기금</b> 조성  남북협력기금의재원은 정부풀연금, 정부외출연 위한 법제 마련에 착수하였다.
										</p>
									</div>
								</div>
							</div>
						</li>
						<li class="listItem">
							<div class="titAreaWrap">
								<div class="titArea">
									<a href="javascript:void(0);" class="tit">2019년 국과장급 국내장기훈련 결과보고서</a>
								</div>
							</div>
							<div class="infoArea">
								<span>공개</span><span class="bar">|</span><span>홍정님 (공보담당관)</span><span class="bar">|</span><span>2020.08.06</span><span class="bar">|</span><span>위치:통일행정포털>게시판>언론브리핑포도자료</span>
							</div>
							<div class="contentArea">
								한반도 평화와 변영을 위한 <b>남북협력기금</b>의 활용방안 연구, 통일교육원 훈련원, 비핵형화시대를 견일할 남북사회문화교륙 협력 추진 결과를 보과합니다.
								한반도 평화와 변영을 위한 <b>남북협력기금</b>의 활용방안 연구, 통일교육원 훈련원, 비핵형화시대를 견일할 남북사회문화교륙 협력 추진 결과를 보과합니다.                          
							</div>
							<div class="fileArea">
								<div class="file">
									<img src="images/ico/file/hwp.gif" class="fileIco">
									<a href="javascript:void(0);" class="fileLink">파일명이 들어갑니다.hwp</a>
									<a href="javascript:void(0);" class="btnFileView">미리보기</a>
								</div>
								<div class="FileView_mordal">
									<div class="FileView_top">
										<p class="view_title">미리보기</p>
										<a href="javascript:void(0);" class="close_btn"></a>
									</div>
									<div class="FileView_bottom">
										<p class="view_info">
											위한 법제 마련에 착수하였다. 그 결과 1989년에 <b>남북</b>교류<b>협력</b>에 관한 기본지침을 제정한데 이어 1990년에는 남북교류협력에 관한 법률과 남북협력기금법이 제정되었다. 제1절 <b>남
											북협력기금</b> 조성  남북협력기금의재원은 정부풀연금, 정부외출연 위한 법제 마련에 착수하였다.위한 법제 마련에 착수하였다. 그 결과 1989년에 <b>남북</b>교류<b>협력</b>에 관한 기본지침을
											제정한데 이어 1990년에는 남북교류협력에 관한 법률과 남북협력기금법이 제정되었다. 제1절 <b>남북협력기금</b> 조성  남북협력기금의재원은 정부풀연금, 정부외출연 위한 법제 마련에 착수하였다.
											위한 법제 마련에 착수하였다. 그 결과 1989년에 <b>남북</b>교류<b>협력</b>에 관한 기본지침을
											제정한데 이어 1990년에는 남북교류협력에 관한 법률과 남북협력기금법이 제정되었다. 제1절 <b>남북협력기금</b> 조성  남북협력기금의재원은 정부풀연금, 정부외출연 위한 법제 마련에 착수하였다.
										</p>
									</div>
								</div>
							</div>
							<script type="text/javascript">//미리보기 modal창 fadeOut
								$(function(){
									$(".FileView_mordal").fadeOut(0);
								});
							</script>
						</li>
					</ul>
				</div>
				<a href="javascript:void(0);" class="btnMore">더보기</a>
			</div><!--resultDiv/e-->
			<!--resultDiv/s-->
			<div class="resultDiv">
				<p class="resultTit">온나라<b>700</b>건</p>
				<div class="resultCt">
					<ul class="list">
						<li class="listItem">
							<div class="titAreaWrap">
								<div class="titArea">
									<a href="javascript:void(0);" class="tit">200906_(보도자료) 제316차 <b>남북</b>교류<b>협력</b> 추진협의, 세계식량계획(WFP) ‘영유아, 여성 지원사업＇ 등에 기금 지원</a>
								</div>
							</div>
							<div class="infoArea">
								<span>공개</span><span class="bar">|</span><span>이수현 (공보담당관)</span><span class="bar">|</span><span>2020.08.06</span><span class="bar">|</span><span>위치:지식관리시스템>지식관리>결과보고서</span>
							</div>
						</li>
						<li class="listItem">
							<div class="titAreaWrap">
								<div class="titArea">
									<a href="javascript:void(0);" class="tit">200906_(보도자료) 제316차 <b>남북</b>교류<b>협력</b> 추진협의, 세계식량계획(WFP) ‘영유아, 여성 지원사업＇ 등에 기금 지원</a>
								</div>
							</div>
							<div class="infoArea">
								<span>공개</span><span class="bar">|</span><span>이수현 (공보담당관)</span><span class="bar">|</span><span>2020.08.06</span><span class="bar">|</span><span>위치:지식관리시스템>지식관리>결과보고서</span>
							</div>
						</li>
						<li class="listItem">
							<div class="titAreaWrap">
								<div class="titArea">
									<a href="javascript:void(0);" class="tit">200906_(보도자료) 제316차 <b>남북</b>교류<b>협력</b> 추진협의, 세계식량계획(WFP) ‘영유아, 여성 지원사업＇ 등에 기금 지원</a>
								</div>
							</div>
							<div class="infoArea">
								<span>공개</span><span class="bar">|</span><span>이수현 (공보담당관)</span><span class="bar">|</span><span>2020.08.06</span><span class="bar">|</span><span>위치:지식관리시스템>지식관리>결과보고서</span>
							</div>
						</li>
					</ul>
				</div>
				<a href="javascript:void(0);" class="btnMore">더보기</a>
			</div><!--resultDiv/e-->
			<!--resultDiv/s-->
			<div class="resultDiv">
				<p class="resultTit">국가법령정보센터<b>800</b>건</p>
				<div class="resultCt">
					<ul class="list">
						<li class="listItem">
							<div class="titAreaWrap">
								<div class="titArea">
									<a href="javascript:void(0);" class="tit">200906_(보도자료) 제316차 <b>남북</b>교류<b>협력</b> 추진협의, 세계식량계획(WFP) ‘영유아, 여성 지원사업＇ 등에 기금 지원</a>
								</div>
							</div>
							<div class="infoArea">
								<span>공개</span><span class="bar">|</span><span>이수현 (공보담당관)</span><span class="bar">|</span><span>2020.08.06</span><span class="bar">|</span><span>위치:지식관리시스템>지식관리>결과보고서</span>
							</div>
						</li>
						<li class="listItem">
							<div class="titAreaWrap">
								<div class="titArea">
									<a href="javascript:void(0);" class="tit">200906_(보도자료) 제316차 <b>남북</b>교류<b>협력</b> 추진협의, 세계식량계획(WFP) ‘영유아, 여성 지원사업＇ 등에 기금 지원</a>
								</div>
							</div>
							<div class="infoArea">
								<span>공개</span><span class="bar">|</span><span>이수현 (공보담당관)</span><span class="bar">|</span><span>2020.08.06</span><span class="bar">|</span><span>위치:지식관리시스템>지식관리>결과보고서</span>
							</div>
						</li>
						<li class="listItem">
							<div class="titAreaWrap">
								<div class="titArea">
									<a href="javascript:void(0);" class="tit">200906_(보도자료) 제316차 <b>남북</b>교류<b>협력</b> 추진협의, 세계식량계획(WFP) ‘영유아, 여성 지원사업＇ 등에 기금 지원</a>
								</div>
							</div>
							<div class="infoArea">
								<span>공개</span><span class="bar">|</span><span>이수현 (공보담당관)</span><span class="bar">|</span><span>2020.08.06</span><span class="bar">|</span><span>위치:지식관리시스템>지식관리>결과보고서</span>
							</div>
						</li>
					</ul>
				</div>
				<a href="javascript:void(0);" class="btnMore">더보기</a>
			</div><!--resultDiv/e-->
		</div><!--ct-center/e-->
		<!--ct-right/s-->
		<div class="ct-right">
			<div class="hitDiv">
				<div class="titArea">
					<p class="tit">인기 검색어</p>
				</div>
				<div class="ctArea">
					<div class="data">
						<ul class="hitList">
							<li class="hitItem top3"><!--add class:top3-->
								<span class="i">1</span>
								<a href="javascript:void(0);" title="투자계약법 길면 점점점처리">투자계약법 길면 점점점처리</a>
								<span class="rank up">300</span><!--add class:up-->
							</li>
							<li class="hitItem top3">
								<span class="i">2</span>
								<a href="javascript:void(0);" title="신규운용사">신규운용사</a>
								<span class="rank down">1</span><!--add class:down-->
							</li>
							<li class="hitItem top3">
								<span class="i">3</span>
								<a href="javascript:void(0);" title="수익자 배당 기준">수익자 배당 기준</a>
								<span class="rank down">88</span>
							</li>
							<li class="hitItem">
								<span class="i">4</span>
								<a href="javascript:void(0);" title="REITS">REITS</a>
								<span class="rank new">new</span><!--add class:new-->
							</li>
							<li class="hitItem">
								<span class="i">5</span>
								<a href="javascript:void(0);" title="계약보수율">계약보수율</a>
								<span class="rank">-</span>
							</li>
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
							<li class="historyItem">
								<span class="item">
									<a href="javascript:void(0);" class="txt" title="사회교류">사회교류</a>
								</span>
							</li>
							<li class="historyItem selected"><!--add class:selected-->
								<span class="item">
									<a href="javascript:void(0);" class="txt" title="남북경협 추진방안">남북경협 추진방안</a>
								</span>
							</li>
							<li class="historyItem">
								<span class="item">
									<a href="javascript:void(0);" class="txt" title="협력기금 운룔규정길어지면 쩜쩜쩜쩜">협력기금 운룔규정길어지면 쩜쩜쩜쩜</a>
								</span>
							</li>
							<li class="historyItem">
								<span class="item">
									<a href="javascript:void(0);" class="txt" title="사회교류">사회교류</a>
								</span>
							</li>
							<li class="historyItem">
								<span class="item">
									<a href="javascript:void(0);" class="txt" title="남북경협 추진방안">남북경협 추진방안</a>
								</span>
							</li>
						</ul>
					</div>
				</div>
			</div>
			<div class="historydbDiv"><!--RIGHT > historyDiv/s-->
				<div class="titArea">
					<p class="tit">연관 전문가 DB</p>
				</div>
				<div class="ctArea">
					<div class="data">
						<ul class="historyList">
							<li class="historyItem">
								<a href="javascript:void(0);" class="profile_img"><img src="images/thumb_nodata.png" alt="thumb_nodata"></a>
								<div class="dbinfo">
									<p class="name">김전문</p>
									<p class="field">전분분야 : 남북협력</p>
									<p class="belong">소속/직책:서울대</p>
								</div>
							</li>
							<li class="historyItem">
								<a href="javascript:void(0);" class="profile_img"><img src="images/thumb1.png" alt="thumb_nodata"></a>
								<div class="dbinfo">
									<p class="name">유전문</p>
									<p class="field">전분분야 : 남북협력</p>
									<p class="belong">소속/직책:서울대</p>
								</div>
							</li>
							<li class="historyItem">
								<a href="javascript:void(0);" class="profile_img"><img src="images/thumb2.png" alt="thumb_nodata"></a>
								<div class="dbinfo">
									<p class="name">박전문</p>
									<p class="field">전분분야 : 남북협력</p>
									<p class="belong">소속/직책:서울대</p>
								</div>
							</li>
							<li class="historyItem">
								<a href="javascript:void(0);" class="profile_img"><img src="images/thumb3.png" alt="thumb_nodata"></a>
								<div class="dbinfo">
									<p class="name">김전문</p>
									<p class="field">전분분야 : 남북협력</p>
									<p class="belong">소속/직책:서울대</p>
								</div>
							</li>
							<li class="historyItem">
								<a href="javascript:void(0);" class="profile_img"><img src="images/thumb1.png" alt="thumb_nodata"></a>
								<div class="dbinfo">
									<p class="name">유전문</p>
									<p class="field">전분분야 : 남북협력</p>
									<p class="belong">소속/직책:서울대</p>
								</div>
							</li>
							<li class="historyItem">
								<a href="javascript:void(0);" class="profile_img"><img src="images/thumb2.png" alt="thumb_nodata"></a>
								<div class="dbinfo">
									<p class="name">박전문</p>
									<p class="field">전분분야 : 남북협력</p>
									<p class="belong">소속/직책:서울대</p>
								</div>
							</li>
						</ul>
					</div>
				</div>
			</div>
		</div><!--ct-right/e-->
	</div><!--s-container/e-->
</div>
</body>
</html>
