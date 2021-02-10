package com.mayfarm.Search.vo;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.Data;

@Data
public class PageMaker {
	
	private int displayPageCnt = 10;
	private int totalDataCount;
	private int startPage;
	private int endPage;
	private boolean prev;
	private boolean next;
	private Criteria cri;
	
	// 생성자
	public PageMaker(Criteria cri) {
		this.cri = cri;
	}
	
	// 전체 게시물의 수를 입력 받음
	public void setTotalCount(int totalDataCount) {
		this.totalDataCount = totalDataCount;
		calcData();
	}
	
	// startPage, endPage, prev, next를 계산
	public void calcData() {
		int page = this.cri.getPage();
		int perPageNum = this.cri.getPerPageNum();
		
		/**
		 * 예: 현재 페이지가  13이면 13/10 = 1.3 반올림 -> 2, 끝페이지는 2 * 10 = 20
		 * ceil - 반올림
		 */
		this.endPage = (int)(Math.ceil(page/(double)displayPageCnt)*displayPageCnt);
		
		/**
		 * 예: 현재 페이지가 13이면 20 - 10 + 1 = 11
		 */
		this.startPage = (this.endPage-displayPageCnt) + 1;
		
		// 실제로 사용되는 페이지의 수
		// 예: 전체 게시물의 수가 88개이면 88/10 = 8.8 올림 -> 9
		int tempEndpage = (int)(Math.ceil(totalDataCount / (double) perPageNum));
		
		if (this.endPage > tempEndpage) {
			this.endPage = tempEndpage;
		}
		
		this.prev = (startPage != 1);	// startPage가 1이 아니면 False
		this.next = (endPage * perPageNum < totalDataCount);	// 아직 더 보여줄 페이지가 있으니 true
	}
	
	public String makeQuery(int page) {
		UriComponents uriComponents = UriComponentsBuilder.newInstance()
						.queryParam("page", page)
						.queryParam("perPageNum", this.cri.getPerPageNum())
						.build()
						.encode();
		return uriComponents.toString();
	}
	
	
	
}
