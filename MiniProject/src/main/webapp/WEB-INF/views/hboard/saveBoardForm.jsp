<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script type="text/javascript">
	let upfiles = new Array(); // 업로드되는 파일들을 저장하는 배열

	$(function() {
		// 업로드 파일 영역에 drag&drop과 관련된 이벤트 (파일의 경우 웹브라우저에서 열림)를 방지 
		$(".fileUploadArea").on("dragenter dragover", function(evt) {
			evt.preventDefault(); // 기본 이벤트 캔슬
		});
		
		$(".fileUploadArea").on("drop", function(evt) {
			evt.preventDefault();
			
			console.log(evt.originalEvent.dataTransfer.files);
			
			for (let file of evt.originalEvent.dataTransfer.files) {
				// 파일 사이즈를 검사해서 10MB가 넘게되면 파일 업로드가 안되도록 한다.
				console.log(file.size)
				if (file.size > 1024*1024*10) { // 10485760 바이트
					alert("파일 용량이 너무 큽니다. 업로드할 파일을 확인해 주세요.")
				} else {
					upfiles.push(file);
					console.log(upfiles);
					// 해당 파일을 업로드
					fileUpload(file);
				}
			}
		});		
	})
	
	// 실제로 유저가 업로드한 파일을 컨트롤러단에 전송하여 저장하도록 하는 함수
	// 드래그 앤 드롭으로 파일을 떨굴때 실행되는 함수
	function fileUpload(file) {
		let fd = new FormData() // form태그와 같은 역할의 객체
		fd.append("file", file);

		// processData : false -> 데이터를 쿼리스트링 형태로 보내지 않겠다는 설정
		// contentType : false -> 기본값인 application/x-www-form-urlencoded로 보내지 않겠다
        $.ajax({
            url: "/hboard/upfiles", // 데이터가 송수신될 서버의 주소
            type: "POST", // 통신 방식 (GET, POST, PUT, DELETE)
            data : fd,
            processData : false,
            contentType : false,
            success: function (data) {
              // 통신이 성공하면 수행할 함수
              // console.log(data);
 			  console.log("ajax 성공");
 			  console.log(data)
              if (data.msg == "success") {
            	  showPreview(file, data)
              };
              
            },
            error: function (request, status, error) {
            	console.log("ajax error!!!! fileupload")
                console.log("code: " + request.status)
                console.log("message: " + request.responseText)
                console.log("error: " + error);
            },
            complete: function () {
              console.log("ajax completed");
            },
          });
	}
	
	// 넘겨진 file이 이미지 파일이면 미리보기 하여 출력한다.
	function showPreview(file, data) {
		let imageType = ["image/jpeg", "image/png", "image/gif"]		
		let fileType = file.type;
		
		if (imageType.indexOf(fileType) != -1) {
			// 이미지 파일이라면...
			let output = `<div><img src='/resources/boardUpFiles/\${data.subdir}/\${data.thumbFileName}' /><span>\${file.name}</span>`
			output += `<span><img src='/resources/images/delete.png' width='20px;' onclick="remFile(this)"
				id='\${data.newFileName}'/></span></div>`
			$(".preview").append(output);
		} else {
			let output = `<div><img src='/resources/boardUpFiles/noimage.jpg'/><span>\${file.name}</span>`
			output += `<span><img src='/resources/images/delete.png' width='20px;' onclick="remFile(this)"
			id='\${data.newFileName}' /></span></div>`
			$(".preview").append(output);
		}
	}
	function cancelBoard() {
		clearPreviewBoard()
	}
	
	function clearPreviewBoard() {
		// 서버에 업로드된 모든 파일 지우기 요청하기 
		// working.....
        $.ajax({
            url: "/hboard/removefileAll", // 데이터가 송수신될 서버의 주소
            type: "POST", // 통신 방식 (GET, POST, PUT, DELETE)
            success: function (data) {
              // 통신이 성공하면 수행할 함수
 			  console.log("ajax 성공 removefileAll")
 			  console.log(data)
 			  if (data == "success") {
 				  //working....
 				 location.href = "/hboard/listAll"
 				 // $(".preview").empty()  
 			  }
            },
            error: function (request, status, error) {
            	console.log("ajax error!!!! removefileAll")
                console.log("code: " + request.status)
                console.log("message: " + request.responseText)
                console.log("error: " + error);
            },
            complete: function () {
              console.log("ajax completed");
            },
          });
	}
	
	// 업로드한 파일을 지운다. (화면, 백엔드)
	function remFile(obj) {
		console.log("지워야할 파일 이름 : " + $(obj).attr("id"));
		let removeFileName = $(obj).attr('id')
		
        $.ajax({
            url: "/hboard/removefile", // 데이터가 송수신될 서버의 주소
            type: "POST", // 통신 방식 (GET, POST, PUT, DELETE)
            data : {
            	"removeFileName" : removeFileName
            },
            success: function (data) {
              // 통신이 성공하면 수행할 함수
              // console.log(data);
 			  console.log("ajax 성공");
 			  console.log(data);
 			  if (data.msg == "success") {
 				 $(obj).parent().parent().remove();  
 			  }
            },
            error: function (request, status, error) {
            	console.log("ajax 실패, remFile")
                console.log("code: " + request.status)
                console.log("message: " + request.responseText)
                console.log("error: " + error);
            },
            complete: function () {
              console.log("ajax completed");
            },
          });
	}

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
		<h1>게시글 작성</h1>
		<form action="" method="POST">
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
		</form>
			<button style="display: inline-block;" class="btn btn-danger" onclick="cancelBoard()">취소</button>
		
		<!--  업로드 ㅍ일 영역 -->
		<div class="fileUploadArea">
			<p>업로드할 파일을 여기에 드래그 앤 드랍 하세요</p>
		</div>
		
		<div class="preview"></div>
		
		
	</div>
	<jsp:include page="../footer.jsp"></jsp:include>
</body>
</html>