package com.miniproj.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString

public class BoardUpFilesVODTO {
	private int boardUpFileNo;
	private String newFileName; // 예시 : landscape-7527616_1280 - 복사본 (2).jpg
	private String originFileName; // 예시 : landscape-7527616_1280.jpg
	private String thumbFileName; // 예시 : thumb_landscape-7527616_1280.jpg
	private String subdir; // 예시 : \2024\09\04
	private String ext;
	private long size;
	private int boardNo;
	private String base64Img;
}