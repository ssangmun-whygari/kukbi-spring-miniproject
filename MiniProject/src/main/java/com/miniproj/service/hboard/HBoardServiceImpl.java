package com.miniproj.service.hboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.miniproj.model.BoardDetailInfo;
import com.miniproj.model.BoardUpFileStatus;
import com.miniproj.model.BoardUpFilesVODTO;
import com.miniproj.model.HBoardDTO;
import com.miniproj.model.HBoardReplyDTO;
import com.miniproj.model.HBoardVO;
import com.miniproj.model.PagingInfo;
import com.miniproj.model.PagingInfoDTO;
import com.miniproj.model.PointLogDTO;
import com.miniproj.model.SearchCriteriaDTO;
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

	// working....
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
			bDao.updateBoardRef(newBoardNo); // 답글이 아니라 그냥 게시글에는 ref를 자신으로 지정
			
			for (BoardUpFilesVODTO file : newBoard.getFileList()) {
				file.setBoardNo(newBoardNo);
				// working...
				// 첨부파일들의 정보를 관리하는 boardupfiles 테이블에 삽입하는 메소드임
				// 근데 subdir에 자꾸 NULL이 삽입됨
				System.out.println("%%%%%%%%%%%%%%%%%%%debug%%%%%%%%%%%%%%%%%%%");
				System.out.println("file 정보 삽입하는데 왜 subdir에 NULL이 들어감??");
				System.out.println("file의 정보는 다음과 같다");
				System.out.println(file);
				System.out.println("이 시점의 file에는 subdir이 있다.");
				System.out.println("%%%%%%%%%%%%%%%%%%%debug%%%%%%%%%%%%%%%%%%%");
				bDao.insertBoardUpFile(file);
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
		System.out.println("boardInfo에 subdir이 제대로 들어가는가? 확인하자");
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
	
	// 조회수 처리 안해도 되니까 ip주소도 안념겨받음
	@Override
	public List<BoardDetailInfo> readArticle(int boardNo) throws Exception {
		return null;
		
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
	
	// working... jsp에서 replyBoard를 넘겨줘야 함
	@Override
	@Transactional(rollbackFor = Exception.class, propagation=Propagation.REQUIRED, isolation = Isolation.DEFAULT)
	public boolean saveReply(HBoardReplyDTO replyBoard) throws Exception {
		boolean result = false;
		System.out.println("답글저장 서비스 호출");
		// working... replyBoard.getRef()가 계속 0나오는데?
		// 부모글에 대한 다른 답글이 있으면, 기존 답글의 refOrder를 + 1 씩해줘야 함
		// 최초 답글이면 기존 답글이 없으니까 아래 코드는 실행되지 않음...
		bDao.updateRefOrder(replyBoard.getRef(), replyBoard.getRefOrder());
		// 답글 저장
		replyBoard.setStep(replyBoard.getStep() + 1);
		replyBoard.setRefOrder(replyBoard.getRefOrder() + 1);
		if (bDao.insertReplyBoard(replyBoard) == 1) {
			result = true;
		}
		return result;
	}
	
	@Override
	public boolean deleteArticle(int boardNo) {
		boolean result = false;
		if (bDao.deleteArticle(boardNo) == 1) {
			
		}
		// working... 첨부 파일 삭제 처리 해야 함
		result = true;
		return result;
	}

	@Override
	public BoardUpFilesVODTO getUploadedFileInfo(int boardUpFileNo) throws Exception {
		BoardUpFilesVODTO result = null; 
		result = bDao.selectUploadedFileInfo(boardUpFileNo);
		return result;
	}

	// 게시글 수정 처리
	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	public boolean modifyBoard(HBoardDTO modifyBoard) {
		boolean result = false;
		System.out.println(modifyBoard);
		System.out.println(modifyBoard.getFileList().size() + "개의 파일");
		
		// 1) 순수게시글 update
		if (bDao.updateBoardByBoardNo(modifyBoard) == 1) {
			// 2) 업로드파일의 fileStatus
			List<BoardUpFilesVODTO> fileList = modifyBoard.getFileList();
			for (BoardUpFilesVODTO file : fileList) {
//				if (file.getFileStatus() == BoardUpFileStatus.INSERT) {
//					file.setBoardNo(modifyBoard.getBoardNo());
//					bDao.insertBoardUpFile(file);
//				} else if (file.getFileStatus() == BoardUpFileStatus.DELETE) {
//					bDao.deleteBoardUpFile(file.getBoardUpFileNo());
//				}
				if (file.getFileStatus() == BoardUpFileStatus.DELETE) {
					bDao.deleteBoardUpFile(file.getBoardUpFileNo());
				}
			}
			result = true;
		}
		
		return result;
	}

	@Override
	public Map<String, Object> getAllBoard(PagingInfoDTO dto) {
		PagingInfo pi = makePagingInfo(dto);
		
		List<HBoardVO> list = bDao.selectAllBoard(pi);
		
		for (HBoardVO hBoardVO : list) {
			System.out.println(hBoardVO);
		}
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("pagingInfo", pi);
		resultMap.put("boardList", list);
		
		return resultMap;
	}
	
	private PagingInfo makePagingInfo(PagingInfoDTO dto) {
		PagingInfo pi = new PagingInfo(dto);
		
		// setter 호출
		// working... 계산식 수정 필요
		pi.setTotalPostCnt(bDao.getTotalPostCnt()); // 이럼 totalPostCnt가 설정됨
		System.out.println("총 글의 갯수 : " + pi.getTotalPostCnt());
		
		pi.setTotalPageCnt(); // totalPostCnt와 viewPostCntPerPage로 계산함
		pi.setStartRowIndex(); // pageNo와 viewPostCntPerPage로 설정함
		
		// 페이징 블럭
		pi.setPageBlockNoCurPage();
		pi.setStartPageNoCurBlock();
		pi.setEndPageNoCurBlock();
		
		System.out.println("pagingInfo : " + pi.toString());
		
		return pi;
	}

	// working... 수정 필요
	@Override
	public List<HBoardVO> getAllBoard(
			PagingInfoDTO dto, 
			SearchCriteriaDTO searchCriteriaDTO) {
		return bDao.selectAllBoard(searchCriteriaDTO);
	}
	
	// makePagingInfo의 오버로드
	private PagingInfo makePagingInfo(PagingInfoDTO dto, SearchCriteriaDTO sc) {
		PagingInfo pi = new PagingInfo(dto);
		
		// setter 호출
		// working... 계산식 수정 필요
		
		pi.setTotalPostCnt(bDao.getTotalPostCntWithSearchWord(sc)); // 이럼 totalPostCnt가 설정됨
		System.out.println("총 글의 갯수 : " + pi.getTotalPostCnt());
		
		pi.setTotalPageCnt(); // totalPostCnt와 viewPostCntPerPage로 계산함
		pi.setStartRowIndex(); // pageNo와 viewPostCntPerPage로 설정함
		
		// 페이징 블럭
		pi.setPageBlockNoCurPage();
		pi.setStartPageNoCurBlock();
		pi.setEndPageNoCurBlock();
		
		System.out.println("pagingInfo : " + pi.toString());
		
		return pi;
	}

	@Override
	public Map<String, Object> getAllBoardBySearchword(
			PagingInfoDTO dto,
			SearchCriteriaDTO searchCriteriaDTO) {
		PagingInfo pi = makePagingInfo(dto, searchCriteriaDTO);
		return bDao.selectAllBoard(pi, searchCriteriaDTO);
	}
}
