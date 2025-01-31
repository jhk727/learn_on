package com.itwillbs.learnon.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.learnon.service.CouponService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
public class CouponController {
	@Autowired
	private CouponService couponService;
	
	//=================================================================================
	// "쿠폰선택" 클릭시 보유한 쿠폰 목록 조회
	@GetMapping("myCouponList")
	public String couponSelect(
			HttpSession session, Model model) {
		//------------------------------------------------------
		// 로그인 정보 가져오기 (세션 아이디값 확인)
		String sId = (String) session.getAttribute("sId");
//		log.info("마이쿠폰 로그인 아이디: " + sId);
		//------------------------------------------------------
		//CouponService - getCoupon() 메서드 호출하여 쿠폰 조회 요청
		List<Map<String, Object>> coupon = couponService.getCoupon(sId);
		
		//CouponService - getCouponCount() 메서드 호출하여 쿠폰 갯수 조회 요청
		int couponCount = couponService.getCouponCount(sId);
		
//		log.info(coupon); // [{MEM_ID=bborara, COUPON_STATUS=1, COUPON_ISUSED=1, COUPON_NAME=전회원 5000원 할인 쿠폰, C_EXPIRY_DATE=20251231, DISCOUNT_AMOUNT=5000, ISSUE_DATE=2024-11-19 11:02:51, COUPON_CODE=DISC5000, COUPON_ID=1},]
//		log.info(couponCount); //3
		
		//------------------------------------------
		// 리턴받은 쿠폰 데이터 뷰페이지로 전달하기 위해 model에 저장
        model.addAttribute("coupon", coupon);
        model.addAttribute("couponCount", couponCount);

        // 쿠폰 페이지(coupon.jsp)로 포워딩
        return "cart_payment/mycoupon";
	}
	
	
	//=================================================================================
	// "쿠폰발급" 클릭시 입력한 쿠폰코드 확인 후 등록 - AJAX
	@ResponseBody
	@GetMapping(value = "CouponCreate", produces = "application/text; charset=UTF-8")
	public String couponCreate(@RequestParam String couponCode, HttpSession session) {
		//------------------------------------------------------
		// 로그인 정보 가져오기 (세션 아이디값 확인)
		String sId = (String) session.getAttribute("sId");
//		log.info("로그인 아이디: " + sId);
		//------------------------------------------------------
		//CouponService - createCoupon() 메서드 호출하여 쿠폰 발급 요청 (발급여부 리턴)
		boolean isIssued = couponService.createCoupon(sId, couponCode);
//		log.info("발급 됐나요?:"+ isIssued); //true
		
		//------------------------------------------------------
		// 응답데이터 Map 형식으로 생성
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("success", isIssued);
//		log.info(response.get("success"));
		
		//AJAX으로 다시 응답 넘겨줄때 Map으로 넘어가지 않음 JSON으로 바꾸고 문자열 형식으로 리턴해줘야함!!!!!!!!!!!!!!!!
		JSONObject jo = new JSONObject(response);
		return jo.toString(); 
	}
	
	//=================================================================================
	
}
