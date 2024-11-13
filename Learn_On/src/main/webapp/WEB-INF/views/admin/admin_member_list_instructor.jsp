<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
    <title>LearnOn - 관리자 페이지</title>
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <meta content="" name="keywords">
    <meta content="" name="description">

    <!-- Favicon -->
    <link href="resources/admin/img/favicon.ico" rel="icon">

    <!-- Google Web Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Heebo:wght@400;500;600;700&display=swap" rel="stylesheet">
    
    <!-- Icon Font Stylesheet -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.10.0/css/all.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css" rel="stylesheet">

    <!-- Libraries Stylesheet -->
    <link href="resources/admin/lib/owlcarousel/assets/owl.carousel.min.css" rel="stylesheet">
    <link href="resources/admin/lib/tempusdominus/css/tempusdominus-bootstrap-4.min.css" rel="stylesheet" />

    <!-- Customized Bootstrap Stylesheet -->
    <link href="resources/admin/css/bootstrap.min.css" rel="stylesheet">
    
	<!-- 한글폰트 -->
    <link rel="stylesheet" href="resources/admin/css/reset.css">
    
    <!-- Template Stylesheet -->
    <link href="resources/admin/css/style.css" rel="stylesheet">
</head>
<body>
	<%@include file="inc/sidebar.jsp"%>
	<%@include file="inc/navbar.jsp"%>
	
	<%-- 내용 시작 --%>
	<!-- Blank Start -->
			<div class="container-fluid pt-4 px-4">
				<div class="bg-light rounded p-4">
					<div class="d-flex mb-5">
						<h5 class="me-auto tableSubject">강사 회원 목록</h5>
						<button type="button" class="btn btn-lg btn-primary ms-3">강사 등록</button>
						<button type="button" class="btn btn-lg btn-primary ms-3">강사 수정</button>
						<button type="button" class="btn btn-lg btn-primary ms-3">강사 삭제</button>
					</div>
					<div class="d-flex input-group mb-3">
						<input type="text" class="form-control" placeholder="회원 이름 검색" aria-label="Recipient's username" aria-describedby="button-addon2">
						<button class="btn btn-primary" type="button" id="button-addon2">검색</button>
					</div>
					<div class="d-flex">
						<table class="table table-striped">
							<thead>
								<tr>
									<th scope="col">#</th>
									<th scope="col">회원 번호</th>
									<th scope="col">아이디</th>
									<th scope="col">이름</th>
									<th scope="col">닉네임</th>
									<th scope="col">생년월일</th>
									<th scope="col">성별</th>
									<th scope="col">이메일</th>
									<th scope="col">연락처</th>
									<th scope="col">상태</th>
									<th scope="col">승인여부</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${getMemberList}" var="ml">
								<c:if test="${ml.MEM_GRADE eq 'MEM02' or ml.MEM_STATUS eq '2'}">
									<tr>
										<th><input class="form-check-input" type="checkbox" id="gridCheck1"></th>
										<td class="col-auto"><input class="form-control" type="text" placeholder="회원 번호" aria-label="default input example" value="${ml.IDX}"></td>
										<td class="col-1"><input class="form-control" type="text" placeholder="아이디" aria-label="default input example" value="${ml.MEM_ID}"></td>
										<td class="col-1"><input class="form-control" type="text" placeholder="이름" aria-label="default input example" value="${ml.MEM_NAME}"></td>
										<td class="col-1"><input class="form-control" type="text" placeholder="닉네임" aria-label="default input example" value="${ml.MEM_NICK}"></td>
										<td class="col-1"><input class="form-control" type="text" placeholder="생년월일" aria-label="default input example" value="${ml.MEM_BIRTHDAY}"></td>
										<td class="col-1">
											<select class="form-select" aria-label="Default select example">
												<option value="1" <c:if test="${ml.MEM_GENDER eq 'M'}">selected</c:if>>남자</option>
												<option value="2" <c:if test="${ml.MEM_GENDER eq 'F'}">selected</c:if>>여자</option>
											</select>
										</td>
										<td>
											<div class="input-group ">
												<input type="text" class="form-control" placeholder="Username" aria-label="Username" value="${ml.MEM_EMAIL1}">
												<span class="input-group-text">@</span>
												<input type="text" class="form-control" placeholder="Server" aria-label="Server" value="${ml.MEM_EMAIL2}">
	                 		  				</div>
	                          		 	</td>
										<td class="col-1"><input class="form-control" type="text" placeholder="연락처" aria-label="default input example" value="${ml.MEM_PHONE}"></td>
										<td class="col-1">
											<select class="form-select " aria-label="Default select example">
												<option value="1" <c:if test="${ml.MEM_STATUS == 1}">selected</c:if>>정상</option>
												<option value="2" <c:if test="${ml.MEM_STATUS == 2}">selected</c:if>>대기</option>
												<option value="3" <c:if test="${ml.MEM_STATUS == 3}">selected</c:if>>휴면</option>
												<option value="4" <c:if test="${ml.MEM_STATUS == 3}">selected</c:if>>탈퇴</option>
											</select>
										</td>
										<td class="d-flex">
											<button type="button" class="btn btn-lg btn-primary row col-auto">승인</button>
											<button type="button" class="btn btn-lg btn-primary row col-auto ms-3">반려</button>
										</td>
	                             	</tr>
	                            </c:if>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
            <!-- Blank End -->
	<%-- 내용 끝 --%>
            
	<%@include file="inc/footer.jsp"%>

	<!-- Back to Top -->
    <a href="#" class="btn btn-lg btn-primary btn-lg-square back-to-top"><i class="bi bi-arrow-up"></i></a>

    <!-- JavaScript Libraries -->
    <script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="resources/admin/lib/chart/chart.min.js"></script>
    <script src="resources/admin/lib/easing/easing.min.js"></script>
    <script src="resources/admin/lib/waypoints/waypoints.min.js"></script>
    <script src="resources/admin/lib/owlcarousel/owl.carousel.min.js"></script>
    <script src="resources/admin/lib/tempusdominus/js/moment.min.js"></script>
    <script src="resources/admin/lib/tempusdominus/js/moment-timezone.min.js"></script>
    <script src="resources/admin/lib/tempusdominus/js/tempusdominus-bootstrap-4.min.js"></script>

    <!-- Template Javascript -->
    <script src="resources/admin/js/main.js"></script>
    <script type="text/javascript">
    		var link = document.location.href;
	    	if (link.includes("member")) {
	    		document.getElementById("member").classList.toggle("active");
	    		document.getElementById("memberIns").classList.toggle("active");
	    	};
    </script>
</body>
</html>