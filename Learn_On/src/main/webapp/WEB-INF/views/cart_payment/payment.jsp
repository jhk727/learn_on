<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>결제 - LearOn</title>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/reset.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/common.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css">
<script src="${pageContext.request.contextPath}/resources/js/jquery-3.7.1.js"></script>

<!-- page 개별 CSS -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/payment.css">
<!-- page 개별 JS -->
<script src="${pageContext.request.contextPath}/resources/js/cart_payment/payment.js"></script>
<!-- 포트원 결제api sdk 추가 -->
<script src="https://cdn.iamport.kr/v1/iamport.js"></script>

</head>
<body>
	<header>
		<jsp:include page="/WEB-INF/views/inc/top.jsp"></jsp:include>
	</header>
	
	<!----------------------------- page 영역 --------------------------- -->
	<main id="pay">
		<form action="Portone" method="post" id="payForm">
		
			<div class="wrapper">
				<!-- pay-wrap start  -->
				<div class="pay-wrap">
					<h2 class="pay-ttl">
						<i class="fa-solid fa-circle-right" ></i>
					  	 결제하기
					  </h2>
					<div class="frame">
						<!----------------- 결제 상품 내역 ----------------->
						<section class="pay-list">
							<div class="pay-item">
								<h5 class="box-ttl">주문상품</h5>
								
								<c:forEach var="item" items="${selectedCartList}">
									<div class="class-box">
										<div class="class-pic">
											<img alt="클래스썸네일" src="/resources/images/thumb_01.webp">
										</div>
										<div class="item-info">
											<p id="classTitle">${item.classTitle}</p>
											<p id="teacherName">${item.teacherName}</p>
										</div>
									</div>
									<!-- 상품 금액부분 -->
									<div class="item-result">
										<span class="price">
											<fmt:formatNumber value="${item.classPrice}" type="number" />
										</span>원
									</div>
								</c:forEach>
							</div>
						</section>
						
						<!-- ----------------- 쿠폰 ---------------->
						<section class="pay-right">
							<div class="pay-item">
								<h5 class="box-ttl">쿠폰</h5>
								<div class="coupon">
									<div class="coupon-select">
										<div class="coupon-select-info">
											<p>쿠폰 할인 금액</p>
											<span class="coupon-price">${coupon.totalDiscount}원</span>
										</div>
										<input type="button" value="쿠폰선택" class="coupon-btn" onclick="couponSelect()">
									</div>
									<div class="coupon-input">
										<input type="text" placeholder="쿠폰 코드를 입력해주세요." class="coupon-inputbox" name="couponCode">
										<input type="button" value="쿠폰발급" class="coupon-btn" onclick="couponCreate()">
									</div>
								</div>
							</div>
						</section>
						
						
						<!-- ----------------- 결제 금액 ---------------->
						<section class="pay-right">
							<div class="price-box">
								<h5 class="box-ttl">결제 금액</h5>
								<dl>
									<dt>결제 상품 금액</dt>
									<c:set var="totalAmount" value="0" />
									<c:forEach var="cart" items="${selectedCartList}">
									    <c:set var="totalAmount" value="${totalAmount + cart.classPrice}" />
									</c:forEach>
									<dd><fmt:formatNumber value="${totalAmount}" type="number" /> 원</dd>
								</dl>
								<dl>
									<dt>할인 금액</dt>
									<dd><fmt:formatNumber value="${discountAmount}" type="number" /> 원</dd>
								</dl>
								<dl class="total">
									<dt>결제 금액</dt>
									<c:set var="payAmount" value="${totalAmount - discountAmount}" />
									<dd><fmt:formatNumber value="${payAmount}" type="number" /> 원</dd>
								</dl>
							</div>
						</section>
						
						<!-- ----------------- 결제수단 ---------------->
						<section class="pay-right">
							<div class="pay-item">
								<h5 class="box-ttl">결제수단</h5>
								<label class="pay-method">
									<input type="radio" name="pay-method" value="card">
									<span>신용카드</span>
								</label>
								<label class="pay-method">
									<input type="radio" name="pay-method" value="bank">
									<span>무통장입금(가상계좌)</span>
								</label>
							</div>
						</section>
							
						<!-- ----------------- 이용약관 동의(필수) ---------------->
						<section class="pay-right">
							<div class="pay-item">
								<div class="notice-box">
									<label class="notice-check">
										<input type="checkbox" id="notice" name="" value="">	
						 				<span>이용약관 동의(필수)</span>
									</label>
					 				<a href="Terms">내용보기</a>
								</div>
							</div>
						</section>	
						<!-- ----------------- 결제하기 버튼 ---------------->
						<div class="btns-box">
							<input type="submit" value="결제하기" class="btnSubmit" onclick="requestPay()">
						</div>
					</div>
				</div>
				<!-- // pay-wrap end  -->
				
			</div>
		</form>
	</main>

	
	<!----------------------------- page 영역 --------------------------- -->
	<footer>
		<jsp:include page="/WEB-INF/views/inc/bottom.jsp"></jsp:include>
	</footer>


</body>
</html>