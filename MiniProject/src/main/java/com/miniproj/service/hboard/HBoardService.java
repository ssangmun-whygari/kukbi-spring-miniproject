package com.miniproj.service.hboard;

import java.util.List;

import com.miniproj.model.BoardDetailInfo;
import com.miniproj.model.BoardUpFilesVODTO;
import com.miniproj.model.HBoardDTO;
import com.miniproj.model.HBoardReplyDTO;
import com.miniproj.model.HBoardVO;

public interface HBoardService {
	
	// 게시판 전체 리스트 조회
	List<HBoardVO> getAllBoard() throws Exception;

	boolean saveBoard(HBoardDTO boardDTO) throws Exception;
	
	HBoardDTO getArticle(int boardNo) throws Exception;

	List<BoardDetailInfo> readArticle(int boardNo, String ipAddr) throws Exception;

	boolean saveReply(HBoardReplyDTO replyBoard) throws Exception;

	boolean deleteArticle(int boardNo);

	List<BoardDetailInfo> readArticle(int boardNo) throws Exception;
	
	BoardUpFilesVODTO getUploadedFileInfo(int boardUpFileNo) throws Exception;
}
