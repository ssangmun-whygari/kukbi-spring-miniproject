package com.springbasic.persistence;

import java.util.List;

import com.websyh.domain.MemberDTO;

public interface MemberDAO {
	// DB로부터 현재날짜시간을 얻어오는 메서드
	String getDateTime();
	
	// 회원 가입
	int insertMember(MemberDTO dto) throws Exception;
	
	// 회원정보 수정
	int updateMember(MemberDTO dto) throws Exception;
	
	// 회원 삭제
	int deleteMember(MemberDTO dto) throws Exception;
	
	// 모든 회원 조회
	List<MemberDTO> selectAllMembers() throws Exception;
	
	// 아이디로 회원조회
	MemberDTO selectMemberByUserId(String userId) throws Exception;
	
	// 로그인
	MemberDTO loginMember(String userId, String userPwd) throws Exception;
}
