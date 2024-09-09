package com.springbasic.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springbasic.persistence.ExTxDAO;


@Service

public class ExTxServiceImpl implements ExTxService {

	@Inject
	private ExTxDAO dao;
	
	
	@Override
	// @Transactional(rollbackFor = Exception.class)
	public void saveData(String data) throws Exception {
		// insert 쿼리가 잘 실행되면...
		if (dao.insertDataTblA(data) == 1) {
			System.out.println("table A에 저장 성공");
		} else {
			System.out.println("table A에 저장 실패");
		}
		
		if (dao.insertDataTblB(data) == 1) {
			System.out.println("table B에 저장 성공");
		} else {
			System.out.println("table B에 저장 실패");
		}
//		
//		System.out.println("예외 발생!!");
//		throw new RuntimeException();
		 
	}

}
