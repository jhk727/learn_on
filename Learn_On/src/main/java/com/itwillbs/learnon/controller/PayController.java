package com.itwillbs.learnon.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.learnon.service.PayService;
import com.itwillbs.learnon.vo.MemberVO;
import com.itwillbs.learnon.vo.OrderVO;
import com.itwillbs.learnon.vo.PayCancelVO;
import com.itwillbs.learnon.vo.PayVO;
import com.itwillbs.learnon.vo.PayVerificationVO;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

@Controller
public class PayController {
	@Autowired
	private PayService payService;
	
	//IamportClient 객체 생성 - 포트원에서 제공
	private IamportClient iamportClient;
	
	public PayController() {
		this.iamportClient = new IamportClient("1582333057803703", "Xeql20bTzSYAkeu3qPVXGoU3GEn0Ve4WKSmXsZViEAIrMFoJKE8n8q78DJlZMXkconSi5Nd3JpfpUX7h");
	}
	
	//=================================================================================
	// "Payment" 서블릿 주소 로드시 매핑 - GET
	// 결제 목록 조회 비즈니스 로직
	@GetMapping("Payment")
	public String payment(@RequestParam(value = "checkitem", required = false) List<String> checkItems,
						 HttpSession session, Model model) {
		// checkItems가 제대로 전달되었는지 확인
//	    System.out.println("(Payment)선택한 장바구니 번호: " + checkItems); //[4, 3, 2, 1]
		//------------------------------------------------------
		// 로그인 정보 가져오기 (세션 아이디값 확인)
		String sId = (String) session.getAttribute("sId");
		System.out.println("결제할 로그인 아이디: " + sId);
		//------------------------------------------------------
		//sId를 이용해 주문정보 객체 호출
		MemberVO member = payService.getMemberInfo(sId);
	    
		// List<String>으로 전달하여 getSelectedCarts 호출
	    List<OrderVO> selectedItems = payService.getSelectedCart(checkItems);
//	    System.out.println("selectedItems: " + selectedItems);
	    
	    
	    // 회원 정보 및 선택된 상품 정보를 Model에 추가하여 payment.jsp로 전달
	    model.addAttribute("member", member);
	    model.addAttribute("selectedCartList", selectedItems);

	    // 결제 페이지로 이동
	    return "cart_payment/payment";
	}
	
	//=================================================================================
	// 1. 결제 사후 검증
	// - 사후 검증은 클라이언트에서 결제 요청이 끝난 후 이루어진다. 
	// - 결제 요청 후 응답받은 내용을 바탕으로 실 결제금액과 자체 DB에 저장된 결제요청금액을 비교한다.
	// - 테스트 환경에서는 부분 환불이 불가능하므로 전체 환불 처리
	@ResponseBody
	@PostMapping("/payments/verification")
	public IamportResponse<Payment> paymentByImpUid(@RequestBody PayVerificationVO payVerificationVO) throws IamportResponseException, IOException {
		System.out.println("결제검증VO : " + payVerificationVO); //결제검증VO: PayVerificationVO(imp_uid=imp_600839946642, merchant_uid=2024112421267, amount=1004)
	
		//IamportClient클래스의 paymentByImpUid() 함수 호출
		//=> 파라미터: PayVerificationVO(imp_uid) 	/리턴: IamportResponse<Payment>
		return iamportClient.paymentByImpUid(payVerificationVO.getImp_uid());
	}
	
	//--------------------------------------------------------
	// 2. 사후 처리 후 결제정보&주문정보 DB 저장
	@ResponseBody
	@PostMapping("payinfoSave")
	public void  savePayInfo(@RequestBody PayVO payVO) throws IamportResponseException, IOException {
		System.out.println("결제저장DTO : " + payVO);
		
		payService.savePayInfo(payVO);
	}
	
	@ResponseBody
    @PostMapping("orderinfoSave")
	public String orderPayInfo(@RequestBody OrderVO orderVO) throws IamportResponseException, IOException {
		System.out.println("주문저장DTO : " + orderVO);
		
		payService.saveOrderInfo(orderVO);
		payService.deleteCart(orderVO.getCartItemIdx());
		
		return orderVO.getMerchant_uid(); 
	}
    
	//--------------------------------------------------------
	// 결제 취소
	@ResponseBody
	@PostMapping("/payments/cancel")
	public IamportResponse<Payment> cancelPaymentbyImpUid(@RequestBody PayCancelVO paycancelVO) throws IamportResponseException, IOException {
		System.out.println("결제 취소에 필요한 VO: " + paycancelVO); 
		
		String impUid = paycancelVO.getImp_uid();
		
		//IamportClient클래스의 cancelPaymentByImpUid() 함수 호출
		//=> 파라미터: CancelData클래스 	/리턴: IamportResponse<Payment>
		//CancelData(결제를 취소할때 사용하기위한 클래스) : 주의! 이름 맞춰줘야함!
		return iamportClient.cancelPaymentByImpUid(new CancelData(impUid, true));
	}
	
	//=================================================================================
	// "Terms" 이용약관 페이지 매핑 - GET
	@GetMapping("Terms")
	public String termPage() {
		return "info/terms";
	}
	
	//=================================================================================
	// "PayResult" 매핑 - GET => 결제 완료 뷰페이지 포워딩
	@GetMapping("PayResult")
	public String paySuccess() {
		return "cart_payment/pay_result";
	}
	
}
