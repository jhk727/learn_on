<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>런 온 - 온라인 No.1 교육 플랫폼</title>
<link rel="SHORTCUT ICON" href="${pageContext.request.contextPath}/resources/images/favicon.ico">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/reset.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/common.css">
<script src="${pageContext.request.contextPath}/resources/js/jquery-3.7.1.js"></script>
<%-- <script src="${pageContext.request.contextPath}/resources/js/index.js"></script> --%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/success.css">

</head>
<body>
	<header id="hd">
		<jsp:include page="/WEB-INF/views/inc/top.jsp"></jsp:include>
	</header>
	
	<div class="success-container">
		<h1>안녕하세요 ${sessionScope.sId}님🥳</h1><br>
		<h1>인증메일이 [${sessionScope.sMail}]으로 발송 되었습니다!</h1><br>
		<h1>📩지금 바로 이메일 인증 후 강의를 시작 해보세요!</h1><br>
		
		<div id="controls">
			<input type="button" id="btn_ai" value="AI 추천 바로가기"><br>
			<input type="button" id="myclass" value="나의 강의실"onclick="location.href ='MyDashboard'"><br>
			<input type="button" value="이메일 인증 다시 하기📩" onclick="location.href ='ReSendAuthMail'">
		</div>
	
	</div>
	<footer id="ft">
		<jsp:include page="/WEB-INF/views/inc/bottom.jsp"></jsp:include>
	</footer>
	
</body>
</html>