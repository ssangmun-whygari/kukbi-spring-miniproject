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
	})
	
	function renderAttachedFiles() {
		console.log("${attachedFiles}")
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
		    <c:forEach var="item" items="${attachedFiles}">
		    	<div>${item.newFileName}
			    	<c:if test="${item.thumbFileName ne null}">
			    		<img src="data:image/png;base64,${item.base64Img}"/>
			    	</c:if>
		    	</div>
            </c:forEach>
			<button type="button" class="btn btn-primary" onclick="validateBoard()">저장</button>
			<button type="button" class="btn btn-secondary" onclick="cancelBoard()">삭제</button>
			<button type="button" class="btn btn-warning" onclick="cancelBoard()">목록보기</button>
		</div>
		
		
	</div>
	<jsp:include page="../footer.jsp"></jsp:include>
</body>
</html>