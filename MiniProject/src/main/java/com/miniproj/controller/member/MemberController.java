package com.miniproj.controller.member;

import java.io.IOException;
import java.util.UUID;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.miniproj.model.MemberDTO;
import com.miniproj.model.MyResponseWithoutData;
import com.miniproj.service.member.MemberService;
import com.miniproj.util.FileProcess;
import com.miniproj.util.SendMailService;
import com.mysql.cj.util.StringUtils;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequestMapping("/member")
@Controller
public class MemberController {
	
	@Inject
	private MemberService mService;
	
	@Inject
	private FileProcess fp;
	
	@RequestMapping("/register")
	public void showRegisterForm() {
		
	}
	
	@RequestMapping(value="/register", method = RequestMethod.POST)
	public String registerMember(
			MemberDTO registerMember, 
			MultipartFile userProfile,
			HttpServletRequest request,
			RedirectAttributes rttr
			) {
		System.out.println("회원 가입 진행하자" + registerMember.toString());
		System.out.println(userProfile.getOriginalFilename());
		
		String resultPage = "redirect:/"; // 회원가입 완료 후 index.jsp로 가자 
		
		String realPath = request
			.getSession().getServletContext().getRealPath("/resources/userImg");
		System.out.println("실제 파일 경로 : " + realPath);
		
		String tmpUserProfileName = userProfile.getOriginalFilename();
		
		if (!StringUtils.isNullOrEmpty(tmpUserProfileName)) {
			String ext = tmpUserProfileName.substring(
				tmpUserProfileName.lastIndexOf(".") + 1);
			registerMember.setUserImg(
				registerMember.getUserId() + "." + ext);
		}
		
		try {
			if (mService.saveMember(registerMember)) { // DB에 저장
				rttr.addAttribute("status", "success");
				// 프로필을 올렸는 지 확인 -> 프로필 사진을 업로드 했다면
				if (!StringUtils.isNullOrEmpty(tmpUserProfileName)) {
					fp.saveUserProfile(
						userProfile.getBytes(), 
						realPath, 
						registerMember.getUserImg());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// IOException
			if (e instanceof IOException) {
				// 회원가입한 유저의 회원가입 취소 처리 (service-dao)
				rttr.addAttribute("status", "fileFail");
			} else {
				rttr.addAttribute("status", "fail");
			}
			resultPage = "redirect:/member/register"; // 실패할 경우 -> 다시 회원가입 페이지로
		}
		return resultPage;
	}
	
	@RequestMapping(value= "/isDuplicated", method = RequestMethod.POST)
	public ResponseEntity<MyResponseWithoutData> idIsDuplicated(
			@RequestParam("tmpUserId") String tmpUserId 
			) {
		log.info("tmpUserId : " + tmpUserId + "가 중복되는 지 확인하자");
		
		MyResponseWithoutData json = null;
		ResponseEntity<MyResponseWithoutData> result = null;
		
		try {
			if (mService.idIsDuplicated(tmpUserId)) {
				json = new MyResponseWithoutData(200, tmpUserId, null, null, "duplicated");
			} else {
				json = new MyResponseWithoutData(200, tmpUserId, null, null, "not duplicated");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		
		result = new ResponseEntity<MyResponseWithoutData>(json, HttpStatus.OK);
		
		return result;
	}
	
	@RequestMapping(value="/callSendMail", method = RequestMethod.POST)
	public void sendWithAuthCode(@RequestParam("email") String email) throws AddressException, MessagingException {
		log.info("email : " + email + "로 이메일을 보내자");
		String authCode = UUID.randomUUID().toString();
		
		try {
			new SendMailService().sendMail(email, authCode);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}