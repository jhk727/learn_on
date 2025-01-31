<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>런 온 - 온라인 No.1 교육 플랫폼</title>
<link rel="SHORTCUT ICON" href="${pageContext.request.contextPath}/resources/images/favicon.ico">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/common.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/reset.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/course.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/notice.css">
<script src="${pageContext.request.contextPath}/resources/js/jquery-3.7.1.js"></script>
</head>

<body>
	<header>
		<%-- inc/top.jsp 페이지 삽입(jsp:include 액션태그 사용 시 / 경로는 webapp 가리킴) --%>
		<jsp:include page="/WEB-INF/views/inc/top.jsp"></jsp:include>
	</header>
	
	
	
	<!-- 게시판 등록 -->
	<div id="nt_dt_form">
		<h2>문의하기</h2>
		<section class="tb-con">
			<div class="tb-hd">
				<h3 class="ttl">${courseSupport.c_support_subject}</h3>
				작성자 : <span class="author">${courseSupport.mem_id}</span>
				작성일자 : <span class="date">${courseSupport.c_support_date}</span>
 				카테고리 :
 				<c:if test="${courseSupport.c_support_category eq '01'}">수강/영상</c:if>
 				<c:if test="${courseSupport.c_support_category eq '02'}">결제/환불</c:if>
 				<c:if test="${courseSupport.c_support_category eq '03'}">기타</c:if>
			</div>
			<div class="tb-details">
				${courseSupport.c_support_content}
			</div>
			<div class="tb-files">
				<c:if test="${not empty courseSupport.c_support_file}">
				 	<div>
				 		<i class="fa-solid fa-paperclip"></i> 
				 		<a href="${pageContext.request.contextPath}/resources/upload/${courseSupport.c_support_file}" download="${originalFileList}">
				 			${courseSupport.c_support_file}<input type="button" value="다운로드">
				 		</a>
				 	</div>
				 </c:if>
			</div>
			<section class="tb-btns">
				<button class="btn-02" onclick="confirmDelete()">삭제</button>
				<button class="btn-03" onclick="CourseSupportModify(${courseSupport.c_support_idx}, ${param.pageNum})">수정</button>
				<button class="btn-03"onclick="location.href='CourseSupportList?class_id=${param.class_id}&pageNum=${param.pageNum}'">목록</button>
				
			</section>
		</section>
	</div>
	<script type="text/javascript">
		function confirmDelete() {
			if(confirm("문의글을 삭제하시겠습니까?")) {
				location.href = "CourseSupportDelete?" + getQueryParams(); // 페이지 요청
			}
		}
		
		function CourseSupportModify(cs_idx, pageNum) {
			location.href = "CourseSupportModifyForm?c_support_idx=" + cs_idx + "&pageNum=" + pageNum;
		}
		function getQueryParams() {
			let params = "";
			
			// URL 에서 파라미터 탐색하여 파라미터가 존재하면 URL 뒤에 파라미터 결합
			let searchParams = new URLSearchParams(location.search);
			for(let param of searchParams) {
				params += param[0] + "=" + param[1] + "&";
			}
			
			// 마지막 파라미터 뒤에 붙은 & 기호 제거
			if(params.lastIndexOf("&") == params.length - 1) { // & 기호가 배열의 끝에 있을 경우
				// & 기호 앞까지 추출하여 url 변수에 저장(덮어쓰기)
				params = params.substring(0, params.length - 1);
			}
			
			// 파라미터 결합된 문자열 리턴
			return params;
		}
	</script>
</body>
</html>








