
//****************생년월일****************
$(document).ready(function() {
	
		for (let i = 2000; i > 1980; i--) {
			$('#year').append('<option value="' + i + '">' + i + '</option>');
		}
		for (let i = 1; i < 13; i++) {
			$('#month').append('<option value="' + i + '">' + i + '</option>');
		}
		for (let i = 0; i < 32; i++){
			$('#day').append('<option value ="' + i + '">' + i + '</option>');
		}
	}
	);

// *********이메일 셀랙트박스**********
$(document).ready(function() {
//	$("#submit").click(submit);
	$("#emaildmain").change(function() {
		$("#mem_email2").val($("#emaildmain").val());
	});
});




// *************유효성 검사**************
// ************* 검사**************

let checkName = false;
let checkPasswd1 = false;
let checkPasswd2 = false;
let checkNumber = false;
let checkAddr = false;
let checkMail = false;
let checkCode = false;
let checkIdResult = false;
let checkNic = false;
let isIdValid=false;
let phoneCheckResult = false;

// ************* 이름 null 검사**************
function checkNameLength(){
	let name = $("#mem_name").val();
	if(name == "" ){
		$("#checkName").text("이름을 입력해주세요.");	
		$("#checkName").css("color","red");	
		checkName=false;	
	}else {
		$("#checkName").text("");
		checkName=true;	
	}
}

// ************* 아이디 중복체크/길이검사**************
function checkId(){
	let id = $("#mem_id").val();
	let regex = /^[\d\w][\d\w_]{3,12}$/;
//	let regex = /^(?=.*[!@#$%^&*].*[!@#$%^&*])[\d\w!@#$%^&*]{4,12}$/;
	if(regex.exec(id)){
		$.ajax({
			type : "get" ,
		   	 url : "MemberCheckId" ,
		   	data : {
			mem_id:id
		} ,
			success : function(result){
//				debugger;
				if(result.trim() == "false"){
					$("#checkIdResult").text("사용가능한 아이디 입니다.").css("color","GREEN");
					isIdValid = true;
				}else{
					$("#checkIdResult").text("이미 존재하는 아이디 입니다.").css("color","RED");
					alert("아이디를 다시 확인해주세요");
					isIdValid = false;
				}
			} 
		});
	}else{
		$("#checkIdResult").text("4~12글자만 사용가능");
		$("#checkIdResult").css("color", "red");
		isIdValid = false;
	}
}
 		
/**************닉네임 중복검사************* */

function ckNick(){
	let nick = $("#mem_nick").val();
	let regex = /^[\w가-힣]{2,8}$/;
		if(regex.exec(nick)){
			$.ajax({
				type : "get",
				url : "MemberCheckNick",
				data : {
					mem_nick : nick
				},
				success : function(result){
					if(result.trim() == "false" ){
						$("#checkNic").text("사용가능한 닉네임 입니다").css("color","GREEN");
						checkNic = true;
					}else {
						$("#checkNic").text("이미 사용중인 닉네임 입니다").css("color","RED");
						alert("닉네임을 다시 확인해주세요");
						checkNic = false;
					
					}
				} ,
			error : function(){
//				alert("이미 사용중인 닉네임 입니다\n다시 입력해주세요")
			}
		});
	}else{
		$("#checkNic").text("4~12글자만 사용가능");
		$("#checkNic").css("color", "red");
		checkNic = false;
	}
}


//function checkIdLength (){
//	let id = $("#MEM_ID").val();
//	if(id.length >= 4 && id.length <= 8){
//		$("#checkId").text("사용가능한 아이디입니다.");
//		$("#checkId").css("color","green");
//		checkId = true;
//	}else if(id == ""){
//		$("#checkId").text("아이디를 입력해주세요");
//		$("#checkId").css("color","red");
//		checkId = false;
//	}else {
//		$("#checkId").text("4~8 글자만 사용 가능합니다");
//		$("#checkId").css("color","red");
//		checkId = false;
//	}
//}

// ************* 비밀번호 길이검사**************
function checkPasswdLength1() {
    let passwd = $("#mem_passwd1").val();
    let regex = /^(?=.*[\d])(?=.*[!@#$%^&*])[a-z\d!@#$%^&*]{8,}$/;

    if (regex.test(passwd)) {
        $("#checkPasswd1").text("사용가능한 비밀번호 입니다");
        $("#checkPasswd1").css("color", "green");
        checkPasswd1 = true;
    } else if (passwd === "") {
        $("#checkPasswd1").text("비밀번호를 입력해주세요.");
        $("#checkPasswd1").css("color", "red");
        checkPasswd1 = false;
    } else {
        // 조건이 맞지 않는 경우
        $("#checkPasswd1").text("8자리 이상,숫자/특수문자 중 2종류 포함 해주세요");
        $("#checkPasswd1").css("color", "red");
        alert("비밀번호를 확인해주세요");
        checkPasswd1 = false;
    }
}

// ************* 비밀번호 같은지 검사**************

function checkPasswdResult(){
	let passwd1 = $("#mem_passwd1").val();
	let passwd2 = $("#mem_passwd2").val();
	if(passwd1 == ""){
		$("#checkPasswd2").text("비밀번호를 입력해주세요.");
		$("#checkPasswd2").css("color","red");
	}else if(passwd1 == passwd2){
		$("#checkPasswd2").text("비밀번호가 일치합니다.");
		$("#checkPasswd2").css("color","green");
	}else {
		$("#checkPasswd2").text("비밀번호가 일치하지 않습니다.");
		$("#checkPasswd2").css("color","red");
	}
}

/**********전화전호 유효성 검사********* */
function phoneCheck(){
	let phone = $("#mem_phone").val();
	let regex = /^[0-9]{11}$/;
	
	if (regex.test(phone)) {
		$("#phoneCheckResult").text("");
		phoneCheckResult = true;
	}else {
        $("#phoneCheckResult").css("color", "red");
		$("#phoneCheckResult").text("전화번호가 올바르지 않습니다.");
		phoneCheckResult = false;
	}
}
// *************이용약관 체크**************
$(document).ready(function() {
$("#terms_all").click(function() {
		// 전체선택을 제외한 나머지 체크박스에 대한 반복 수행
		$("input[name=terms]").each(function(index, item) {
			// 전체선택 체크박스 체크상태값을 각 체크박스 체크상태값으로 설정
			$(item).prop("checked", $("#terms_all").prop("checked"));
		});
		
	});
});
// **************프로필 미리보기*************
$("#profile_img").change(function (event) {
    let file = event.target.files[0];
    let reader = new FileReader();

    reader.onload = function (event2) {
        console.log("파일: " + event2.target.result);
        $("#preview_profile").attr("src", event2.target.result);
    };

    if (file) {
        reader.readAsDataURL(file);
    }
});





/******************* */
function submit() {
	if (!isIdValid) {
		$("#mem_id").focus();
		return false;
	} else if(!checkNic){
		$("#mem_nick").focus();
		return false;
	} else if(!checkPasswd1) {
		$("#mem_passwd").focus();
		return false;
	}else {
		return true;
	}

}