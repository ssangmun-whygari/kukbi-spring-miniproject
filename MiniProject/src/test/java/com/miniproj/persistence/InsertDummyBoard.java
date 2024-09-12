package com.miniproj.persistence;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.miniproj.model.HBoardDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "file:src/main/webapp/WEB-INF/spring/root-context.xml"
})

public class InsertDummyBoard {
	@Inject
	private HBoardDAO dao;
	
	@Test
	public void InsertDummyDataBoard() throws Exception {
		for (int i = 0; i < 300; i++) {
			HBoardDTO dto = HBoardDTO.builder()
							.title("dummy title" + i + ".....")
							.content("dummy content")
							.writer("tosimi")
							.build();
			if (dao.insertNewBoard(dto) == 1) {
				int newBoardNo = dao.selectMaxBoardNo();
				dao.updateBoardRef(newBoardNo);
			}
		}
	}
}
