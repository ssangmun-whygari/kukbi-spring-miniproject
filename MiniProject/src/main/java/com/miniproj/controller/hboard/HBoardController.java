package com.miniproj.controller.hboard;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.miniproj.model.BoardDetailInfo;
import com.miniproj.model.BoardUpFileStatus;
import com.miniproj.model.BoardUpFilesVODTO;
import com.miniproj.model.HBoardDTO;
import com.miniproj.model.HBoardReplyDTO;
import com.miniproj.model.HBoardVO;
import com.miniproj.model.MyResponseWithoutData;
import com.miniproj.service.hboard.HBoardService;
import com.miniproj.util.FileProcess;
import com.miniproj.util.GetClientIPAddr;


// Controller단에서 해야 할 일
// 1) URL 매핑 (어떤 URI가 어쩐방식 (GET/POST)으로 호출되었을 떄 어떤 메서드에 매핑 시킬 것이냐)
// 2) 있다면 view단에서 보내준 매개 변수 수집
// 3) 데이터베이스에 대한 CRUD를 수행하기 위해 service단의 해당 메서드를 호출,
//	  service 단에서 return 값을 view으로 바인딩 + view단 호출
// 4) 부가적으로 컨트롤러단은 Servlet에 의해 동작되는 모듈이기 때문에, HttpServletRequest, 
// 	  HttpServletResponse, HttpSession 등의 Servlet 객체를 이용할 수 있다.
// 	  -> 이러한 객체들을 이용하여 구현할 기능이 있다면, 그 기능은 컨트롤러단에서 구현한다.

@Controller // 아래의 클래스가 컨트롤러 객체임을 명시
@RequestMapping("/hboard")
public class HBoardController {
	private static final Logger logger = LoggerFactory.getLogger(HBoardController.class);
	
	// 유저가 업로드한 파일을 임시 보관하는 객체(컬렉션)
	private List<BoardUpFilesVODTO> uploadFileList = new ArrayList<>();
	// private List<BoardUpFilesVODTO> modifyFileList = new ArrayList<>(); // 수정요청시 유저가 업로드한 파일을 저장
	private List<BoardUpFilesVODTO> modifyFileList = new ArrayList<>();
	
	
	@Inject
	private HBoardService service;
	@Inject
	private FileProcess fileProcess; // FileProcess 객체 주입

	@RequestMapping("/listAll")
	public void listAll(Model model) {
		logger.info("HBoardController.listAll()........................");
		
		List<HBoardVO> list;
		try {
			list = service.getAllBoard();
			model.addAttribute("boardList", list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			model.addAttribute("exception", "error");
		}
	}
	
	@RequestMapping(value="/saveBoard", method=RequestMethod.GET)
	public String showSaveBoardForm() { // 게시판 글 저장페이지를 출력하는 메서드
		
		return "hboard/saveBoardForm";
	}
	
	// 게시글 저장 버튼을 누르면 해당 게시글을 DB에 저장하는 메서드
	@RequestMapping(value="/saveBoard", method=RequestMethod.POST) 
	public String saveBoard(HBoardDTO boardDTO, RedirectAttributes rttr) { 
		System.out.println("글저장하러 출발 : " + boardDTO.toString());
		
		// 첨부파일리스트를 list에 추가
		boardDTO.setFileList(uploadFileList);
		// 첨부파일 리스트에는 첨부한 것만 올라감...
		System.out.println("------------------첨부파일 리스트----------------");
		System.out.println(uploadFileList);
		System.out.println("------------------첨부파일 리스트----------------");
		
		String returnPage = "redirect:/hboard/listAll";
		
		try {
			if (service.saveBoard(boardDTO)) {
				System.out.println("저장 성공");
				rttr.addAttribute("status", "success");
			} else {
				System.out.println("저장 실패...");
			}
		} catch (Exception e) {
			e.printStackTrace();
			rttr.addAttribute("status", "fail");
		}
		
		// 이전 글의 파일들 저장시
		uploadFileList.clear();
		
		return returnPage;
	}
	
	@RequestMapping(value="/upfiles", method=RequestMethod.POST)
	public ResponseEntity<MyResponseWithoutData> saveBoardfile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		System.out.println("파일 전송 요청됨");
		System.out.println("파일의 오리지널 이름 : " + file.getOriginalFilename());
		System.out.println("파일의 사이즈 : " + file.getSize());
		System.out.println("파일의 contentType : " + file.getContentType());
		
		
		ResponseEntity<MyResponseWithoutData> result = null;
		try {
			//working...
			BoardUpFilesVODTO fileInfo = fileSave(file, request);
			
			//-------------fileInfo 결과 예시-----------------------------------------
			// newFileName=landscape-7527616_1280.jpg, 
			// originFileName=landscape-7527616_1280.jpg, 
			// thumbFileName=\2024\09\04\thumb_landscape-7527616_1280.jpg
			// ▲왜 thumbFileName은 따로 날짜가 붙음? 왜 이런 식으로 만들었지 햇갈리게?
			//------------------------------------------------------------------------
			
			uploadFileList.add(fileInfo);
			// working...
			System.out.println("uploadFileList에 갱신된 파일 정보");
			System.out.println(fileInfo.getNewFileName());
			String tmp = null;

			MyResponseWithoutData mrw;
			if (fileInfo.getThumbFileName() != null) { // 이미지 파일이면
				mrw = MyResponseWithoutData.builder().code(200).msg("success")
				.newFileName(fileInfo.getNewFileName())
				.subdir(fileInfo.getSubdir())
				.thumbFileName(fileInfo.getThumbFileName()).build();
				// thumbFileName도 부가적으로 보내준다. HTML에서 미리보기로 렌더링할때 필요하므로...
			} else {
				mrw = MyResponseWithoutData.builder().code(200).msg("success")
				.subdir(fileInfo.getSubdir())
				.newFileName(fileInfo.getNewFileName()).build();
			}

			result = new ResponseEntity<MyResponseWithoutData>(
				mrw, HttpStatus.OK
			);
			
		} catch (IOException e) {
			e.printStackTrace();
			result = new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}
		return result;
	}

	private BoardUpFilesVODTO fileSave(MultipartFile file, HttpServletRequest request) throws IOException {
		// 파일의 기본 정보 저장
		String oringinalFilename = file.getOriginalFilename();
		long fileSize = file.getSize();
		String contentType = file.getContentType();
		byte[] upfile = file.getBytes();
		System.out.println("서버의 실제 물리적 경로 : " + request.getSession().getServletContext().getRealPath("/resources/boardUpFiles"));
		String realPath = request.getSession().getServletContext().getRealPath("/resources/boardUpFiles");
		// 실제 파일 저장
		return fileProcess.saveFileToRealPath(upfile, realPath, contentType, oringinalFilename, fileSize);
	}
	
	@RequestMapping(value="/removefile", method = RequestMethod.POST)
	public ResponseEntity<MyResponseWithoutData> removeUpFile(@RequestParam("removeFileName") String removeFileName, HttpServletRequest request) {
		System.out.println("업로드된 파일을 삭제하자~ : " + removeFileName);
		
		ResponseEntity<MyResponseWithoutData> result = null;
		String serverPath = request.getSession().getServletContext().getRealPath("/resources/boardUpFiles"); //?????????
		
		System.out.println("현재 업로드된 파일 목록 : ");
		// 업로드된 파일 목록이 이상함...
		System.out.println(uploadFileList.stream().map(obj -> obj.getNewFileName()).collect(Collectors.toList()));
		
		
		// 이미지라면
		//	-> thumb_xxx.png 삭제
		//	-> original_xxx.png 삭제
		// 이미지가 아니라면
		//	-> original_xxx.ext 삭제
		
		int deleted = -1;
		for (int i = 0; i < uploadFileList.size(); i++) {
			if (uploadFileList.get(i).getThumbFileName().contains(removeFileName)) { // 이미지이다.
				String realPath = serverPath
						+ uploadFileList.get(i).getSubdir()
						+ File.separator
						+ uploadFileList.get(i).getThumbFileName();
				System.out.println("--------------------------------");
				System.out.println(realPath);
				fileProcess.removeFile(realPath);
			}
			if (uploadFileList.get(i).getNewFileName().contains(removeFileName)) {
				String realPath = serverPath
						+ uploadFileList.get(i).getSubdir()
						+ File.separator
						+ uploadFileList.get(i).getNewFileName();
				System.out.println("--------------------------------");
				System.out.println(realPath);
				fileProcess.removeFile(realPath);
				deleted = i;
			}
		}
		// fileProcess를 통해 지운 다음 uploadFileList에서도 파일이름을 지운다.
		if (deleted != -1) {
			uploadFileList.remove(deleted);
			result = new ResponseEntity<MyResponseWithoutData>(new MyResponseWithoutData(
					200, "", "", "", "success"), HttpStatus.OK);
		} else {
			result = new ResponseEntity<MyResponseWithoutData>(new MyResponseWithoutData(
					200, "", "", "", "fail"), HttpStatus.CONFLICT);
		}
		System.out.println("서버에서 파일 삭제 완료");
		return result;
		

		
		/*
		 * 
		 * 기존 코드
		 * 
		 * int removeIndex = -1; boolean removeResult = false;
		 * 
		 * if (removeFileName.contains("thumb_")) { for (int i = 0; i <
		 * uploadFileList.size(); i++) { if
		 * (uploadFileList.get(i).getThumbFileName().contains(removeFileName)) {
		 * System.out.println( i + "번째에서 해당 파일 찾았음 : " +
		 * uploadFileList.get(i).getThumbFileName() ); if
		 * (fileProcess.removeFile(realPath + removeFileName)) { removeIndex = i;
		 * System.out.println("파일 삭제 완료"); removeResult = true; break; }; } } } else {
		 * for (int i = 0; i < uploadFileList.size(); i++) { if
		 * (uploadFileList.get(i).getNewFileName().contains(removeFileName)) {
		 * System.out.println( i + "번째에서 해당 파일 찾았음 : " +
		 * uploadFileList.get(i).getThumbFileName() ); if
		 * (fileProcess.removeFile(realPath + removeFileName)) { removeIndex = i;
		 * System.out.println("noimage - " + removeFileName + "파일 삭제 완료"); removeResult
		 * = true; break; }; } } } if (removeResult) {
		 * uploadFileList.remove(removeIndex); // 리스트에서 삭제
		 * System.out.println("=================================================");
		 * System.out.println("현재 파일리스트에 있는 파일들"); for (BoardUpFilesVODTO f :
		 * uploadFileList) { System.out.println(f); }
		 * System.out.println("=================================================");
		 * result = new ResponseEntity<MyResponseWithoutData>(new MyResponseWithoutData(
		 * 200, "", "", "success"), HttpStatus.OK); } else { result = new
		 * ResponseEntity<MyResponseWithoutData>(new MyResponseWithoutData( 200, "", "",
		 * "fail"), HttpStatus.CONFLICT); } return null;
		 */
	}
	
	@RequestMapping(value="/removefileAll", method = RequestMethod.POST)
	public ResponseEntity<String> removeUpFileAll(HttpServletRequest request) {
		ResponseEntity<String> result = null;
		
		String serverPath = request.getSession().getServletContext().getRealPath("/resources/boardUpFiles");
		
		for (int i = 0; i < uploadFileList.size(); i++) {
			if (uploadFileList.get(i).getThumbFileName() != null) {
				String realPath = serverPath
						+ uploadFileList.get(i).getSubdir()
						+ File.separator
						+ uploadFileList.get(i).getThumbFileName();
				fileProcess.removeFile(realPath);
			}
			String realPath = serverPath
					+ uploadFileList.get(i).getSubdir()
					+ File.separator
					+ uploadFileList.get(i).getNewFileName();;
			fileProcess.removeFile(realPath);
		}
		System.out.println("전 파일 삭제 완료");
		result = new ResponseEntity<String>("success", HttpStatus.OK);
		return result;
	}
	
//	@GetMapping("/viewBoard") //void면 viewBoard.jsp로 간다네
//	public void viewBoard(@RequestParam(value="boardNo", defaultValue = "-1") int boardNo, Model model, HttpServletRequest request) throws Exception {
//		logger.info(boardNo + "번 글을 조회하자...");
//		// viewBoard.jsp에 상세글 + 업로드 파일 정보 출력
//		HBoardDTO article = service.getArticle(boardNo);
//		System.out.println(article);
//		if (article != null) {
//			model.addAttribute("title", article.getTitle());
//			model.addAttribute("writer", article.getWriter());
//			model.addAttribute("content", article.getContent());
//			model.addAttribute("attachedFiles", article.getFileList());
//			// 서버에 이미지도 요청할 수 있나?
//			String serverPath = request.getSession().getServletContext().getRealPath("/resources/boardUpFiles");
//		}
//	}
	
	// 게시글 수정도 게시글 보는 거랑 비슷하니까
	// =====================================================================
	// NOTE
	// GetMapping에 주소 두개 보내면 GET 요청이 2번 실행되는 것 같음
	// 하나는 boardNo 쿼리로 붙여주는데 다른 한 요청은 title, content 등 엉뚱한 걸 붙여줌...
	// 왜 그러는 지 모르겠음... 일단 분리시켜보겠다
	// ** 해결됨 : return문에서 redirect를 걸어주기 때문에 다시 순환하여
	// GET 요청을 함. 그래서 두번 요청이 된거임. 그냥 "viewBoard" String을 
	// 반환해야 viewBoard.jsp를 찾아서 렌더링해줌
	// =====================================================================
	@GetMapping({"/viewBoard", "/modifyBoard"})
	public String viewBoard(@RequestParam(value="boardNo", defaultValue = "-1") int boardNo, Model model, HttpServletRequest request) {	
		// viewBoard.jsp에 상세글 + 업로드 파일 정보 출력
		HBoardDTO article;
		try {
			System.out.println("&&&&&&&&&&&&&&&&&&boardNo :" + boardNo);
			System.out.println("Request URI: " + request.getRequestURI());
			System.out.println("Query String: " + request.getQueryString());
			article = service.getArticle(boardNo);
			System.out.println(article);
			if (article != null) {
				model.addAttribute("title", article.getTitle());
				model.addAttribute("writer", article.getWriter());
				model.addAttribute("content", article.getContent());
				model.addAttribute("attachedFiles", article.getFileList());
				// 서버에 이미지도 요청할 수 있나?
				String serverPath = request.getSession().getServletContext().getRealPath("/resources/boardUpFiles");
			}
		} catch (Exception e1) {
			// working... boardNo가 -1로 넘어가는 문제 있음.
			e1.printStackTrace();
			return "redirect:/hboard/listAll";
		}

		// 조회수 기능 구현을 위한 코드 (ip 주소, 24시간 등...)
		// working... 왜 List로 넘겨주지? 하나밖에 없는데
		List<BoardDetailInfo> boardDetailInfo = null;
		
		String ipAddr = GetClientIPAddr.getClientIp(request);
		logger.info(ipAddr + "에서" + boardNo + "번 글을 조회한다.");
		
		
		// /viewBoard, /modifyBoard 중 어느 곳에서 넘어왔는 지 판단하는 로직
		if (request.getRequestURI().equals("/hboard/viewBoard")) {
			System.out.println("게시글 상세보기 호출");
			try {
				boardDetailInfo = service.readArticle(boardNo, ipAddr);
			} catch (Exception e) {
				e.printStackTrace();
				return "redirect:/hboard/listAll";
			}
			model.addAttribute("boardDetailInfo", boardDetailInfo);
			return "hboard/viewBoard";
		} else if (request.getRequestURI().equals("/hboard/modifyBoard")) {
			System.out.println("게시글 수정 호출");
			try {
				boardDetailInfo = service.readArticle(boardNo, ipAddr);
			} catch (Exception e) {
				e.printStackTrace();
				return "redirect:/hboard/listAll";
			}
			model.addAttribute("boardDetailInfo", boardDetailInfo);
			return "hboard/modifyBoard";
		}
		return null;
	}
	
//	@GetMapping("/modifyBoard")
//	public String showModifyBoard(
//			@RequestParam(
//				value="boardNo", 
//				defaultValue = "-1"
//			) int boardNo, 
//			Model model, 
//			HttpServletRequest request) {
//		
//		return null;
//	}
	
	@RequestMapping("/showReplyForm")
	public String showReplyForm() {
		System.out.println("showReplyForm GET요청");
		return "/hboard/replyForm";
	}

	@RequestMapping(value="/saveReply", method=RequestMethod.POST)
	// rttr 맨날 까먹네...
	// jsp의 form 태그에서 name 속성 지정해서 보내면 HBoardReplyDTO 타입 객체를 자동으로 만들어줌
	public String saveReplyBoard(HBoardReplyDTO replyBoard, RedirectAttributes rttr) {
		System.out.println(replyBoard + "답글 저장하자~");
		
		String returnPage = "redirect:/hboard/listAll";
		try {
			if (service.saveReply(replyBoard)) {
				rttr.addAttribute("status", "success");
			}
		} catch (Exception e) {
			e.printStackTrace();
			rttr.addAttribute("status", "fail");
		}
		return returnPage;
	}
	
	@RequestMapping(value="/removeBoard")
	public String removeArticleFromBoard(
		@RequestParam("boardNo") int boardNo,
		HttpServletRequest request,
		RedirectAttributes rttr
	) throws Exception {
		System.out.println(boardNo + "번 글을 삭제해야지...");
		String ipAddr = GetClientIPAddr.getClientIp(request);
		String serverPath = request.getSession().getServletContext().getRealPath("/resources/boardUpFiles");
		
		// 첨부파일 삭제
		List<BoardDetailInfo> boardDetailInfo = service.readArticle(boardNo, ipAddr);
		for (BoardDetailInfo info : boardDetailInfo) {
			List<BoardUpFilesVODTO> files = info.getFileList();
			for (BoardUpFilesVODTO file : files) {
				if (file.getThumbFileName() != null) {
					String filePath = serverPath + file.getSubdir() 
							+ File.separator
							+ file.getThumbFileName();
					System.out.println("파일 주소 : " + filePath);
					fileProcess.removeFile(filePath);
				}
				String filePath = serverPath + file.getSubdir()
					+ File.separator + file.getNewFileName();
				System.out.println("파일 주소 : " + filePath);
				fileProcess.removeFile(filePath);
			}
		}
		
		
		if (service.deleteArticle(boardNo)) {
			System.out.println("글 삭제 완료, 글 목록 페이지로 이동합니다...");
			rttr.addAttribute("status", "removeSuccess");
		}
		return "redirect:/hboard/listAll";
	}

	// 게시글 수정 처리
	@RequestMapping(value="modifyRemoveFileCheck", method = RequestMethod.POST)
	public ResponseEntity<MyResponseWithoutData> modifyRemoveFileCheck(@RequestParam("removeFileNo") int removeFilePK) {
		System.out.println(removeFilePK + "번 파일 삭제 요청됨");
		System.out.println(this.modifyFileList);
		// working...
		try {
			BoardUpFilesVODTO file = service.getUploadedFileInfo(removeFilePK);
			System.out.println("======================================");
			System.out.println("파일 제대로 들어오는 거 맞어?");
			System.out.println(file);
			System.out.println("======================================");
			file.setFileStatus(BoardUpFileStatus.DELETE);
			this.modifyFileList.add(file);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("===========================================");
		System.out.println("현재 파일리스트에 있는 파일들 (수정시)");
		System.out.println(this.modifyFileList);
		System.out.println("===========================================");
		
		return new ResponseEntity<MyResponseWithoutData> (
				new MyResponseWithoutData(
						200,
						null,
						null, 
						null, 
						"success"), HttpStatus.OK);
	}
	
	@RequestMapping(value="/cancelRemFiles", method = RequestMethod.POST)
	public ResponseEntity<MyResponseWithoutData> cancelRemFiles() {
		System.out.println("파일리스트의 모든 파일삭제를 취소하고 싶다...");
		for (BoardUpFilesVODTO file : this.modifyFileList) {
			file.setFileStatus(null);
		}
		
		// outputModifyFIleList(); <- 파일 목록을 출력해주는 함수
		return new ResponseEntity<MyResponseWithoutData>(
				new MyResponseWithoutData(0, null, null, null, "success"),
				HttpStatus.OK
			);
	}
	
	@RequestMapping(value = "/modifyBoardSave", method = RequestMethod.POST)
	public void modifyBoardSave(HBoardDTO modifyBoard,
			@RequestParam("modifyNewFile") MultipartFile[] modifyNewFile) {
		System.out.println(modifyBoard.toString() + "를 수정하자...");
		
		for (int i = 0; i < modifyNewFile.length; i++) {
			System.out.println(
				"새로 업로드된 파일 : " + modifyNewFile[i].getOriginalFilename()
			);
		}
	}
}
