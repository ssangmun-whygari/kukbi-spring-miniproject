<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
</head>
<body>
	<script>
	$(function() {
		renderAttachedFiles();
		$(".modal-close").on("click", function() {
			$("#deleteArticleConfirmModal").hide();
		})
	})
	
	function renderAttachedFiles() {
		console.log("${attachedFiles}")
	}
	
	function cancelBoard() {
		// 삭제 확인 모달창 띄우기(showDeleteModal)
		$("#deleteArticleConfirmModal").show(500);
	}
	
	function cancelRemoveFiles() {
		// 파일삭제 취소버튼 클릭시 호출
		$.ajax({
			url: "/hboard/cancelRemFiles",
			type: "POST",
			dataType: "json",
			async: false,
			success: function (data) {
				console.log(data)
 				if (data.msg == "success") {
					$(".fileSelect-checkbox").each(function(i, item) {
						$(item).prop('checked', false)
						$(item).prop("disabled", false)
						$(item).parent().parent().css("opacity", 1)
					})
				}
			},
			error: function() {
														
			},
			complete: function() {
				
			}
		})
	}
	
	// 파일 추가
	function addRows(obj) {
		let rowCount = $('.fileListTable tr').length
		console.log('tr 갯수 : ' + rowCount)
		let row = $(obj).parent().parent()
		let inputFileTag = `
			<tr>
			<td colspan='2'>
			<input 
				type='file'
				id='newFile_\${rowCount}'
				name="modifyNewFile"
			/>
			</td>
			<td>
			<input type="button" value='파일저장취소'> // working... onclick 메서드 구현
			</td>
			</tr>
			$(inputFileTag).insertBefore(row)
		`;
	}
	
	function showPreview(obj) {
		console.log(obj.files)
		if (obj.files[0].size > 1024*1024*10) {
			alert("10MB 이하의 파일만 업로드할 수 있다")
			obj.value = "";
			return;
		}
		
		// 파일 타입 확인
		let imageType = ["image/jpeg", "image/png", "image/gif", "image/jpg"]
		let fileType = obj.files[0].type
		let fileName = obj.files[0].name
		console.log(fileType)
		
		if (imageType.indexOf(fileType) != -1) { // 이미지 파일이라면
			let reader = new FileRead(); // FileReader 객체 생성
			reader.readAsDataURL(obj.files[0]); // 업로드된 파일을 읽어온다.
			reader.onload = function (e) {
				// reader 객체에 의해 파일을 읽기 완료하면 실행되는 콜백함수
				console.log(e.target)
				let imgTag = `<div>
					<img src='\${e.target.result}' width='50px' /><span>\${fileName}</span>
				</div>`
				$(imgTag).insertAfter(obj)
			}
		} else { // 이미지 파일이 아니라면
			let imgTag = `<div>
				<img src='noImage.jpg' width='50px' /><span>\${fileName}</span>
			</div>`
		}
		
	}
	
	function cancelAddFile(obj) {
		let fileTag = $(obj).parent().prev().children().eq(0); // input type=file 태그
		$(fileTag).val('');
	}
	
	<!-- 파일 삭제, 취소 메서드-->
	function removeSelectedFiles() {
 		boardUpFileNo = []
 		$(".fileSelect-checkbox:checked").each(function(index, element) {
			boardUpFileNo.push($(element).attr("id"))
		})
		if (boardUpFileNo.length == 0) {return;}
 		
		$.each(boardUpFileNo, function(index, item) {
			$.ajax({
				url: "/hboard/modifyRemoveFileCheck",
				type: "POST",
				dataType: "json",
				data: {
					"removeFileNo" : item
				},
				async: false,
				success: function (data) {
					console.log(data)
					if (data.msg == "success") {
						console.log("체크를 해제하고, 입력을 비활성화하고, 투명도를 낮춘다.")
						console.log(".fileselect-checkbox#" + item)
						$(".fileSelect-checkbox#" + item).prop("checked", false);
						$(".fileSelect-checkbox#" + item).prop("disabled", true);  // 입력 비활성화
						$(".fileSelect-checkbox#" + item).parent().parent().css("opacity", 0.3);
					}
				},
				error: function() {
															
				},
				complete: function() {
					
				}
			})
		})
 	} // function end
	</script>

	<style>
		.fileUploadArea {
			width: 100%;
			height: 200px;
			background-color: lightgray;
			text-align: center;
			line-height: 200px;
		}
	</style>

	<jsp:include page="../header.jsp"></jsp:include>
	
	
	
	<div>${boardDetailInfo}</div>
	
	
	<div class="container">
		<h1>게시물 수정</h1>
		<c:forEach var="article" items="${boardDetailInfo}">
		
		
			<form action="" method="POST">
				<div class="input-group mb-3">
				  <span class="input-group-text" id="title">제목</span>
				  <input type="text" 
				  	class="form-control" 
				  	value="${title}"
				  	name="title"/ readonly>
				</div>
				
				<div class="input-group mb-3">
				  <span class="input-group-text">작성자</span>
				  <input type="text" 
				  	class="form-control" 
				  	value="${writer }"
				  	name="writer"/ readonly>
				</div>
	
				<div class="input-group mb-3">
					<label for="comment">내용</label>
					<textarea class="form-control" rows="5" id="comment" name="content"
					>${content }</textarea>
				</div>
			</form>
			<!--  업로드 ㅍ일 영역 -->
			<div class="attachedFiles">
			
				<!-- !!!!!! 문제 생길 가능성 있음...... -->
			    <c:forEach var="item" items="${attachedFiles}">
			    	<div>${item.newFileName}
				    	<c:if test="${item.thumbFileName ne null}">
				    		<img src="data:image/png;base64,${item.base64Img}"/>
				    	</c:if>
			    	</div>
	            </c:forEach>
	            
	            
	            <div>${article}</div>
	            <button 
	            	type="button"
	            	class="btn btn-info"
	            	onclick="location.href='/hboard/showReplyForm?ref=${article.getRef()}&step=${article.getStep()}&refOrder=${article.getRefOrder()}'">
	            	답글달기
	            </button>
	            <!-- working... 잘 작동되나 확인 필요 -->
				<button type="button" class="btn btn-primary" onclick="location.href='hboard/modifyBoard?boardNo=${article.getBoardNo()}'">수정</button>
				<button type="button" class="btn btn-secondary" onclick="cancelBoard()">삭제</button>
				<button type="button" class="btn btn-warning" onclick="location.href='/hboard/listAll'">목록보기</button>
				
			</div>
			
			<div class="fileList">
				<table class="table table-hover">
					<thead>
						<tr>
							<th>#</th>
							<th>uploadedFiles</th>
							<th>fileName</th>
						</tr>
					</thead>
					<tbody>
					<!-- working... 이거 수정해야 함 -->
< 					<c:forEach var="file" items="${attachedFiles}">
					<c:if test="${file.boardUpFileNo != '0'}">
						<tr>
 						<td>
						<input type="checkbox" class="form-check-input fileSelect-checkbox" id="${file.boardUpFileNo }">
						</td>
						<td>
							<c:choose>
							<c:when test="${file.thumbFileName != null }">
								<img src="/resources/boardUpFiles/${file.subdir}/${file.thumbFileName}" />
							</c:when>
							<c:when test='${file.thumbFileName == null }'>
								<a href="/resources/boardUpFiles/${file.subdir}/${file.newFileName }">
								<img src="/resources/images/noimage.png" width="100px"/>
								</a>
							</c:when>
							</c:choose>
						</td>
						<td>
							${file.originFileName }
						</td>
						</tr>
					</c:if> 
					</c:forEach>
					</tbody>
				</table>
				<div>
					<button type="button" class="btn btn-danger" onclick="removeSelectedFiles()">선택한 파일 삭제</button>
					<button type="button" class="btn btn-secondary" onclick="cancelRemoveFiles()">파일 삭제 취소</button>
				</div>
			</div>
			
			
			
			
		</c:forEach>
		

		
	</div> <!-- container end -->
	
	
	
	<!-- 삭제 모달 -->
	<div class="modal" id="deleteArticleConfirmModal">
	  <div class="modal-dialog">
	    <div class="modal-content">
	
	      <!-- Modal Header -->
	      <div class="modal-header">
	        <h4 class="modal-title">Modal Heading</h4>
	        <button type="button" class="btn-close modal-close" data-bs-dismiss="modal"></button>
	      </div>
	
	      <!-- Modal body -->
	      <!-- boardDetailInfo.get(0) <- 이 방식 나중에 문제 생길 가능성 있음 -->
	      <div class="modal-body">
	        정말 ${boardDetailInfo.get(0).getBoardNo()} 번 글을 삭제할까요?
	      </div>
	
	      <!-- Modal footer -->
	      <div class="modal-footer">
	      	<!-- 문제 있을 가능성 있음 -->
	        <button 
	        	type="button" 
	        	class="btn btn-danger" 
	        	data-bs-dismiss="modal" 
	        	onclick="location.href='/hboard/removeBoard?boardNo=${param.boardNo}'"
	        >삭제</button>
	       	<button 
	        	type="button" 
	        	class="btn btn-primary modal-close" 
	        	data-bs-dismiss="modal">
	        취소</button>
	      </div>
	
	    </div>
	  </div>
	</div>
	
	
	
	
	<jsp:include page="../footer.jsp"></jsp:include>
</body>
</html>