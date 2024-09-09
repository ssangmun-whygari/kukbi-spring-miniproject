package com.miniproj.persistence;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.miniproj.model.PointLogDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class PointLogDAOImpl implements PointLogDAO {

	@Inject
	private SqlSession ses;
	
	private static final String ns = "com.miniproj.mappers.pointlogmapper.";
	
	@Override
	public int insertPointLog(PointLogDTO pointLogDTO) throws Exception {
		
		System.out.println("33333333333333333333333333333333333333333");
		System.out.println(pointLogDTO);
		System.out.println("33333333333333333333333333333333333333333");
		// TODO Auto-generated method stub
		return ses.insert(ns + "insertPointlog", pointLogDTO);
	}
}
