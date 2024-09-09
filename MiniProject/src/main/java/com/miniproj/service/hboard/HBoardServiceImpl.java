package com.miniproj.service.hboard;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.miniproj.model.BoardDetailInfo;
import com.miniproj.model.BoardUpFilesVODTO;
import com.miniproj.model.HBoardDTO;
import com.miniproj.model.HBoardVO;
import com.miniproj.model.PointLogDTO;
import com.miniproj.persistence.HBoardDAO;
import com.miniproj.persistence.MemberDAO;
import com.miniproj.persistence.PointLogDAO;

import lombok.extern.slf4j.Slf4j;


// Service 단에서 해야 할 작업
// 1) Controller에서 넘겨진 파라미터를 처리한 후 (비즈로직에 의해... 트랜잭션 처리를 통해)
// 2) DB작업이 필요하다면 DAO단 호출...
// 3) DAO단에서 반환된 값을 Controller단으로 넘겨준다.




@Slf4j
@Service // 아래의 클래스가 서비스 객체임을 컴파일러 공지
public class HBoardServiceImpl implements HBoardService {
	
	@Inject
	private HBoardDAO bDao;
	@Inject
	private PointLogDAO pDao;
	@Inject
	private MemberDAO mDao;
	
	@Override
	public List<HBoardVO> getAllBoard() throws Exception {
		System.out.println("HBoardServiceImpl..........");
		log.info("HBoardServiceImpl!!!");
		
		List<HBoardVO> list = bDao.selectAllBoard();
		
		return list;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean saveBoard(HBoardDTO newBoard) throws Exception {
		boolean result = false;
		
		//---------------------------------------------------------------
		// 트랜잭션
		// 하나라도 예외가 나면 안됨
		//---------------------------------------------------------------
		if (bDao.insertNewBoard(newBoard) == 1) { // 게시글 저장 성공함?
			System.out.println("insertNewBoard 통과");
			int newBoardNo = bDao.selectMaxBoardNo();
			for (BoardUpFilesVODTO file : newBoard.getFileList()) {
				file.setBoardNo(newBoardNo);
				bDao.insertBoardUpFile(file); // 파일 리스트도 보내야지
			}
			// 로그도 써야지
			if (pDao.insertPointLog(new PointLogDTO(newBoard.getWriter(), "글작성")) == 1) {
				System.out.println("insertPointLog 통과");
				if (mDao.updateUserPoint(newBoard.getWriter()) == 1) {
					System.out.println("updateUserPoint 통과");
					result = true;
				}
			}
		}
		return result;
	}

	
	@Override
	public HBoardDTO getArticle(int boardNo) throws Exception {
		HBoardDTO article = bDao.getArticle(boardNo);
		return article;
	}
	
	@Override
	public List<BoardDetailInfo> readArticle(int boardNo, String ipAddr) throws Exception {
		List<BoardDetailInfo> boardInfo = bDao.selectBoardDetailInfoByBoardNo(boardNo); 
		
		System.out.println("===================================================");
		System.out.println(boardInfo);
		System.out.println("===================================================");
		
		// 조회수 증가
		// dateDiff = 날짜차이계산결과
		int dateDiff = bDao.getDateDiff(ipAddr, boardNo);
		
		System.out.println("===================================================");
		System.out.println("지나간 날짜 : " + dateDiff + "일");
		System.out.println("===================================================");
		
		if (dateDiff == -1) { // ipAddr가 boardNo번 글을 최초로 조회
			if (bDao.insertBoardReadLog(ipAddr, boardNo) == 1) { // 지금 시간이 log에 기록됨
				updateReadCount(boardNo, boardInfo);
			}
		} else if (dateDiff >= 1) { // ipAddr가 boardNo번 글을 다시 조회
			bDao.updateReadwhen(ipAddr, boardNo);
			// 조회수 증가 (+1)
			updateReadCount(boardNo, boardInfo);
		}
		
		Map<String, Object> boardreadlog = bDao.selectBoardreadlog(boardNo);
		System.out.println("======================================================");
		System.out.println(boardreadlog);
		System.out.println("======================================================");
		
		return boardInfo;
	}
	
	private void updateReadCount(int boardNo, List<BoardDetailInfo> boardInfo) throws Exception {
		if (bDao.updateReadCount(boardNo) == 1) {
			// BoardDetailInfo 객체에도 readCount 속성 있으니까 그것도 업데이트해줘야 함
			// 쿼리를 다시 날려서 객체의 정보를 갱신하면 어떻냐고?? 그건 자원 낭비임
			for (BoardDetailInfo b : boardInfo) {
				b.setReadCount(b.getReadCount() + 1);
			}
		}
	}

}
