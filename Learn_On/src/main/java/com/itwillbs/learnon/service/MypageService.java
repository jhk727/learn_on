package com.itwillbs.learnon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.learnon.mapper.MypageMapper;
import com.itwillbs.learnon.vo.MyCourseVO;
import com.itwillbs.learnon.vo.MyReviewVO;
import com.itwillbs.learnon.vo.WishlistVO;

@Service
public class MypageService {
	@Autowired
	private MypageMapper myMapper;

	public List<WishlistVO> getWishlist(String id, String filterType) {
		return myMapper.selectWishlist(id, filterType);
	}

	public int cancelMyFav(String class_id) {
		return myMapper.deleteWish(class_id);
	}

	public List<MyCourseVO> getMyCourse(String id, String filterType, String statusType) {
		return myMapper.selectMyCourse(id, filterType, statusType);
	}

	public int registReview(MyReviewVO review) {
		return myMapper.insertReview(review);
	}


	// 수강률 
	public int getCompletionRate(String id, String class_id) throws Exception {
		return myMapper.selectCompletionRate(id, class_id);
	}
	
	// 수강후기 작성 여부
	public Boolean isReviewWrited(String id, String class_id) {
		return myMapper.selectIsReviewed(id, class_id);
	}
	
	

}
