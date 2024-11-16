/*
	장바구니 기능 구현
	-(V) 장바구니 목록 불러오기 : 클래스ID, 클래스명, 강사명, 클래스가격, 썸네일사진(이거는 나중에 구현) 
	-(V) 전체선택 체크 기능 : checkAll, chk(각각의 클래스id)
	-(V) 삭제버튼 기능 : CARTITEM_IDX
	-(V) 체크한 상품에 대한 총계 표출 
	-(V) 선택 삭제 => 장바구니(CART) 테이블에서 딜리트
	-(V) 헤더부분 장바구니 갯수 표시 => top.js에서 작성함
	-(V) 선택 구매 => 선택한 상품 결제페이지로 데이터 넘기기
	-() 썸네일사진 표출 : 클래스 등록 완료되면 구현
	-() 상품 클릭시 상품 상세 페이지 이동() : CourseDetail?CLASS_ID=240108 형식으로 보냈음. 나중에 주미언니 강의 상세 페이지 다되면 구현.
 */
$(document).ready(function() {
	//-------------------------------------------------------------------------
	// "전체선택(checkAll)" 버튼 클릭시 전체 항목 선택/해제 이벤트
	const checkAll = document.querySelector('#checkAll'); //id 속성 가져와서 변수에 저장
	const checkboxes = document.querySelectorAll('.chk'); //name 속성 전체 가져와서 변수에 저장
	//-------------
	const itemCnt = document.querySelector('#itemCount'); //선택상품 갯수 
	const itemTotal = document.querySelector('#totalAmount') //선택상품 금액
	
	//-------------------------------------------------------------------------
	// 1. 초기 동기화 : 페이지가 처음 로드될 때 'checkAll'의 상태에 따라 다른 체크박스들의 상태를 설정
	const isChecked = checkAll.checked; // checkAll이 체크되어 있는지 확인
	// 각 체크박스의 상태를 전체선택 상태와 동일하게 설정하는 for문
//	for(const checkbox of checkboxes) { 
//		checkbox.checked = isChecked; 
//	}
	//=> 람다식으로!! 
	checkboxes.forEach(checkbox => checkbox.checked = isChecked);
		
	// 2. 전체선택 클릭시 이벤트
	checkAll.addEventListener('click', function() {
		let isChecked = checkAll.checked;
//		console.log("전체선택 버튼 클릭함? ", isChecked); 
		checkboxes.forEach(checkbox => checkbox.checked = isChecked);
		
		updateSelect(); //전체선택 후 총합 계산 호출
	});
	
	// 3. 개별 체크박스 클릭 시, 전체선택 상태 업데이트
	// 개별 체크박스 하나라도 체크 해제 시 전체선택 체크 해제 이벤트
	checkboxes.forEach(checkbox => {
		checkbox.addEventListener('click', function() {
			let totalCnt = checkboxes.length; //총 체크박스 갯수
			let checkedCnt = document.querySelectorAll('.chk:checked').length; //체크한 갯수
			
			// 모든 체크박스가 선택되었는지 확인 후 전체선택 상태 업데이트
			checkAll.checked = totalCnt === checkedCnt;
			
			updateSelect(); //개별선택 후 항목에 대한 총합 호출
		});
		
	});	
	
	// 4. 체크한 체크박스의 갯수/금액 계산 업데이트 함수
	function updateSelect() {
		let selectCnt = 0; 
		let selectTotal = 0;
		
		checkboxes.forEach(checkbox => { //체크박스요소들 체크될때마다
	        if (checkbox.checked) {
	            selectCnt++; // 체크된 항목 갯수 증가
	            selectTotal += parseInt(checkbox.dataset.price); // 체크된 항목 금액 누적
	        }
   		});
		
		itemCnt.textContent = selectCnt;
		itemTotal.textContent = selectTotal.toLocaleString(); // 금액에 세자리 콤마 추가
	}
	
	// 페이지 로드시 초기화
    updateSelect();
    
});


//================================================================================
// 'X' 버튼(btn-del 클래스) 클릭시 해당상품 장바구니 테이블에서 삭제 (foreach로 각 상품)
//"삭제하시겠습니까?" 확인 창 출력 후 사용자로부터 "예/아니오" 입력받아 판별
$(document).on("click", ".btn-del", function(e) {
    // 삭제 여부 확인
    if (!confirm("삭제하시겠습니까?")) {
        return;  // "취소"를 클릭하면 아무 작업도 하지 않음
    }
    // "확인"을 클릭하면 삭제 요청을 GET 방식으로 보냄
    const cartItemIdx = $(this).data("cartitem"); //data-cartitem 속성에서 값 가져오기

    // 삭제 요청을 GET 방식으로 보내는 URL로 이동
    window.location.href = "DeleteItem?cartitem_idx=" + cartItemIdx;
    
});

//================================================================================
// '선택삭제' 버튼 클릭시 체크한 상품을 장바구니 테이블에서 삭제 chkDelete()
function chkDelete() {
	// 삭제 확인 메세지
    if (!confirm("선택된 상품을 삭제하시겠습니까?")) {
        return;
    }
	
	// 선택한 체크박스 가져오기 (상수로 선언=const 변하지 않는 값)
	const selectedChk = []; 
	document.querySelectorAll('.chk:checked').forEach(checkbox => {
		selectedChk.push(checkbox.value); // 체크된 항목의 cartitem_idx 값을 배열에 넣기
	});
	
	if(selectedChk.length == 0) {
		alert("선택된 상품이 없습니다.");
		return;
	}
   
    //선택된 항목들을 콤마로 구분된 문자열로 결합
    const cartItemsParam = selectedChk.join(","); 
//    console.log("cartItemsParam : " + cartItemsParam); //cartItemsParam : 8,7,6,3
   	
	//AJAX 요청을 통해 삭제 처리    
    $.ajax({
		type : "GET",
		url : "DeleteItems",
		data : { //넘겨줄 데이터들 작성
			cartitem_idx : cartItemsParam }, //요청 파라미터
		
		success : function(response) {
			alert("선택한 상품이 삭제되었습니다.");
			location.reload();// 삭제 후 페이지 새로 고침
		},
		error : function(jqXHR) {
			console.log("삭제 요청중 오류 발생 : "+ jqXHR);
			alert("삭제에 실패하였습니다. 다시 시도해주세요.");
		}
		
	});
    
}



// 아래 코드는 AJAX JACSON 설정 어쩌고저쩌고때메 안돼서 그냥 form 태그로 구현함
//================================================================================
// "주문하기(btnSubmit)" 클릭시 선택한 상품을 가지고 결제페이지(payment.jsp)로 이동 
// '선택한 상품 구매'는 주문(Purchase) 테이블에 인서트하고, 장바구니(Cart) 테이블에서 삭제가 이루어져야함 (-> 결제완료시 결과값을 결제페이지에 payment에 삽입)
//function orderCart() {
//	//주문할 상품 있는지 판별 여부 팝업창
//	const checkedCnt = document.querySelectorAll('.chk:Checked').length;
//	if(checkedCnt == 0) {
//		alert('주문할 상품이 없습니다.\n상품을 선택해 주세요.');
//		return;
//	}
//	//--------------------------------------------------------
//	// 선택한 상품을 결제 페이지로 넘겨야하는 데이터
//	// : 체크한 장바구니 번호/클래스명/강사명/클래스가격, itemCount(주문갯수), totalAmount(주문금액)
//	let selectedChk = []; //주문 정보
//	let itemCount = 0; //주문 갯수
//	let totalAmount = 0; //주문 금액
//	
//	//선택된 상품들 반복하여 처리
//	document.querySelectorAll('.chk:checked').forEach(checkbox => {
//		let classIdx = checkbox.value; //체크박스 idx
//		let classTitle = checkbox.dataset.classTitle; //클래스명
//		let teacherName = checkbox.dataset.teacherName; //강사명
//		let classPrice = parseInt(checkbox.dataset.price,10); //클래스가격(10진법)
//		 
//		//선택된 상품 정보들 배열에 추가
//		selectedChk.push({
//			classIdx : classIdx,
//			classTitle : classTitle,
//			teacherName : teacherName,
//			classPrice : classPrice
//		});
//		 
//		//주문 총합 계산
//		totalAmount += classPrice;
//		itemCount++;
//	});
//	console.log("주문 상품정보 : " + selectedChk); //주문 상품정보 : [object Object],[object Object]
//	console.log("주문 금액 : " + totalAmount);
//	console.log("주문 갯수 : " + itemCount);
//	
//	
//	//AJAX 요청 보내기 
//	$.ajax({
//		type: "POST",
//		url: "Payment",
//		data: JSON.stringify({ //전송할 데이터들
//			purchaseItems : selectedChk,
//			totalAmount : totalAmount,
//			itemCount : itemCount
//		}),
//		contentType: "application/json; charset=UTF-8",
//		
//		success: function(response) {
//			window.location.href = 'Payment'; //Payment 매핑주소 포워딩 해야함 (controller)
//		},
//		error: function(jqXHR, textStatus, errorThrown) {
//		// jqXHR: 서버로부터 받은 응답 객체
//        // textStatus: 상태 메시지
//        // errorThrown: 발생한 예외 메시지
//        console.log("Error occurred: ", textStatus, errorThrown); // 오류 메시지 확인
//        console.log("jqXHR responseText: ", jqXHR.responseText); // 응답 텍스트 확인
//        console.log("jqXHR status: ", jqXHR.status); // 상태 코드 확인
//        console.log("jqXHR responseJSON: ", jqXHR.responseJSON); // JSON 응답 확인 (만약 서버에서 JSON을 보냈다면)
//
//        alert("주문 처리 중 오류가 발생했습니다. 콘솔을 확인해주세요.");
//		}
//	});
//	
//}



