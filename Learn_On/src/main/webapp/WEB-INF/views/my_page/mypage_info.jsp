<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>런 온 - 온라인 No.1 교육 플랫폼</title>
<link rel="SHORTCUT ICON" href="${pageContext.request.contextPath}/resources/images/favicon.ico">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/reset.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/common.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/mypage.css">
<script src="${pageContext.request.contextPath}/resources/js/jquery-3.7.1.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/join.js"></script>
    

</head>
<body>
	<header>
		<jsp:include page="/WEB-INF/views/inc/top.jsp"></jsp:include>
	</header>
	<main>
		<!-- 계정설정 -->
		<h2 class="page-ttl">마이페이지</h2>
		<section class="my-wrap">
			<aside class="my-menu">
				<a href="MyInfo" class="active">계정정보</a>
				<a href="MyFav">관심목록</a>
				<a href="MyDashboard">나의 강의실</a>
				<a href="MyReview">작성한 수강평</a>
				<a href="MyPayment">결제내역</a>
				<a href="MyCoupon">보유한 쿠폰</a>
				<a href="MySupport">문의내역</a>
				<a href="MyAttendance">출석체크</a>
			</aside>
			<div class="my-container">
				<div class="contents-ttl">계정설정</div>
				<div class="contents">
					<!-- contents -->
					<section class="info-set-wrap">
						<form action="MyInfo" name="MemberInfo"class="my-frm" method="post" enctype="multipart/form-data">
							<div class="set">
<!-- 								<label>프로필 변경</label> -->
<!-- 								<div> -->
<%-- 									<img src="${pageContext.request.contextPath}/resources/images/profile_thumb.svg" class="profile-thumb" alt="profile" id="preview_profile" height="100px"> --%>
<!-- 									<input type="file"class="btn-frm" value="프로필변경" id="profile_img" name="profile_img"> -->
<!-- 								</div> -->
							</div>
							<div class="set">
								<label>
									이름
								</label>
								<div>
									<input type="text" name="mem_name" id="name" value="${member.mem_name}" readonly>
								</div>
							</div>
							<div class="set">
								<label>
									아이디
								</label>
								<div>
									<input type="text" name="mem_id" id="id" value="${member.mem_id}" readonly>
								</div>
							</div>
							<div class="set">
								<label>닉네임</label>
								<div id="checkNic"></div>
								<div>
									<input type="text" name="mem_nick" id="mem_nick" value="${member.mem_nick }" onblur="ckNick()">
								</div>
							</div>
							<div class="set">
								<label>선호 해시태그</label>
								<div>
									<input type="text" name="hashtag" id="hashtag" value="${member.hashtag }">
								</div>
							</div>
							<div class="set">
								<label>비밀번호 <span>*</span></label>
								<div>
									<input type="password" name="oldPasswd" required>
								</div>
							</div>
							<div class="set">
								<label>변경할 비밀번호</label>
								<div id="checkPasswd1"></div>
								<div>
<!-- 									<input type="password" id="mem_passwd1" name="mem_passwd" onblur="checkPasswdLength1()" placeholder="변경할 비밀번호 8 ~ 16글자 사이 입력"> -->
									<input type="password" id="mem_passwd1" name="mem_passwd" placeholder="새비밀번호 입력">
								</div>
							</div>
							<div class="set">	
								<label>변경할 비밀번호 재입력</label>
								<div id="checkPasswd2"></div>
								<div>
									<input type="password" id="mem_passwd2" onblur="checkPasswdResult()" placeholder="새비밀번호 재입력">
								</div>
							</div>
							<div class="set">
								<label>주소</label>
								<div>
									<input type="text" id="mem_address1" name="mem_address1" size="25" value="${member.mem_address1 }"readonly>
									<input type="button" value="주소검색" onclick="search_address()" class="btn-frm"><br>
								</div>
								<div>
									<input type="text" id="mem_address2" name="mem_address2" value="${member.mem_address2 }" size="25" placeholder="상세주소" >
									<input type="text" id="mem_post_code" name="mem_post_code" size="6" value="${member.mem_post_code }" readonly><br>
								</div>
							</div>
							<div class="set">
								<label>이메일 </label>
								<div>
									<input type="text" size="15" name="mem_email1" id="mem_email1" placeholder="${member.mem_email}" readonly>
<!-- 									<select id="emailDomain" class="sel-frm"> -->
<!-- 										<option value="">직접입력</option> -->
<!-- 										<option value="naver.com">naver.com</option> -->
<!-- 										<option value="nate.com">nate.com</option> -->
<!-- 										<option value="gmail.com">gmail.com</option> -->
<!-- 									</select> -->
								</div>
							</div>
							<div class="set">
								<label>전화번호</label>
								<div id="phoneCheckResult"></div>
								<div>
									<input type="text" size="10" id="mem_phone" value="${member.mem_phone}" name="mem_phone" placeholder="'-'제외 후 입력해주세요" onblur="phoneCheck()">
								</div>
							</div>
							<div class="btns">
								<input type="submit" class="btn-submit" value="수정">
							</div>
						</form>
						<a href="MemberWithdraw" class="withdraw-link">회원탈퇴</a>
					</section>
					<!-- // contents -->
				</div>
			</div>
		</section>
	</main>
	<footer>
		<jsp:include page="/WEB-INF/views/inc/bottom.jsp"></jsp:include>
	</footer>
	<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
	<script type="text/javascript">
		function search_address() {
			new daum.Postcode({
				oncomplete : function(data) {
					console.log(data);
					document.MemberInfo.mem_post_code.value = data.zonecode;

					let address = data.address;
					if (data.buildingName != "") {
						address += " (" + data.buildingName + ")";
					}

					document.MemberInfo.mem_address1.value = address;

					document.MemberInfo.mem_address2.focus();

				}
			}).open();
		}
	</script>
	
	<script type="text/javascript">
    $("#profile_img").change(function (event) {
        let file = event.target.files[0]; // 사용자가 업로드한 파일 가져오기
        let reader = new FileReader();

        reader.onload = function (event2) {
            console.log("파일 : " + event2.target.result); // 파일 내용 확인용 로그
            $("#preview_profile").attr("src", event2.target.result); // 미리보기 이미지 변경
        };

        // 파일을 URL로 읽어오기
        if (file) {
            reader.readAsDataURL(file);
        }
    });
</script>

	
</body>
</html>