package com.websyh.controller;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.springbasic.persistence.MemberDAO;
import com.websyh.domain.MemberDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "file:src/main/resources/root-context.xml"
})
public class MemberDAOTest {
	
	@Inject
	private MemberDAO dao;
	
//	@Test
//	public void getDateTimeTest() {
//		System.out.println(dao.getDateTime());
//	}
//	
	
//	@Test
//	public void insertMemberTest() throws Exception {
//		MemberDTO member = new MemberDTO(
//				"user3", "1234", "user3@abc.com",
//				"010-1111-4444",
//				null, null, null, null, null, null
//				);
//		if (dao.insertMember(member) == 1) {
//			System.out.println("회원 저장 성공");
//		}
//	}
	
//	@Test
//	public void updateMemberTest() throws Exception {
//		MemberDTO member = new MemberDTO(
//				"test", null, null, null, null, null, null, "uploadMember/noImage.png", null, null
//				);
//		if (dao.updateMember(member) == 1) {
//			System.out.println("수정 성공");
//		}
//	}
	
//	@Test
//	public void deleteMemberTest() throws Exception {
//		MemberDTO member = new MemberDTO("user3", null, null, null, null, null, null, null, null, null)
//		if (dao.deleteMember(member) == 1) {
//			System.out.println("삭제 성공");
//		}
//	}
	
//	@Test
//	public void selectAllMemberTest() throws Exception {
//		List<MemberDTO> list = dao.selectAllMembers();
//		
//		for (MemberDTO member : list) {
//			System.out.println(member);
//		}
//	}
	
//	@Test
//	public void selectMemberByUserIdTest() throws Exception {
//		System.out.println(dao.selectMemberByUserId("test2"));	
//	}
	
	@Test
	public void loginMemberTest() throws Exception {
		System.out.println(dao.loginMember("user1", "1234"));
		if (dao.loginMember("user3", "1234") != null ) {
			System.out.println("로그인 성공");
		} else {
			System.out.println("로그인 실패");
		}
	}
}
