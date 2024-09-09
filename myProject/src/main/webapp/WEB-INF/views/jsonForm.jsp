<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>productForm.jsp</h1>
<form action="saveJson" method="POST">
	<div>상품번호 : <input type="text" name="no"/></div>
	<div>상품이름 : <input type="text" name="name"/></div>
	<div>상품가격 : <input type="text" name="price"/></div>
	<button type="submit">저장</button>
</form>
</body>
</html>