package com.miniproj.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.miniproj.model.BoardDetailInfo;
import com.miniproj.model.BoardUpFilesVODTO;
import com.miniproj.model.HBoardDTO;
import com.miniproj.model.HBoardReplyDTO;
import com.miniproj.model.HBoardVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class HBoardDAOImpl implements HBoardDAO {

	@Inject
	private SqlSession ses;	
	
	private static final String ns = "com.miniproj.mappers.hboardMapper.";
	
	@Override
	public List<HBoardVO> selectAllBoard() throws Exception {
		log.info("Here is HBoardDAOImpl......");
		return ses.selectList(ns + "getAllBoard");
	}
	
	public int insertNewBoard(HBoardDTO newBoard) throws Exception {
		return ses.insert(ns + "saveNewBoard", newBoard);
	}

	@Override
	public int selectMaxBoardNo() throws Exception {
		return ses.selectOne(ns + "getMaxNo");
	}

	@Override
	public int insertBoardUpFile(BoardUpFilesVODTO file) throws Exception {
		return ses.insert(ns + "saveUpFile", file);
	}

	@Override
	public HBoardDTO getArticle(int boardNo) {
		System.out.println("getArticle 호출됨...");
		Map<String, Object> articleInfo1 = ses.selectOne(ns + "getArticleInfo1", boardNo);
		List<BoardUpFilesVODTO> attachedFiles = ses.selectList(ns + "getArticleInfo2", boardNo);
		System.out.println("articleInfo1 : ----------------------------------------");
		System.out.println(articleInfo1);
		System.out.println("attachedFiles : ----------------------------------------");
		System.out.println(attachedFiles);
		HBoardDTO result = new HBoardDTO(
				(int) articleInfo1.get("boardNo"),
				(String) articleInfo1.get("title"),
				(String) articleInfo1.get("content"),
				(String) articleInfo1.get("writer"),
				attachedFiles);
		return result;
	}
	
	@Override
	public int insertBoardReadLog(String ipAddr, int boardNo) {
		Map<String, Object> params = new HashMap<>();
		params.put("readWho", ipAddr);
		params.put("boardNo", boardNo);
		// params에 map을 쓰는 이유는 selectOne에 이미 지정되어 있으니까..
		return ses.insert(ns + "insertBoardReadLog", params);
	}
	
	@Override
	public int getDateDiff(String ipAddr, int boardNo) {
		Map<String, Object> params = new HashMap<>();
		params.put("readWho", ipAddr);
		params.put("boardNo", boardNo);
		// params에 map을 쓰는 이유는 selectOne에 이미 지정됭 있으니까..
		return ses.selectOne(ns + "selectDateDiff", params);
	}
	
	@Override
	public int updateReadCount(int boardNo) throws Exception {
		return ses.update(ns + "updateReadCount", boardNo);
	}
	
	@Override
	public int updateReadwhen(String ipAddr, int boardNo) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("readWho", ipAddr);
		params.put("boardNo", boardNo);
		return ses.update(ns + "updateReadWhen", params);
	}
	
	// BoardDetailInfo에는 db의 칼럼과 칼럼의 값이 필드와 필드의 값으로 매칭되어 있을 것이다.
	@Override
	public List<BoardDetailInfo> selectBoardDetailInfoByBoardNo(int boardNo) {
		return ses.selectList(ns + "selectBoardDetailInfoByBoardNo", boardNo);
	}
	
	@Override
	public Map<String, Object> selectBoardreadlog(int boardNo) {
		return ses.selectOne(ns + "selectBoardreadlog", boardNo);
	}

	// working...
	@Override
	public void updateBoardRef(int newBoardNo) {
		ses.update(ns + "updateBoardRef", newBoardNo);
	}

	@Override
	public void updateRefOrder(int ref, int refOrder) throws Exception {
		// map으로 myBatis가 받네....
		Map<String, Object> params = new HashMap<>();
		params.put("refOrder", refOrder);
		params.put("ref", ref);
		
		ses.update(ns + "updateBoardRefOrder", params);
	}

	@Override
	public int insertReplyBoard(HBoardReplyDTO replyBoard) {
		// TODO Auto-generated method stub
		return ses.insert(ns + "insertReplyBoard", replyBoard);
	}

	@Override
	public int deleteArticle(int boardNo) {
		return ses.update(ns + "markArticleToDeleted", boardNo);
	}

	@Override
	public BoardUpFilesVODTO selectUploadedFileInfo(int boardUpFileNo) {
		return ses.selectOne(ns + "selectUploadedFilesInfo", boardUpFileNo);
	}

}
