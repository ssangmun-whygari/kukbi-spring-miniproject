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
		<h1>게시물 보기</h1>
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
				<button type="button" class="btn btn-primary" onclick="validateBoard()">저장</button>
				<button type="button" class="btn btn-secondary" onclick="cancelBoard()">삭제</button>
				<button type="button" class="btn btn-warning" onclick="location.href='/hboard/listAll'">목록보기</button>
				
			</div>
		</c:forEach>
		

		
	</div>
	
	
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