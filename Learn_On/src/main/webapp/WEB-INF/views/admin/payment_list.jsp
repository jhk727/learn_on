<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
						<h5 class="me-auto tableSubject">결제 내역 관리</h5>
						<button type="button" class="btn btn-lg btn-primary ms-3 col-auto">결제 내역 삭제</button>
					</div>
					<div class="d-flex input-group mb-3">
						<input type="text" class="form-control" placeholder="구매자 이름 검색" aria-label="Recipient's username" aria-describedby="button-addon2">
						<button class="btn btn-primary" type="button" id="button-addon2">검색</button>
					</div>
						<table class="table table-striped">
							<thead>
								<tr>
									<th scope="col">#</th>
									<th scope="col">구매 일시</th>
									<th scope="col">구매 코드</th>
									<th scope="col">구매자</th>
									<th scope="col">금액</th>
									<th scope="col">결제 수단</th>
									<th scope="col">상태</th>
									<th scope="col"></th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<th><input class="form-check-input" type="checkbox" id="gridCheck1"></th>
									<td><h6 class="form-control" aria-label="default input example">2024-11-01</h6></td>
									<td><h6 class="form-control" aria-label="default input example">INV-0123</h6></td>
									<td><h6 class="form-control" aria-label="default input example">홍길동</h6></td>
									<td><h6 class="form-control" aria-label="default input example">￦ 80,000</h6></td>
									<td><h6 class="form-control" aria-label="default input example">카드 결제</h6></td>
									<td><h6 class="form-control" aria-label="default input example">결제 완료</h6></td>
									<td><button type="button" class="btn btn-lg btn-primary col-auto">상세 정보</button></td>
                             	</tr>
								<tr>
									<th><input class="form-check-input" type="checkbox" id="gridCheck1"></th>
									<td><h6 class="form-control" aria-label="default input example">2024-11-01</h6></td>
									<td><h6 class="form-control" aria-label="default input example">INV-0124</h6></td>
									<td><h6 class="form-control" aria-label="default input example">강감찬</h6></td>
									<td><h6 class="form-control" aria-label="default input example">￦ 120,000</h6></td>
									<td><h6 class="form-control" aria-label="default input example">무통장 결제</h6></td>
									<td><h6 class="form-control" aria-label="default input example">결제 대기중</h6></td>
									<td><button type="button" class="btn btn-lg btn-primary col-auto">상세 정보</button></td>
                             	</tr>
							</tbody>
						</table>
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
	    	if (link.includes("Adm")) {
	    		document.getElementById("payment").classList.toggle("active");
	    		document.getElementById("paymentList").classList.toggle("active");
	    	};
    </script>
</body>
</html>