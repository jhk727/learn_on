package com.itwillbs.learnon.mapper;

import java.util.List;
import java.util.Map;

import com.itwillbs.learnon.vo.MemberVO;
import com.itwillbs.learnon.vo.PayVO;
import com.itwillbs.learnon.vo.OrderVO;

public interface PayMapper {
	//주문자 정보 조회
	MemberVO selectMember(String sId);
	
	//결제 상품 목록 조회 (조회한 결제 상품을 OrderVO에 담아서 리턴)
	List<OrderVO> selectedCart(List<String> checkItems);
	
	//결제 정보 저장
	void insertPayInfo(PayVO payVO);
	
	//주문 정보 저장
	void insertOrderInfo(Map<String, Object> orderData);
	
	//쿠폰 사용 상태 업데이트
	void updateCoupon(int coupon_id);
	
	//나의클래스 인서트
	void insertMycourse(Map<String, Object> mycourseData);
	
	//결제완료 페이지에 전달할 결제 정보 조회
	PayVO selectPayInfo(String merchant_uid);
	
	//결제 취소에 필요한 impUid에 해당하는 회원ID 조회
	String selectMemId(String impUid);
	
	//결제 취소 시 결제 상태값 변경
	void updatePayStatus(String imp_uid, String memId);
	
	//결제 취소 시 쿠폰 복구
	void updateCouponUsed(String imp_uid, String memId);
	
	

	
}
