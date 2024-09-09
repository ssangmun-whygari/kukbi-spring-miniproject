package com.springbasic.service;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "file:src/main/resources/root-context.xml"
})

public class ExTxTest {
	@Inject
	private ExTxService service;
	
	@Inject
	private SaveService saveService;
	
	@Test
	public void transxText() throws Exception {
		saveService.insertIntoTables();
		
	}
}
