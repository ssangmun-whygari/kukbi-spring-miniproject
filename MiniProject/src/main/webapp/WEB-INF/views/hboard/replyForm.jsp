<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>답글 작성</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script type="text/javascript">
	function validateBoard() {
		// 게시글의 제목 (not null) 유효성 검사
		let result = false;
		let title = $("#title").val()
		console.log(title)
		console.log(title === '') // true
		console.log(title.length < 1)
		
		if (title === '') {
			// 제목을 입력하지 않았을 때
			alert("제목은 반드시 입력하셔야 합니다.")
			$("#title").focus()
		} else {
			result = true
		}
		return result;
	}
</script>

</head>
<body>	

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
	<div class="container">
		<h1>${param} 번에 대한 답글 작성 페이지</h1>
		<form action="/hboard/saveReply" method="POST">
			<div class="input-group mb-3">
			  <span class="input-group-text" id="title">글 제목</span>
			  <input type="text" 
			  	class="form-control" 
			  	placeholder="글 제목을 입력하세요..."
			  	name="title"/>
			</div>
			
			<div class="input-group mb-3">
			  <span class="input-group-text">작성자</span>
			  <input type="text" 
			  	class="form-control" 
			  	placeholder="작성자를 입력하세요..."
			  	name="writer"/>
			</div>

			<div class="input-group mb-3">
				<label for="comment">내용</label>
				<textarea class="form-control" rows="5" id="comment" name="content"></textarea>
			</div>
			<button type="submit" class="btn btn-primary" onclick="validateBoard()">저장</button>
				
			<!-- 값을 넘기는 방법 -->
			<div>
				<input name="ref" value=${param.ref} />
				<input name="step" value=${param.step} />
				<input name="refOrder" value=${param.refOrder} />
<!-- 				<input type="hidden" name="ref" value=${param.ref} />
				<input type="hidden" name="step" value=${param.step} />
				<input type="hidden" name="refOrder" value=${param.refOrder} / -->
			</div>
			<button style="display: inline-block;" class="btn btn-danger" onclick="cancelBoard()">취소</button>		
		</form>
		
	</div>

	<jsp:include page="../footer.jsp"></jsp:include>
</body>
</html>