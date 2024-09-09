<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>
	<h1>hello.jsp</h1>
	<div>이름 : ${param.name }</div>
	<div>나이 : ${param.age } </div>
	<div>status : ${status} </div>
	
	<hr/>
	<div>이름 : ${param.name} </div>
	<div>나이 : ${param.age} </div>
</body>
</html>