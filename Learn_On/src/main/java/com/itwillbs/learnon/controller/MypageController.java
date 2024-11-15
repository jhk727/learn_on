package com.itwillbs.learnon.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.itwillbs.learnon.service.MypageService;
import com.itwillbs.learnon.vo.MyCourseVO;
import com.itwillbs.learnon.vo.MyReviewVO;
import com.itwillbs.learnon.vo.SupportBoardVO;
import com.itwillbs.learnon.vo.WishlistVO;
import com.itwillbs.learnon.vo.PageInfo;

@Controller
public class MypageController {
	
	@Autowired
	private MypageService myService;
	
	// 첨부파일 가상경로
	private String uploadPath = "/resources/upload";
	
	// 계정정보
	@GetMapping("MyInfo")
	public String mypageForm() {
		return "my_page/mypage_info";
	}
	
	// 관심목록
	@GetMapping("MyFav")
	public String myFav(@RequestParam(defaultValue = "") String filterType, HttpServletRequest request, HttpSession session, Model model) {
//		System.out.println("필터타입: " + filterType);
		
		String id = (String)session.getAttribute("sId");
		if(id == null) {
			model.addAttribute("msg", "로그인 필수!\\n 로그인 페이지로 이동합니다!");
			model.addAttribute("targetURL", "MemberLogin");
			savePreviousUrl(request, session);
			
			return "result/fail";
		}
		
		List<WishlistVO> wishlist = myService.getWishlist(id, filterType);
		
		model.addAttribute("wishlist", wishlist);
		
		return "my_page/mypage_fav";
	}
	
	// 관심목록 삭제
	@PostMapping("MyFavDel")
	public String myFavDel(String class_id, HttpServletRequest request, HttpSession session, Model model) {
		System.out.println("class_id: " + class_id);

		String id = (String)session.getAttribute("sId");
		if(id == null) {
			model.addAttribute("msg", "로그인 필수!\\n 로그인 페이지로 이동합니다!");
			model.addAttribute("targetURL", "MemberLogin");
			savePreviousUrl(request, session);
			
			return "result/fail";
		}
		
		int deleteCount = myService.cancelMyFav(class_id);

		if (deleteCount > 0) {
			return "redirect:/MyFav";
		} else {
			model.addAttribute("msg", "삭제 실패!");
			return "result/fail";
		}
		
	}
	
	// 나의 강의실
	@GetMapping("MyDashboard")
	public String myDashboard(@RequestParam(defaultValue = "") String filterType,
							  @RequestParam(defaultValue = "") String statusType,
							  HttpServletRequest request, HttpSession session, Model model) {
		
		String id = (String)session.getAttribute("sId");
		
		// 나의 강의실, 작성한 수강평 모두 이전 페이지 저장 필요함 (수정, 삭제 이후 리다이렉트 때문)
		savePreviousUrl(request, session);
		
		if(id == null) {
			model.addAttribute("msg", "로그인 필수!\\n 로그인 페이지로 이동합니다!");
			model.addAttribute("targetURL", "MemberLogin");
			return "result/fail";
		}
		
		List<MyCourseVO> myCourse = myService.getMyCourse(id, filterType, statusType);
		
		model.addAttribute("myCourse", myCourse);
		
		return "my_page/mypage_dashboard";
	}
	
	// 작성한 수강평 목록
	@GetMapping("MyReview")
	public String myReview(MyReviewVO review, HttpServletRequest request, HttpSession session, Model model) {
		
		String id = (String)session.getAttribute("sId");
		
		// 나의 강의실, 작성한 수강평 모두 이전 페이지 저장 필요함 (수정, 삭제 이후 리다이렉트 때문)
		savePreviousUrl(request, session);
		
		if(id == null) {
			model.addAttribute("msg", "로그인 필수!\\n 로그인 페이지로 이동합니다!");
			model.addAttribute("targetURL", "MemberLogin");
			return "result/fail";
		}
		
		review.setMem_id(id);
		
		List<MyReviewVO> myReviewList = myService.getMyReview(review);
		int reviewCount = myService.getMyReviewCount(review);
		
		model.addAttribute("myReviewList", myReviewList);
		model.addAttribute("reviewCount", reviewCount);
		
		return "my_page/mypage_review";
	}
	
	// 수강평 작성하기
	@PostMapping("MyReviewWrite")
	public String myReviewWrite(MyReviewVO review, HttpServletRequest request, HttpSession session, Model model) {
		String id = (String)session.getAttribute("sId");
		if(id == null) {
			model.addAttribute("msg", "로그인 필수!\\n 로그인 페이지로 이동합니다!");
			model.addAttribute("targetURL", "MemberLogin");
			
			return "result/fail";
		}
		
		review.setMem_id(id);
		
		int insertCount = myService.registReview(review);
			
		if(insertCount > 0) {
			return "redirect:/MyDashboard";
		} else {
			model.addAttribute("msg", "삭제 실패!");
			return "result/fail";
		}
		
	}
	
	// 작성된 수강후기 폼
	@ResponseBody
	@GetMapping("MyReviewUpdateForm")
	public String myReviewUpdateForm(MyReviewVO review, HttpServletRequest request, HttpSession session, Model model) {
		
		String id = (String)session.getAttribute("sId");
		if(id == null) {
			model.addAttribute("msg", "로그인 필수!\\n 로그인 페이지로 이동합니다!");
			model.addAttribute("targetURL", "MemberLogin");
			
			return "result/fail";
		}
		
		review.setMem_id(id);
		
		review = myService.getMyReviewDetail(review);
		
		JSONObject jo = new JSONObject(review);
		
		return jo.toString();
	}
	
	// 작성된 수강후기 수정
	@PostMapping("MyReviewUpdate")
	public String myReviewUpdate(MyReviewVO review, HttpServletRequest request, HttpSession session, Model model) {
		
		String id = (String)session.getAttribute("sId");
		if(id == null) {
			model.addAttribute("msg", "로그인 필수!\\n 로그인 페이지로 이동합니다!");
			model.addAttribute("targetURL", "MemberLogin");
			
			return "result/fail";
		}
		
		review.setMem_id(id);
		
		int updateCount = myService.modifyMyReview(review);
		
		if(updateCount > 0) {
			System.out.println("이전주소: " + session.getAttribute("prevURL"));
			return "redirect:" + session.getAttribute("prevURL");
		} else {
			model.addAttribute("msg", "수정 실패!");
			return "result/fail";
		}
		
	}
	
	// 수강평 삭제하기
	@PostMapping("MyReviewDelete")
	public String myReviewDelete(MyReviewVO review, HttpServletRequest request, HttpSession session, Model model) {
		
		String id = (String)session.getAttribute("sId");
		if(id == null) {
			model.addAttribute("msg", "로그인 필수!\\n 로그인 페이지로 이동합니다!");
			model.addAttribute("targetURL", "MemberLogin");
			savePreviousUrl(request, session);
			
			return "result/fail";
		}
		
		int deleteCount = myService.removeReview(review);
		
		if(deleteCount > 0) {
			System.out.println("삭제 성공!");
			return "redirect:/MyReview";
		} else {
			model.addAttribute("msg", "삭제 실패!");
			return "result/fail";
		}
	}
	
	// 강의 시청 페이지
	@GetMapping("MyCourseBoard")
	public String myCourseBoard() {
		
		return "my_page/mypage_course";
	}
	
		
	// 결제내역
	@GetMapping("MyPayment")
	public String myPayment() {
		return "my_page/mypage_payment";
	}
	
	// 보유한 쿠폰
	@GetMapping("MyCoupon")
	public String myCoupon(HttpServletRequest request, HttpSession session, Model model) {
		
		String id = (String)session.getAttribute("sId");
		if(id == null) {
			model.addAttribute("msg", "로그인 필수!\\n 로그인 페이지로 이동합니다!");
			model.addAttribute("targetURL", "MemberLogin");
			savePreviousUrl(request, session);
			
			return "result/fail";
		}
		
		List<Map<String, Object>> myCoupon = myService.getMyCouponList(id);
		int couponCount = myService.getMyCouponCount(id);
		
		model.addAttribute("myCoupon", myCoupon);
		model.addAttribute("couponCount", couponCount);
		
		return "my_page/mypage_coupon";
	}
	
	// 문의내역 글쓰기 폼
	@GetMapping("MySupportWrite")
	public String mySupportWriteForm(HttpServletRequest request, HttpSession session, Model model) {
		String id = (String)session.getAttribute("sId");
		if(id == null) {
			model.addAttribute("msg", "로그인 필수!\\n 로그인 페이지로 이동합니다!");
			model.addAttribute("targetURL", "MemberLogin");
			savePreviousUrl(request, session);
			
			return "result/fail";
		}
		
		return "my_page/mypage_inquiry_write";
	}
	
	@PostMapping("MySupportWrite")
	public String mySupportWrite(SupportBoardVO support, HttpServletRequest request, HttpSession session, Model model) {
		System.out.println("1:1 문의글 쓰기 : " + support);
		
		// 세션아이디 체크
		String id = (String)session.getAttribute("sId");
		if(id == null) {
			model.addAttribute("msg", "로그인 필수!\\n 로그인 페이지로 이동합니다!");
			model.addAttribute("targetURL", "MemberLogin");
			savePreviousUrl(request, session);
			
			return "result/fail";
		}
		
		// 파일 첨부 업로드 경로 처리
		String realPath = getRealPath(session);
		
		// 디렉토리 생성
		String subDir = createDirectories(realPath);
		
		// 실제 파일 처리
		MultipartFile mFile1 = support.getFile1();
//		System.out.println("원본파일명: " + mFile1);
		support.setSupport_file1("");
		
		String fileName = processDuplicateFileName(support, subDir);
		
		System.out.println("DB 저장파일" + support.getSupport_file1());
		System.out.println("1:1문의 글 작성 최종내용: " + support);
		
		// 글쓰기 서비스 요청
		int insertCount = myService.registSupport(support);
		
		if (insertCount > 0) {
			completeUpload(support, realPath, fileName);
			
			return "redirect:/MySupport";
		} else {
			model.addAttribute("msg", "글쓰기 실패!");
			return "result/fail";
		}
		
	}
	
	// 문의내역 목록
	@GetMapping("MySupport")
	public String mySupportList(@RequestParam(defaultValue = "1") int pageNum,HttpServletRequest request, HttpSession session, Model model) {
		System.out.println("페이지번호: " + pageNum);
		
		// 페이징 설정
		int listLimit = 6; // 한 페이지당 게시물 수
		int startRow = (pageNum - 1) * listLimit;
		int listCount = myService.getSupportListCount();
		
		int pageListLimit = 5; // 페이징 개수 
		int maxPage = (listCount / listLimit) + (listCount % listLimit > 0 ? 1 : 0);
		
		if(maxPage == 0) {
			maxPage = 1;
		}
		int startPage = (pageNum - 1) / pageListLimit * pageListLimit + 1;
		System.out.println("maxPage = " + maxPage);
		int endPage = startPage + pageListLimit - 1;
		
		if(endPage > maxPage) {
			endPage = maxPage;
		}
		
		if(pageNum < 1 || pageNum > maxPage) {
			model.addAttribute("msg", "해당 페이지는 존재하지 않습니다!");
			model.addAttribute("targetURL", "MySupport?pageNum=1");
			return "result/fail";
		}
		PageInfo pageInfo = new PageInfo(listCount, pageListLimit, maxPage, startPage, endPage);
		
		// Model 객체에 페이징 정보 저장
		model.addAttribute("pageInfo", pageInfo);
		
		// 게시물 목록 조회
		List<SupportBoardVO> supportList = myService.getSupportList(startRow, listLimit);
		
		model.addAttribute("supportList", supportList);
		
		return "my_page/mypage_inquiry_list";
	}
	
	// 문의내역 상세
	@GetMapping("MySupportDetail")
	public String mySupportDetail(int support_idx, Model model) {
		SupportBoardVO support = myService.getSupportDetail(support_idx);
		
		if(support == null) {
			model.addAttribute("msg", "존재하지 않는 게시물입니다");
			return "result/fail";
		}
		
		addFileToModel(support, model);
		model.addAttribute("support", support);
		
		return "my_page/mypage_inquiry_detail";
	}
	
	// 문의내역 수정 폼
	@GetMapping("MySupportModify")
	public String mySupportModifyForm(int support_idx, HttpSession session, HttpServletRequest request, Model model) {
		
		/// 세션아이디 체크
		String id = (String)session.getAttribute("sId");
		if(id == null) {
			model.addAttribute("msg", "로그인 필수!\\n 로그인 페이지로 이동합니다!");
			model.addAttribute("targetURL", "MemberLogin");
			savePreviousUrl(request, session);
			
			return "result/fail";
		}
		
		SupportBoardVO support = myService.getSupportDetail(support_idx);
		
		if(support == null || !id.equals(support.getMem_id())) {
			model.addAttribute("msg", "잘못된 접근입니다!");
			return "result/fail";
		}
		
		model.addAttribute("support", support);
		
		addFileToModel(support, model);
		
		return "my_page/mypage_inquiry_update";
	}
	
	// 문의내역 수정
	@PostMapping("MySupportModify")
	public String mySupportModify(SupportBoardVO support, @RequestParam(defaultValue = "1") int pageNum, HttpServletRequest request, HttpSession session, Model model) {
		
		String realPath = getRealPath(session);
		String subDir = createDirectories(realPath);
		realPath += "/" + subDir;
		
		String fileName = processDuplicateFileName(support, subDir);
		
		
		int updateCount = myService.modifySupport(support);
		
		if(updateCount > 0) {
			completeUpload(support, realPath, fileName);
			return "redirect:/MySupportDetail?support_idx=" + support.getSupport_idx() + "&pageNum=" + pageNum; 
 		} else {
 			model.addAttribute("msg", "글 수정 실패!");
 			return "result/fail";
 		}
	}
	
	
	// 문의내역 삭제
	
	
	
	
	// 출석체크
	@GetMapping("MyAttendance")
	public String myAttendance() {
		return "my_page/mypage_attendance";
	}
	
	
	// ===========================================================================================
	// 이전 페이지 이동 저장
	private void savePreviousUrl(HttpServletRequest request, HttpSession session) {
		String prevURL = request.getServletPath();
		String queryString = request.getQueryString();
//		System.out.println("prevURL : " + prevURL);
//		System.out.println("queryString : " + queryString);
		
		if (queryString != null) {
			prevURL += "?" + queryString;
		}
		
		session.setAttribute("prevURL", prevURL);
	}
	
	
	// 첨부파일 - 파일 목록 처리 (첨부파일 단일)
	private void addFileToModel(SupportBoardVO support, Model model) {
		String fileName = support.getSupport_file1();
		String originalFileName = "";
		
		if(fileName != null) {
			originalFileName = fileName.substring(fileName.indexOf("_") + 1);
		} else {
			originalFileName = fileName;
		}
		
		model.addAttribute("originalFileName", originalFileName);
		model.addAttribute("fileName", fileName);
	}
	
	// 첨부파일 - 실제 경로 리턴 처리
	private String getRealPath(HttpSession session) {
		String realPath = session.getServletContext().getRealPath(uploadPath);
		
		return realPath;
	}
	
	// 첨부파일 - 서브디렉토리 생성 처리
	private String createDirectories(String realPath) {
		String subDir = "";
		
		// 서브디렉토리명 만들기
		LocalDate today = LocalDate.now(); // 날짜로 폴더명 생성하기
//		System.out.println("작성날짜: " + today);
		String datePattern = "yyyy/MM/dd";
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(datePattern);
		
		subDir = today.format(dtf);
		realPath += "/" + subDir;
//		System.out.println("실제 파일 업로드 경로: " + realPath);
		
		try {
			Path path = Paths.get(realPath);
			Files.createDirectories(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return subDir;
	}
	
	// 첨부파일 -  첨부파일명 중복 대책 처리
	private String processDuplicateFileName(SupportBoardVO support, String subDir) {
		MultipartFile mFile1 = support.getFile1();
		System.out.println("원본파일명: " + mFile1);
		support.setSupport_file1("");
		
		String fileName = "";
		
		if(!mFile1.getOriginalFilename().equals("")) {
			fileName = UUID.randomUUID().toString().substring(0, 8) + "_" + mFile1.getOriginalFilename();
			support.setSupport_file1(subDir + "/" + fileName);
		}
		
		return fileName;
	}
	
	// 첨부파일 - 실제 파일 업로드 처리(임시경로 -> 실제경로)
	private void completeUpload(SupportBoardVO support, String realPath, String fileName) {
		MultipartFile mFile1 = support.getFile1();
		
		try {
			if(!mFile1.getOriginalFilename().equals("")) {
				mFile1.transferTo(new File(realPath, fileName));
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
