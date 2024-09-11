package com.miniproj.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class HBoardVO {

	private int boardNo;
	private String title;
	private String content;
	private String writer;
	private Timestamp postDate;
	private int readCount;
	private int ref;
	private int step;
	private int refOrder;
	private boolean isNew = false;
	private String isDeleted = "N";
	
	private void setIsNew() {
		// working, 현재 시간과 타임 스탬프 비교
		LocalDateTime old = this.postDate.toLocalDateTime();
		LocalDateTime now = LocalDateTime.now();
		isNew = true;
	}
}
