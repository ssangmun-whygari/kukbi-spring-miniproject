<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판 목록</title>
</head>
<body>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script>
	$(function() {
		showModal();
		
		// $(".modalCloseBtn").on()
		
/* 		$(".modalCloseBtn").click((function() {
			$("#myModal").hide();
		}) */
		highlightNewArtcle();
		timediffPostDate();
	})
		
 	function showModal() {
		let status = '${param.status}'; // url 주소창에서 status 쿼리스트링의 값을 가져와 변수 저장
		console.log(status);
		
 		if (status == 'scuccess') {
			// 글저장성공 모달창
			$(".modal-body").html("<h5>글 저장에 성공하였습니다.</h5>");
			$("#myModal").show();
		} else if (status == 'fail') {
			$(".modal-body").html('<h5>글 저장에 실패하였습니다.</h5>')
			$("#myModal").show();
		}
		
		// 게시글을 불러올 때 예외가 발생한 경우
		let except = '${exception}'
		if (except == 'error') {
			$(".modal-body").html("<h2>문제가 발생해 데이터를 불러오지 못했습니다.</h2>")
			$("#myModal").show();
		}	
	}
		
	function highlightNewArtcle() {
		// 이 코드가 자꾸 문제 일으킴
		/*console.log('${boardList}')*/
		$(".newMark").html("????");
	}
	
	function timediffPostDate() {
		$(".postDate").each(function(index, element) {
			console.log(index + "번째 : " + $(element).html());
			
			let postDate = new Date($(element).html()); // 글 작성일 저장 (Date 객체로 변환)
			let currentDate = new Date(); // 현재 날짜 시간 객체 생성
			console.log(currentDate - postDate); // 밀리초
			let diff = (currentDate - postDate) / 1000 / 3600
			console.log(diff);
			
			let title = $(element).prev().prev().html()
			console.log(title)
			
			if (diff < 3) {
				let output = "<span><img src='/resources/images/icons8-new-64.png'/></span>"
				$(element).prev().prev().html(title + output)
			}
		})
	}
</script>

<script>
function goToArticle(boardNo) {
	console.log("--------------------------------------------")
	console.log(boardNo);
	console.log(`게시글번호 \${boardNo}가 잘 전달되나???`)
	console.log("--------------------------------------------")
	location.href = `/hboard/viewBoard?boardNo=\${boardNo}`
}
</script>

<script>
	function removeFileCheckBox(fileId) {
		// 체크박스를 클릭할 때 호출되는 함수
		console.log(fileId)
		let checkCount = countCheckBoxChecked()
		if (checkCount > 0) {
			$(".removeUpFileBtn").removeAttr("disabled")
			$(".removeUpFileBtn").val(checkCount + " 개 파일을 삭제합니다.")
		} else if (checkCount == 0) {
			$(".removeUpFileBtn").attr("disabled", true)
			$(".removeUpFileBtn").attr("선택된 파일이 없습니다")
		}
	}
	
	function countCheckBoxChecked() {
		// 체크된 체크박스 갯수
		let result = 0
		$(".fileCheck").each(function (i, item) {
			console.log($(item).is(":checked"))
			if ($(item).is(":checked")) {
				result++;
			}
		})
		console.log(result + "개가 체크됨")
		return result
	}
	
	function removeFile() {
		// 비활성화해놓은 [선택된파일삭제] 버튼 클릭시 호출
		let removeFileArray = []
		
		$(".fileCheck").each(function(i, item) {
			if ($(item).is(":checked")) { // 파일 삭제하려고 체크박스를 체크했다면
				let tmp = $(item).attr("id") // 선택된 파일의 id값을 얻어옴 (pk)
				removeFileArray.push(tmp)
			}
		})
		
		console.log("삭제될 파일 : " + removeFileArray);
		
		$.each(removeFileArray, function(i, item) {
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
						// 투명도 0.2로 바꾸기
					}
				},
				error: function() {
															
				},
				complete: function() {
					
				}
			})
		})
		
	}
</script>

<jsp:include page="../header.jsp"></jsp:include>
	<div class="container">
		<h1>계층형 게시판 전체 목록</h1>
		<div>${boardList}</div>
		<h2>테이블이라네</h2>
		<table class="table table-hover"><thead>
		<tr>
			<th scope="col">글번호</th>
			<th scope="col">제목</th>
			<th scope="col">글쓴이</th>
			<th scope="col">게시날짜</th>
			<th scope="col">조회수</th>
		</tr></thead>
		<tbody><c:forEach var="article" items="${boardList}">

			<c:if test="${article.getIsDeleted() eq 'N'}">
			<tr onclick="goToArticle(${article.getBoardNo()})">		
 				<td>
 					<c:if test=""> <!--  -->
 						<span class="newMark"><img src="/resources/images/icons8-new-64.png" width="32" height="32"/></span>
 					</c:if>
 					${article.getBoardNo()}
 				</td>
				<td>
					<c:if test="${article.step > 0 }">
						<img 
							src='/resources/images/rightArrow.png'
							style="margin-left: calc(20px * ${article.step})"/>
					</c:if>
					${article.getTitle()}
				</td>
				<td>${article.getWriter()}</td>
				<td class="postDate">${article.getPostDate()}</td>
				<td>${article.getReadCount()}</td>
			</tr>
			</c:if>
		</c:forEach>
		</tbody></table>
		<div>
		<button type="button" class="btn btn-success" onclick="location.href='/hboard/saveBoard'">글쓰기</button>
		</div>
		
	</div>
	
	<!-- 모달 추가 -->
	<div class="modal" id="myModal">
	  <div class="modal-dialog">
	    <div class="modal-content">
	
	      <!-- Modal Header -->
	      <div class="modal-header">
	        <h4 class="modal-title">Modal Heading</h4>
	        <button type="button" class="btn-close modalCloseBtn" data-bs-dismiss="modal"></button>
	      </div>
	
	      <!-- Modal body -->
	      <div class="modal-body">
	        Modal body..
	      </div>
	
	      <!-- Modal footer -->
	      <div class="modal-footer">
	        <button type="button" class="btn btn-danger modalCloseBtn" data-bs-dismiss="modal">Close</button>
	      </div>
	
	    </div>
	  </div>
	</div>
	
	<!-- base64 확인해보기 -->
	<div>
	</div>
	
<jsp:include page="../footer.jsp"></jsp:include>
</body>
</html>