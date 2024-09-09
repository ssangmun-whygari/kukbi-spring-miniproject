package com.websyh.domain;

public class Student {
	private String stuNo;
	private String stuName;
	
	
	
	public String getStuNo() {
		return stuNo;
	}

	public String getStuName() {
		return stuName;
	}

	public Student(String stuNo, String stuName) {
		super();
		this.stuNo = stuNo;
		this.stuName = stuName;
	}

	@Override
	public String toString() {
		return "Student [stuNo=" + stuNo + ", stuName=" + stuName + "]";
	}
}
