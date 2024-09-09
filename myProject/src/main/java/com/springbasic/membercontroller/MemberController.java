package com.springbasic.membercontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MemberController {
	
	@RequestMapping("doMemberView")
	public String doMemberView() {
		System.out.println("doMemberView 호출됨");
		
		return "doMemberView";
	}
	
	// 매핑주소가 같으면, Ambiguous mapping 오류 발생
	@RequestMapping(value = {"memberSave", "memberSave2"}, method = RequestMethod.GET)
	public String doMemberSave() {
		return "memberSave";
	}
	
	@RequestMapping(value = "memberTest")
	public void doMemberTest(@RequestParam(name="name") String name,
							@RequestParam(name="age", required=false, defaultValue = "0") Integer age) {
		
		// required = false : String age -> null
		System.out.println("name : " + name);
		System.out.println("age : " + age);
	}
	
	@RequestMapping(value="paramTest")
	public String paramTest(@RequestParam(name="age", required = false) String age) {
		System.out.println("age : " + age);
		
		// 1) String age
		// ?쿼리스트링이 없음 : 400 에러
		// ?age=3 -> age : 3
		// ?age= -> age : (빈문자)
		// ?쿼리스트링이 없음 + (required = false) -> age : null
		// ?쿼리스트링이 없음 + (required = false, defaultValue = "0") -> age : null
		// ?쿼리스트링잉섭미
		
		
		
		return "paramTest";
	}
}
