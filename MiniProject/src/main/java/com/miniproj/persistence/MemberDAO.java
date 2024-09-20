package com.miniproj.persistence;

import com.miniproj.model.MemberDTO;

public interface MemberDAO {
	// 유저의 userPoint를 수정하는 메서드
	int updateUserPoint(String userId) throws Exception;

	int selectDuplicateId(String tmpUserId) throws Exception;

	int insertMember(MemberDTO registerMember) throws Exception;

}
