package com.springbasic.persistence;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

@Repository
public class ExTxDAOImpl implements ExTxDAO {

	@Inject
	SqlSession ses;
	
	@Override
	public int insertDataTblA(String data) {
		return ses.insert("com.springbasic.mappers.tableAMapper.insertData", data);
	}

	@Override
	public int insertDataTblB(String data) {
		return ses.insert("com.springbasic.mappers.tableBMapper.insertData", data);
	}

}
