package com.miniproj.model;

import java.sql.Timestamp;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PROTECTED) // 왜 PROTECTED로 했지?
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString


// HBoardDTO, HBoardVO랑 뭐가 다르지???????
public class BoardDetailInfo {
	private int boardNo;
	private String title;
	private String content;
	private String writer;
	private Timestamp postDate;
	private int readCount;
	private int ref; // 이게 뭐임?
	private int step; // 이게 뭐임?
	private int refOrder; // 이게 뭐임?
	
	private List<BoardUpFilesVODTO> fileList; // BoardUpFilesVODTO... 이름 진짜 이상하게 지었네
	
	private String userName;
	private String email;
}
