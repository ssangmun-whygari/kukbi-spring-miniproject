package com.miniproj.persistence;

import java.util.List;
import java.util.Map;

import com.miniproj.model.BoardDetailInfo;
import com.miniproj.model.BoardUpFilesVODTO;
import com.miniproj.model.HBoardDTO;
import com.miniproj.model.HBoardReplyDTO;
import com.miniproj.model.HBoardVO;
import com.miniproj.model.PagingInfo;
import com.miniproj.model.SearchCriteriaDTO;

public interface HBoardDAO {
	List<HBoardVO> selectAllBoard() throws Exception;
	
	int insertNewBoard(HBoardDTO newBoard) throws Exception;
	
	int selectMaxBoardNo() throws Exception;
	
	int insertBoardUpFile(BoardUpFilesVODTO file) throws Exception;

	HBoardDTO getArticle(int boardNo);

	List<BoardDetailInfo> selectBoardDetailInfoByBoardNo(int boardNo);
	
	int insertBoardReadLog(String ipAddr, int boardNo);

	int getDateDiff(String ipAddr, int boardNo);

	int updateReadCount(int boardNo) throws Exception;

	int updateReadwhen(String ipAddr, int boardNo) throws Exception;

	Map<String, Object> selectBoardreadlog(int boardNo);

	void updateBoardRef(int newBoardNo);
	
	void updateRefOrder(int ref, int refOrder) throws Exception;
	
	int insertReplyBoard(HBoardReplyDTO replayBoard);

	int deleteArticle(int boardNo);

	BoardUpFilesVODTO selectUploadedFileInfo(int boardUpFileNo);

	// 게시글 삭제
	int updateBoardByBoardNo(HBoardDTO modifyBoard);

	// 게시글 수정 -- 첨부파일 삭제
	void deleteBoardUpFile(int boardUpFileNo);

	int getTotalPostCnt();

	// 게시글 목록 조회 -- 페이징
	List<HBoardVO> selectAllBoard(PagingInfo pi);

	List<HBoardVO> selectAllBoard(SearchCriteriaDTO searchCriteriaDTO);

	int getTotalPostCntWithSearchWord(SearchCriteriaDTO sc);

	Map<String, Object> selectAllBoard(PagingInfo pi, SearchCriteriaDTO searchCriteriaDTO);
}
