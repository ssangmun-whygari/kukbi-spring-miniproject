package com.springbasic.persistence;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.websyh.domain.MemberDTO;

@Repository // 아래의 클래스가 DAO 객체임을 명시
public class MemberDAOImpl implements MemberDAO {

	private static String ns = "com.springbasic.mappers.myMapper.";
	
	@Inject
	private SqlSession ses; // SqlSessionTemplate 객체 주입
	
	@Override
	public String getDateTime() {
		
		String result = ses.selectOne(ns + "getDateTime");
		
		return result;
	}

	@Override
	public int insertMember(MemberDTO dto) throws Exception {
		int result = ses.insert(ns + "insertMember", dto);
		
		return result;
	}

	@Override
	public int updateMember(MemberDTO dto) throws Exception {
		return ses.update(ns + "updateMember", dto);
	}

	@Override
	public int deleteMember(MemberDTO dto) throws Exception {
		return ses.delete(ns + "deleteMember", dto);
	}

	@Override
	public List<MemberDTO> selectAllMembers() throws Exception {
		return ses.selectList(ns + "viewAllMember");
	}

	@Override
	public MemberDTO selectMemberByUserId(String userId) throws Exception {
		return ses.selectOne(ns + "viewMemberByUserId", userId);
	}

	@Override
	public MemberDTO loginMember(String userId, String userPwd) throws Exception {
		// 여러개의 parameter 값을 map으로 감싸서 넘길 수 있다.
		HashMap<String, String> parameters = new HashMap<>();
		parameters.put("userId", userId);
		parameters.put("userPwd", userPwd);
		return ses.selectOne(ns + "loginMember", parameters);
	}

}
