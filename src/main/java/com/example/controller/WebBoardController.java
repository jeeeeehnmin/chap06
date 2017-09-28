package com.example.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.domain.WebBoard;
import com.example.persistence.WebBoardRepository;
import com.example.vo.PageMaker;
import com.example.vo.PageVO;
import com.querydsl.core.types.Predicate;

import lombok.extern.java.Log;

@Controller
@RequestMapping("/board/")
@Log
public class WebBoardController {
	
	@Inject
	WebBoardRepository repo;

	@GetMapping("/register")
	public String registerGET(@ModelAttribute("vo")WebBoard vo ){
		log.info("register get");
//		vo.setTitle("샘플 게시물 제목입니다....");
//		vo.setContent("내용을 처리해 봅니다 " );
		vo.setWriter("user00");
		
		return "jsp/board/register";
	}
	
	@PostMapping("/register")
	public String registerPOST(@ModelAttribute("vo")WebBoard vo, RedirectAttributes rttr){
		
		log.info("register post");
		log.info("" + vo);

		repo.save(vo);
		rttr.addFlashAttribute("msg", "success");
		
		return "redirect:/board/list";
	}
	
	
	@GetMapping("/view")
	public String view(Long bno, @ModelAttribute("pageVO") PageVO vo, Model model){
		
		log.info("BNO: "+ bno);
		
		repo.findById(bno).ifPresent(board -> model.addAttribute("vo", board));
		
		return "jsp/board/view";
	}
	
	@GetMapping("/list")
	public String list(@ModelAttribute("xxx")PageVO vo, Model model){
		
		Pageable pageable = vo.makePageable(0, "bno");
		Predicate predicate = repo.makePredicate(vo.getType(), vo.getKeyword());
		
		Page<WebBoard> result = repo.findAll(predicate, pageable);
		
		log.info("board/list()...");
		log.info("PageVO=" + vo);
		
		model.addAttribute("pageMaker", new PageMaker(result));
		
		return "jsp/board/list";
	}

}


