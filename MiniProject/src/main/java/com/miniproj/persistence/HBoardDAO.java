package com.miniproj.persistence;

import java.util.List;
import java.util.Map;

import com.miniproj.model.BoardDetailInfo;
import com.miniproj.model.BoardUpFilesVODTO;
import com.miniproj.model.HBoardDTO;
import com.miniproj.model.HBoardVO;

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
}
