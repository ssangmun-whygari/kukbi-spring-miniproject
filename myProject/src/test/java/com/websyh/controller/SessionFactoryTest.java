package com.websyh.controller;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "file:src/main/resources/root-context.xml"
})


public class SessionFactoryTest {

	@Inject
	private SqlSessionFactory ssf;
	
	@Test
	public void sqlSessionFactoryTest() {
		
		SqlSession session = ssf.openSession();
		System.out.println(session.toString());
		
		System.out.println(ssf.toString());
		
	}
}
