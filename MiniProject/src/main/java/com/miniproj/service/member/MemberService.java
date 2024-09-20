package com.miniproj.service.member;

import com.miniproj.model.MemberDTO;

public interface MemberService {
	// 아이디 중복검사
	boolean idIsDuplicated(String tmpUserId) throws Exception;

	boolean saveMember(MemberDTO registerMember) throws Exception;
	
	
}
