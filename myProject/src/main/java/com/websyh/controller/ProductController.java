package com.websyh.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.websyh.domain.ProductDTO;

@Controller
public class ProductController {
	@RequestMapping("viewProduct")
	public void viewProduct() {
		System.out.println("viewProduct 호출");
		// WEB-INF/Views/viewProduct.jsp 포워딩
	}
	
	@RequestMapping("viewProduct2")
	public String redirectView() {
		return "redirect:/";
	}
	
	@RequestMapping("viewProduct3")
	public String redirectView2(RedirectAttributes rttr) {
//		rttr.addAttribute("name", "둘리");
//		rttr.addAttribute("age", 10);
//		
//		// 쿼리스트링으로 넘겨지지 않는다!!! + 휘발성
//		rttr.addFlashAttribute("status", "success");
		
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", "둘리");
		map.put("age", "10");
		
		rttr.addAllAttributes(map);
		
		return "redirect:/hello";
	}
	
	// 파라미터 수집
	@RequestMapping(value="productTest", method = RequestMethod.GET)
	public void testParam(String name, int age) {
		System.out.println("name : " + name);
		System.out.println("age : " + age);
	}
	
	
	@RequestMapping(value="hello", method=RequestMethod.GET)
	public void hello(Model model) {
		
	}
	
//	@RequestMapping("productForm")
//	public void productFormGET() {
//	}
	
	@GetMapping("productForm")
	public void productFormGET() {
	}
	
//	@RequestMapping(value="prodPost", method=RequestMethod.POST)
//	public String saveProduct(@ModelAttribute(name="prod") ProductDTO product) {
//		System.out.println(product.toString());
//		return "product";
//	}
	
//	@RequestMapping(value="prodPost", method=RequestMethod.POST)
//	public String saveProduct(ProductDTO product) {
//		System.out.println(product.toString());
//		return "product";
//	}
	
	@PostMapping("prodPost")
	public String saveProduct(ProductDTO product) {
		System.out.println(product.toString());
		return "product";
	}
	
	// JSON 응답
	@GetMapping("jsonForm")
	public void jsonFormGET() {
	}
	
	@PostMapping("saveJson")
	public @ResponseBody ProductDTO outputJson(ProductDTO product) {
		return product; // product 객체가 JSON으로 포장된다.
	}
}
