package com.websyh.persistance;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.springbasic.service.ExTxService;

// test에 들어가야 하는 코드
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "file:src/main/resources/root-context.xml"
		// root-context에는 bean을 쓰기 위한 설정이 들어있다...
		// <context:component-scan base-package="com.websyh.persistence"></context:component-scan>
})

public class MemberDAOTest {
	
	@Inject
	private ExTxService service;
	
	@Test
	public void transxText() throws Exception {
		service.saveData("abc");
	}
	
}
