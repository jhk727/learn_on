<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
    <title>런 온 - 관리자 페이지</title>
	<link rel="SHORTCUT ICON" href="${pageContext.request.contextPath}/resources/images/favicon.ico">
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
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" rel="stylesheet">
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
						<h5 class="me-auto tableSubject">쿠폰 발급</h5>
					</div>
					<form class="d-flex input-group mb-3" method="get">
						<select class="form-select" name= "searchType" aria-label="Default select example">
							<option value="subject" <c:if test="${param.searchType eq 'subject'}">selected</c:if>>쿠폰이름</option>
							<option value="code" <c:if test="${param.searchType eq 'code'}">selected</c:if>>쿠폰코드</option>
						</select>
						<input type="text" class="form-control" name="searchKeyword" placeholder="쿠폰 제목 검색" aria-label="Recipient's username" aria-describedby="button-addon2">
						<button class="btn btn-primary" type="submit" id="button-addon2">검색</button>
					</form>
					<div class="couponIssueContainer">
						<div class="couponIssueBox">
							<table class="table table-striped">
								<colgroup>
									<col width="10%">
									<col width="30%">
									<col width="20%">
									<col width="20%">
									<col width="20%">
								</colgroup>
								<thead>
									<tr>
										<th scope="col">쿠폰 번호</th>
										<th scope="col">쿠폰 이름</th>
										<th scope="col">쿠폰 할인률</th>
										<th scope="col">쿠폰 유효기간</th>
										<th scope="col">쿠폰 상태</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${couponList}" var="couponBoard" varStatus="status">
										<tr onclick="showMembers(${status.index})">
											<td><input id="couponIdx_${status.index}" class="form-control couponIdx coupon" type="text" aria-label="default input example" value="${couponBoard.coupon_id}" readonly></td>
											<td><input id="couponName_${status.index}" class="form-control couponName coupon" type="text" aria-label="default input example" value="${couponBoard.coupon_name}" readonly></td>
											<td><input class="form-control coupon" type="text" aria-label="default input example"
												<c:choose>
													<c:when test="${couponBoard.discount_type eq 1}">value="-${couponBoard.discount_percent}%"</c:when>
													<c:when test="${couponBoard.discount_type eq 2}">value="-${couponBoard.discount_amount}원"</c:when>
												</c:choose> readonly></td>
											<td><input class="form-control coupon" type="text" aria-label="default input example" value="${couponBoard.c_expiry_date}" readonly></td>
											<td><input class="form-control coupon" type="text" aria-label="default input example"
												<c:choose>
													<c:when test="${couponBoard.coupon_status eq 1}">value="사용가능"</c:when>
													<c:when test="${couponBoard.coupon_status eq 2}">value="사용불가"</c:when>
												</c:choose> readonly></td>
	                            		</tr>
	                              	</c:forEach>
								</tbody>
							</table>
						</div>
						<form action="AdmCouponIssue" class="couponMemberForm d-flex mb-3" method="post">
							<div class="couponIssueMem">
								<div class="couponMember-btn">
									<input class="couponIdHidden" type="text" name="coupon_id" hidden>
									<button type="submit" class="btn btn-lg btn-primary">쿠폰 발급</button>
								</div>
								<table class="table table-striped-columns">
									<colgroup>
										<col width="20%">
										<col width="40%">
										<col width="40%">
									</colgroup>
									<tr>
										<th scope="col">쿠폰 번호</th>
										<th scope="col" colspan="2">쿠폰 이름</th>
									</tr>
									<tr>
										<th><input class="form-control couponIssueIdx coupon" type="text" aria-label="default input example" readonly></th>
										<td colspan="2"><input class="form-control couponIssueName coupon" type="text" aria-label="default input example" readonly></td>
									</tr>
									<tr>
										<th><input id="couponMemberCheckAll" class="form-check-input" type="checkbox" value="${couponBoard.coupon_id}"></th>
										<th class="couponMemberTitle">아이디</th>
										<th class="couponMemberTitle">이름</th>
									</tr>
								</table>
							</div>
						</form>
					</div>
						<section id="pagingArea">
							<button
							onclick="location.href='AdmPayListCoupon?pageNum=${pageInfo.startPage - pageInfo.pageListLimit}&searchType=${searchType}&searchKeyword=${searchKeyword}'"
							<c:if test="${pageInfo.startPage eq 1}">disabled</c:if>>
								<i class="fa-solid fa-angles-left"></i>
							</button>
							<button
							onclick="location.href='AdmPayListCoupon?pageNum=${pageNum - 1}&searchType=${param.searchType}&searchKeyword=${param.searchKeyword}'"
							<c:if test="${pageNum eq 1}">disabled</c:if>>
								<i class="fa-solid fa-angle-left"></i>
							</button>
							<c:forEach var="i" begin="${pageInfo.startPage}" end="${pageInfo.endPage}">
								<c:choose>
									<c:when test="${i eq pageNum}">
										<strong>${i}</strong>
									</c:when>
									<c:otherwise>
										<a href="AdmPayListCoupon?pageNum=${i}&searchType=${param.searchType}&searchKeyword=${param.searchKeyword}">${i}</a>
									</c:otherwise>
								</c:choose>
							</c:forEach>
							<button
							onclick="location.href='AdmPayListCoupon?pageNum=${pageNum + 1}&searchType=${param.searchType}&searchKeyword=${param.searchKeyword}'"
							<c:if test="${pageNum eq pageInfo.maxPage}">disabled</c:if>>
								<i class="fa-solid fa-angle-right"></i>
							</button>
						   	<button
						   	onclick="location.href='AdmPayListCoupon?pageNum=${pageInfo.startPage + pageInfo.pageListLimit}&searchType=${param.searchType}&searchKeyword=${param.searchKeyword}'"
							<c:if test="${pageInfo.endPage eq pageInfo.maxPage}">disabled</c:if>>
						   		<i class="fa-solid fa-angles-right"></i>
						   	</button>
					   	</section>
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
    <script src="resources/admin/js/coupon_list.js"></script>

    <!-- Template Javascript -->
    <script src="resources/admin/js/main.js"></script>
    <script type="text/javascript">
    	if (performance.navigation.type === 1) {
			location.href= "AdmCouponIssue";
		}
    	
    	// 메뉴 활성화
		let link = document.location.href;
    	if (link.includes("AdmCouponIssue")) {
    		document.querySelector("#CouponIssue").parentElement.previousElementSibling.classList.add("show");
    		document.querySelector("#CouponIssue").parentElement.previousElementSibling.classList.add("active");
    		document.querySelector("#CouponIssue").parentElement.classList.add("show");
    		document.querySelector("#CouponIssue").classList.toggle("active");
    	};
    </script>
</body>
</html>