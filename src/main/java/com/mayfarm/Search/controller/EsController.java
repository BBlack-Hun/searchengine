package com.mayfarm.Search.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mayfarm.Search.service.EsService;
import com.mayfarm.Search.vo.Criteria;
import com.mayfarm.Search.vo.EsVO;
import com.mayfarm.Search.vo.PageMaker;

@Controller
public class EsController {
	
	private static final Logger logger = LoggerFactory.getLogger(EsController.class);
	
	@Inject
	private EsService service;
	
	// 검색어 반환을 위해 전역변수 선언
	String str = "행안부";
	
	// 카테고리 반환을 위한 전역변수 선언
	String Category = "";
	
	// Main 검색 화면
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public String index(Model model, HttpServletRequest request, Criteria cri) {
		// 로그 출력
		logger.info("Search Index");
		
		// 결과를 반환하기 위한 list 선언
		List<EsVO> hit = new ArrayList<EsVO>();
		// 한번에 값을 넘기기 위해, Map에 저장해 둔다.
		Map<String, Object> modelMap = new HashMap<String, Object>();
		
		// 통합검색 Map 생성
		Map<String, Object> TSearch = new HashMap<String, Object>();
		// MOIS 카테고리 Map 생성
		Map<String, Object> MSearch = new HashMap<String, Object>();
		
		// PageMaker 객체 생성
		PageMaker pageMaker = new PageMaker(cri);
		// 관련 변수 초기화
		String totalCount = "";
		int toCnt;
		
		// 검색어 유입
//		str = request.getParameter("search");
		
		// 카테고리 유입
//		String Category = request.getParameter("Category");
		String Category = "통합검색";
		
		try {
			switch (Category) {
			case "통합검색":
				Category = "통합검색";
				TSearch = service.TSearch(str, cri, Category);
				
				modelMap.put("str", str);
				modelMap.put("Category", Category);
				modelMap.put("elastic", TSearch);
				modelMap.put("total", TSearch.get("total"));
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		model.addAttribute("index", modelMap);
		return "elastic/index";
	}

}
