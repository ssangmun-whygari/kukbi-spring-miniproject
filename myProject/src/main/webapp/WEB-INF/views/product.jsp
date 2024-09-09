<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>product.jsp</h1>
	<div>prod : ${prod}</div>
	<div>상품 이름 : ${prod.name}</div>
	<div>상품 번호 : ${prod.no}</div>
	<div>상품 가격 : ${prod.price}</div>
	
	<h1>product.jsp</h1>
	<div>prod : ${productDTO}</div> <!-- 클래스 이름에서 앞글자를 소문자로 한거임... -->
	<div>상품 이름 : ${productDTO.name}</div> 
	<div>상품 번호 : ${productDTO.no}</div>
	<div>상품 가격 : ${productDTO.price}</div>
</body>
</html>