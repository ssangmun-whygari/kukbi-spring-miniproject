package com.miniproj.service.hboard;

import java.util.List;
import java.util.Map;

import com.miniproj.model.BoardDetailInfo;
import com.miniproj.model.BoardUpFilesVODTO;
import com.miniproj.model.HBoardDTO;
import com.miniproj.model.HBoardReplyDTO;
import com.miniproj.model.HBoardVO;
import com.miniproj.model.PagingInfo;
import com.miniproj.model.PagingInfoDTO;
import com.miniproj.model.SearchCriteriaDTO;

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

	boolean modifyBoard(HBoardDTO modifyBoard);

	//게시글 조회 -- 페이징
	Map<String, Object> getAllBoard(PagingInfoDTO dto);

	// 게시글 조회 -- 검색
	List<HBoardVO> getAllBoard(PagingInfoDTO dto, SearchCriteriaDTO searchCriteriaDTO);

	Map<String, Object> getAllBoardBySearchword(PagingInfoDTO dto, SearchCriteriaDTO searchCriteriaDTO);
	
}
