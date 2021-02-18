package com.mayfarm.Search.controller;

import java.io.IOException;
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
import com.mayfarm.Search.vo.ParamVO;

@Controller
public class EsController {
	
	private static final Logger logger = LoggerFactory.getLogger(EsController.class);
	
	@Inject
	private EsService service;
	
	// 검색어 반환을 위해 전역변수 선언
	String str = "";
	
	// 카테고리 반환을 위한 전역변수 선언
	String Category = "";
	// 검색 내역 저장을 위한 전역 변수 선언
	String ostr = "";
	
	// Main 검색 화면
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public String index(Model model, HttpServletRequest request, Criteria cri, ParamVO paramVO) throws IOException {
		// 로그 출력
		logger.info("Search Index");
		
		// 결과를 반환하기 위한 list 선언
		List<EsVO> hit = new ArrayList<EsVO>();
		// 한번에 값을 넘기기 위해, Map에 저장해 둔다.
		Map<String, Object> modelMap = new HashMap<String, Object>();
		
		// 인기검색어 반환을 위한 list 선언 및 반환
		List<Map<String, Integer>> list = service.getSearchTopWordList();
		modelMap.put("searchResult", list);
		
		// 통합검색 검색어 자동완성 반환을 위한 list선언 및 반환
		List<String> list2 = service.getAutocompleteSearch(paramVO.getSearch());
		System.out.println(list2);
		modelMap.put("autoC", list2);
		
		// VO 반환
		modelMap.put("paramVO", paramVO);
			
		// 통합검색 Map 생성
		Map<String, Object> TSearch = new HashMap<String, Object>();
		// MOIS 카테고리 Map 생성
		Map<String, Object> MSearch = new HashMap<String, Object>();
		// LAW 카테고리 Map 생성
		Map<String, Object> LSearch = new HashMap<String, Object>();
		// NEWS 카테고리 Map 생성
		Map<String, Object> NSearch = new HashMap<String, Object>();
		
		
		// PageMaker 객체 생성
		PageMaker pageMaker = new PageMaker(cri);
		// 관련 변수 초기화
		String totalCount = "";
		int toCnt;
		
		// 검색어 유입
		str = request.getParameter("search");
					
		// 카테고리 유입
			Category = request.getParameter("category");
		if (Category == null) {
			Category = "통합검색";
		}
		
		try {
			switch (Category) {
			case "통합검색":
				Category = "통합검색";
				TSearch = service.TSearch(paramVO);
				
				modelMap.put("str", str);
				modelMap.put("ostr", ostr);
				modelMap.put("Category", Category);
				modelMap.put("elastic", TSearch);
				modelMap.put("total", TSearch.get("total"));
				break;
			
			case "MOIS":
				Category= "MOIS";
				MSearch = service.MSearch(paramVO);
				
				// 전체 게시물의 수를 구함 String -> int 형변환
				totalCount = MSearch.get("total").toString();
				toCnt = Integer.parseInt(totalCount);
				
				// pageMaker로 전달 -> pageMaker는 startPage, endPage, prev, next를 계산.
				pageMaker.setTotalCount(toCnt);
				
				modelMap.put("str", str);
				modelMap.put("Category", Category);
				modelMap.put("pageMaker", pageMaker);
				modelMap.put("elastic", MSearch);
				model.addAttribute("index", modelMap);
				return "elastic/mois";
			case "LAW":
				Category= "LAW";
				LSearch = service.LSearch(paramVO);
				
				// 전체 게시물의 수를 구함 String -> int 형변환
				totalCount = LSearch.get("total").toString();
				toCnt = Integer.parseInt(totalCount);
				
				// pageMaker로 전달 -> pageMaker는 startPage, endPage, prev, next를 계산.
				pageMaker.setTotalCount(toCnt);
				
				modelMap.put("str", str);
				modelMap.put("ostr", ostr);
				modelMap.put("Category", Category);
				modelMap.put("pageMaker", pageMaker);
				modelMap.put("elastic", LSearch);
				model.addAttribute("index", modelMap);
				return "elastic/law";
			case "NEWS":
				Category= "NEWS";
				NSearch = service.NSearch(paramVO);
				
				// 전체 게시물의 수를 구함 String -> int 형변환
				totalCount = NSearch.get("total").toString();
				toCnt = Integer.parseInt(totalCount);
				
				// pageMaker로 전달 -> pageMaker는 startPage, endPage, prev, next를 계산.
				pageMaker.setTotalCount(toCnt);
				
				modelMap.put("str", str);
				modelMap.put("ostr", ostr);
				modelMap.put("Category", Category);
				modelMap.put("pageMaker", pageMaker);
				modelMap.put("elastic", NSearch);
				model.addAttribute("index", modelMap);
				return "elastic/news";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		model.addAttribute("index", modelMap);
		return "elastic/index";
	}

}
