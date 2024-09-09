package com.websyh.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.websyh.domain.Student;

/**
 * Handles requests for the application home page.
 */
@Controller // 현재 클래스가 컨트롤러단임을 암시
public class HomeController {
	
	// 로그를 남길 수 있도록 하는 객체
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	// servlet으로부터 요청된 객체를 매핑
	// "/"가 GET 방식으로 요청되면, 아래의 home 메서드를 호출한다.
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		// model : controller단에서 view단으로 바인딩된 데이터를 넘겨주는 객체
		model.addAttribute("serverTime", formattedDate );
		
		
		// view resolver에게 "home"문자열 반환
		// "/WEB-INF/view/" + "home" + ".jsp"가 객체화 되어
		// 최종 Dispatcher Servlet에 의해 response됨
		return "home";
	}
	
	@RequestMapping(value="/doAct1", method=RequestMethod.GET)
	public String doAction1() {
		System.out.println("doAction1이 호출됨");
		return "doAction1";
	}
	
	@RequestMapping(value="/doAct2")
	public void doAction2() {
		logger.info("doAct2가 호출됨");
	}
	
	@RequestMapping(value="/doAct3")
	public String doAction3(Model model) {
		logger.info("doAction3가 호출됨");
		String name = "syh";
		model.addAttribute("name", name);
		return "doAction3";
	}
	
	@RequestMapping(value="/doAct4")
	public ModelAndView doAction4() {
		logger.info("doAct4가 호출됨");
		
		String name = "syh";
		ModelAndView mav = new ModelAndView();
		mav.addObject("name", name); // ModelAndView 객체에 바인딩
		return mav;
	}
	
	// 안되네....
	@RequestMapping(value="/doAct5")
	public void doAction5(Model model) {
		logger.info("doAction5가 호출됨");
		
		Student stu = new Student("24001", "홍길동");
		
		//바인딩하는 객체의 이름을 지정하지 않은 경우에는
		// 자동으로 클래스명 앞글자를 소문자로 바꿔서 바인딩
		model.addAttribute("stu", stu);
	}

}
