<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
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
<script src="${pageContext.request.contextPath}/resources/js/jquery-3.7.1.js"></script>
</head>
<body>
	<header>
		<jsp:include page="/WEB-INF/views/inc/top.jsp"></jsp:include>
	</header>
	<main>
		<div class="wrapper">
			<div class="cls-wrap">
				<div class="cls-cate">
					<h1 class="cls-ttl">${codeType[0].codename}</h1>
					<div class="cate-li">
						<button <c:if test="${param.codetype eq codeType[0].codetype && param.codetype_id eq null}">class="on"</c:if>>
							<a href="Category?codetype=${codeType[0].codetype}" >전체</a>
						</button>
						<c:forEach var="category" items="${requestScope.codeType}" varStatus="status">
 							<button <c:if test="${param.codetype_id eq category.codetype_id}">class="on"</c:if>> 
								 <a href="Category?codetype=${category.codetype}&codetype_id=${category.codetype_id}">${category.name}</a>
							</button>
						</c:forEach>
						
						<form action="Category">
							<select name="searchType">
								<option value="new" <c:if test="${param.searchType eq 'new'}">selected</c:if>>최신순</option>
								<option value="title" <c:if test="${param.searchType eq 'title'}">selected</c:if>>제목순</option>
								<option value="price" <c:if test="${param.searchType eq 'price'}">selected</c:if>>가격순</option>
								<option value="score" <c:if test="${param.searchType eq 'score'}">selected</c:if>>평점순</option>
							</select>
							<input type="hidden" name="codetype" value="${codeType[0].codetype }"/>
							<input type="submit" value="검색" />
						</form>
						<script>
							function handleChange(event) {
								let form = document.searchType;
						        form.method = "get";
						        form.submit();
							}
						</script>
					</div><!-- cate-li -->
				</div><!-- clas-cate -->
				<div class="course-wrap">
				<c:choose>
					<c:when test="${empty requestScope.courseList}">
						<div class="empty">강의를 준비중이에요 😊</div>
					</c:when>
					<c:otherwise>
						<ul class="course-card">
							<c:forEach var="course" items="${requestScope.courseList}" varStatus="status">
								<li id="${course.class_id}">
									<button class="fav-off" style="display:block;" onclick="addToWishList('${course.class_id}')">
										<i class="fa-regular fa-heart"></i>
									</button>
									<button class="fav-on" style="display:none;" onclick="deleteToWishList('${course.class_id}')">
										<i class="fa-solid fa-heart"></i>
									</button>
									<a href="CourseDetail?class_id=${course.class_id}&codetype=${codeType[0].codetype}">
										<img src="${pageContext.request.contextPath}/resources/upload/${course.class_pic1}" class="card-thumb" alt="thumbnail" />
<%-- 										<img src="resources/upload/${course.class_pic1}" class="card-thumb" alt="thumbnail" /> --%>
										<div class="card-info">
											<div class="category">
												<span>${course.catename}</span>
											</div>
											<div class="ttl">${course.class_title}</div>
											<div class="price">
												 <fmt:formatNumber pattern="#,###">${course.class_price}</fmt:formatNumber>원
											</div>
											<div class="rating">
												<i class="fa-solid fa-star"></i>
												<span>${course.review_score}</span>
											</div>
											<div class="name">${course.mem_id}</div>
										</div>
									</a>
								</li>
							</c:forEach>
							
							<script>
								window.onload = function() {
									const wishList = ${wishList};
									wishList.forEach(wish => {
										const listItem = document.getElementById(wish.CLASS_ID);
										if (listItem) {
							                const favOnBtn = listItem.querySelector(".fav-on");
							                if (favOnBtn) {
							                	favOnBtn.style.display = "block";
							                }
		
							                const favOffBtn = listItem.querySelector(".fav-off");
							                if (favOffBtn) {
							                	favOffBtn.style.display = "none";
							                }
							            }
									});
								}
								function addToWishList(id){
									if(confirm("관심목록에 추가하시겠습니까?")) {
										location.href="MyFavAdd?class_id=" + id;
									}
								}
								function deleteToWishList(id){
									if(confirm("관심목록에서 삭제하시겠습니까?")){
										location.href="MyFavDel?class_id=" + id;
									}
								}
							</script>									
						</ul>
					</c:otherwise>
				</c:choose>
				<c:set var="pageNum" value="1"/>
	            <c:if test="${not empty param.pageNum}">
					<c:set var="pageNum" value="${param.pageNum}"/>
				</c:if>
				<c:choose>
					<c:when test="${not empty requestScope.courseList}">
						<section id="pageList">	<!-- 페이징 처리 시작 -->
							<input type="button" value="&lt;&lt;" 
								onclick="location.href='Category?codetype=${codeType[0].codetype}&pageNum=${pageInfo.startPage - pageInfo.pageListLimit}'"				
								<c:if test="${pageInfo.startPage == 1}">disabled</c:if> 	
							>
							<input type="button" value="&lt;" 
								onclick="location.href='Category?codetype=${codeType[0].codetype}&pageNum=${pageNum - 1}'"
								<c:if test="${pageNum == 1}">disabled</c:if> 	
							>
							
							<c:forEach var="i" begin="${pageInfo.startPage}" end="${pageInfo.endPage}">
								<c:choose>
									<c:when test="${i eq pageNum}">
										<strong>${i}</strong>
									</c:when>
									<c:otherwise>
										<a href="Category?codetype=${codeType[0].codetype}&pageNum=${i}">${i}</a>
									</c:otherwise>
								</c:choose>
							</c:forEach>
							
							<input type="button" value="&gt;" 
								onclick="location.href='Category?codetype=${codeType[0].codetype}&pageNum=${pageNum + 1}'"
								<c:if test="${pageNum == pageInfo.maxPage}">disabled</c:if> 		
							>
							<input type="button" value="&gt;&gt;" 
								onclick="location.href='Category?codetype=${codeType[0].codetype}&pageNum=${pageInfo.startPage + pageInfo.pageListLimit}'"
								<c:if test="${pageInfo.endPage == pageInfo.maxPage}">disabled</c:if>	
							>			
							</section><!-- 페이징 처리 끝 -->
					</c:when>
				</c:choose>
				
				</div><!-- course-wrap -->
			</div><!-- cls-wrap -->
		</div><!-- wrapper -->
	</main>
	<footer>
		<jsp:include page="/WEB-INF/views/inc/bottom.jsp"></jsp:include>
	</footer>
</body>
</html>