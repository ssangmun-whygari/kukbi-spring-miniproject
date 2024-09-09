package com.springbasic.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SaveService {
	
	@Inject
	private SaveTableAService tableAService;
	
	@Inject
	private SaveTableBService tableBService;
	
	@Transactional
	public void insertIntoTables() {
		tableAService.insertTableA();
		
		tableBService.insertTableB();
	}
	
}
