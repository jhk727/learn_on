package com.itwillbs.learnon.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.itwillbs.learnon.handler.GenerateRandomCode;
import com.itwillbs.learnon.service.MailService;
import com.itwillbs.learnon.service.MemberService;
import com.itwillbs.learnon.vo.MailAuthInfo;
import com.itwillbs.learnon.vo.MemberVO;

@Controller
public class MemberController {

	@Autowired
	private MemberService memberService;
	@Autowired
	private MailService mailService;
	
	
	private String uploadPath = "/resources/upload";
	
	@GetMapping("MemberLogin")
	public String loginForm() {
		
		return "member/member_login";
	}
	
	
	@PostMapping("MemberLogin")
	public String login(MemberVO member,Model model,HttpSession session
			,BCryptPasswordEncoder passwordEncoder,@CookieValue(value="userId",required=false)String userId2,HttpServletResponse response) {
		
		System.out.println("가져온 쿠키아이디@@"+userId2); //on
		Cookie cookie = new Cookie("userId",member.getMem_id()); //쿠키설정
		if(userId2 != null) { //체크
			cookie.setMaxAge(60*60*24*30);
		} else {
			cookie.setMaxAge(0);
		}
		response.addCookie(cookie);
		
		MemberVO dbMember = memberService.getMember(member);
		System.out.println(dbMember);
		
		
		if(dbMember == null || !passwordEncoder.matches(member.getMem_passwd(), dbMember.getMem_passwd())) {		
			model.addAttribute("msg", "로그인 실패!\\n아이디와 패스워드를 다시 확인해주세요");
			System.out.println("유저"+dbMember);
			return "result/fail";
		}else if(dbMember.getMem_status() == 3) { // 로그인 성공이지만, 탈퇴 회원일 경우
			model.addAttribute("msg", "탈퇴한 회원입니다!");
			return "result/fail";
		}else { //로그인 성공
			session.setAttribute("sId", dbMember.getMem_name());
			session.setAttribute("sId", member.getMem_id());
			session.setMaxInactiveInterval(60 * 120);
//			return "redirect:/";
			
			// 이전 페이지 저장 후 로그인 시 리다이렉트처리
			if(session.getAttribute("prevURL") == null) {
				return "redirect:/";
			} else {
				// request.getServletPath() 메서드를 통해 이전 요청 URL 을 저장할 경우
				// "/요청URL" 형식으로 저장되므로 redirect:/ 에서 / 제외하고 결합하여 사용
				return "redirect:" + session.getAttribute("prevURL");
			}
		}
		
	}	
	
	@GetMapping("MemberJoin")
	public String memberJoin() {
		
		return"member/member_join";
	}
	
	@PostMapping("MemberJoin")
	public String join(MemberVO member, Model model, BCryptPasswordEncoder passwordEncoder, HttpSession session) {
	    System.out.println("member : " + member);
	    // 비밀번호 암호화
	    String securePasswd = passwordEncoder.encode(member.getMem_passwd());
	    member.setMem_passwd(securePasswd);

	    // 파일 업로드 처리
	    boolean fileUploadSuccess = handleFileUpload(member, session, model);
	    if (!fileUploadSuccess) {
	        model.addAttribute("msg", "파일 업로드 중 오류가 발생했습니다.");
	        return "result/fail";
	    }

	    // 회원 가입 처리
	    int insertCount = memberService.registMember(member);
	    if (insertCount > 0) {
	        session.setAttribute("sId", member.getMem_name());
//	        session.setAttribute("sName", member.getMem_name());

//	         이메일 인증 처리
	        handleEmailAuth(member);
//	        MailAuthInfo mailAuthInfo = mailService.sendAuthMail(member);
//		    System.out.println("인증정보 : " + mailAuthInfo);
//		    memberService.registMemberAuthInfo(mailAuthInfo);

	        return "redirect:/MemberJoinSuccess";
	    } else {
	        model.addAttribute("msg", "회원가입 실패\n항목을 다시 확인해주세요");
	        return "result/fail";
	    }
	}

	// 파일 업로드 처리 메서드
	private boolean handleFileUpload(MemberVO member, HttpSession session, Model model) {
	    String realPath = session.getServletContext().getRealPath(uploadPath);
	    System.out.println("실제 경로: " + realPath);

	    LocalDate today = LocalDate.now();
	    String datePattern = "yyyy/MM/dd";
	    DateTimeFormatter dtf = DateTimeFormatter.ofPattern(datePattern);

	    String subDir = today.format(dtf);
	    realPath += "/" + subDir;

	    try {
	        Path path = Paths.get(realPath);
	        Files.createDirectories(path);
	    } catch (IOException e) {
	        e.printStackTrace();
	        return false;
	    }

	    MultipartFile mFile = member.getMem_pp_file();
	    String originalFileName = mFile.getOriginalFilename();
	    String saveFileName = UUID.randomUUID().toString() + "_" + originalFileName;
	    member.setFile_pp(subDir + "/" + saveFileName);

	    if (!mFile.getOriginalFilename().equals("")) {
	        try {
	            mFile.transferTo(new File(realPath, saveFileName));
	        } catch (IOException e) {
	            e.printStackTrace();
	            return false;
	        }
	    }
	    return true;
	}

//	 이메일 인증 처리 메서드
	private void handleEmailAuth(MemberVO member) {
		System.out.println("memberHandle : " + member);
	    MailAuthInfo mailAuthInfo = mailService.sendAuthMail(member ,member.getMem_email1(), member.getMem_email2());
	    System.out.println("인증정보 : " + mailAuthInfo);
	    memberService.registMemberAuthInfo(mailAuthInfo);
	}


	@GetMapping("MemberJoinSuccess")
	public String memberJoinSuccess() {
	    return "result/success";
	}
	
	//*****************인증메일 재발송***********************

	@GetMapping("ReSendAuthMail")
	public String reSendAuthMail(MemberVO member,HttpSession session) {
		
		return "member/resend_auth_mail_form";
		
	}
	
	@PostMapping("ReSendAuthMail")
	public String reSendAuthMail(MemberVO member,Model model , HttpSession session) {
		MemberVO dbmember = memberService.getMember(member);

		if(!member.getMem_email().equals(dbmember.getMem_email())) {
			model.addAttribute("msg","[존재하지 않는 이메일]\\n이메일을 다시한번 확인해주세요");
			return "result/fail";
		}
		
//		MailAuthInfo mailAuthInfo = mailService.sendAuthMail(member);
		MailAuthInfo mailAuthInfo = mailService.sendAuthMail(member, member.getMem_email1(), member.getMem_email2());
		
		memberService.registMemberAuthInfo(mailAuthInfo);
		System.out.println("인증메일 다시 보냄!!@!!!!!!!!!");
		model.addAttribute("msg", "인증메일 발송 성공");
		model.addAttribute("targetURL", "MemberJoinSuccess");
		
		return "result/success";
		}
		
	
	//****************************************
	
	//이메일 인증
		@GetMapping ("MemberEmailAuth")
		public String emailAuth(MailAuthInfo mailAuthInfo , Model model) {
			System.out.println("mailAuthInfo"+mailAuthInfo);
			
			// MemberService
			boolean isAuthSuccess = memberService.requestEmailAuth(mailAuthInfo);
			
			// 인증처리 결과판별
			if(!isAuthSuccess) {
				model.addAttribute("msg", "메일 인증 실패\\다시 인증해주세요");
				return "result/fail";
				
			}else{
				model.addAttribute("msg", "메일 인증 성공\\n홈페이지로 이동합니다");
				model.addAttribute("targetURL", "/");
				return "result/fail"; //fail로 가는이유는 문자 출력하기 위해서
			}
			
		}

	//*************아이디/닉네임 중복체크***************
	@ResponseBody
	@GetMapping("MemberCheckId")
	public String memberCheckId(String mem_id,MemberVO member) {
		System.out.println("mem_id : "+mem_id);
		member = memberService.getMember(member);
		boolean isDuplicate = false;
		if(member != null) { //아이디 중복
			isDuplicate= true;
		}
		return isDuplicate+"";
	}
	
	@ResponseBody
	@GetMapping("MemberCheckNick")
	public String memberCheckNick(String mem_nick,MemberVO member) {
		System.out.println("mem_nick : "+mem_nick);
		member = memberService.getMemberNick(member);
		System.out.println("가져온 member(get_nick) : "+member.getMem_nick());
		boolean isDuplicate = false;
		if(member != null) { //닉네임 중복
			isDuplicate= true;
		}
		return isDuplicate+"";
	}
	
	//*************로그아웃***************
	@GetMapping("MemberLogout")
	public String logout(HttpSession session) {
		session.invalidate();
		
		return "redirect:/";
	}
		
	//*************회원정보 수정***************
	@GetMapping("MemberModify")
	public String memberModify(MemberVO member, HttpSession session,Model model) {
		String id = (String)session.getAttribute("sId");
		if(id == null) {
			model.addAttribute("msg", "로그인 후 이용해주세요");
			model.addAttribute("targetURL", "MemberLogin");
			
			return "member/fail";
		}
		
		member.setMem_id(id);
		member = memberService.getMember(member);
		model.addAttribute("member", member);
		
		
		return "my_page/mypage_info";
		
	}
	
	@PostMapping("MemberModify")
	public String memberModifyForm(MemberVO member,Model model , BCryptPasswordEncoder passwordEncoder ,@RequestParam Map<String, String>map,HttpSession session ) {
		System.out.println("MAP : "+map);
		System.out.println("member : "+member);
//		System.out.println("member id @@ : "+member.getMem_id());
//		System.out.println("member nick@@ : "+member.getMem_nick());
//		System.out.println("member phone@@ : "+member.getMem_phone());
//		System.out.println("member Email@@ : "+member.getEmail());
//		System.out.println("member Email1@@ : "+member.getMem_email1());
//		System.out.println("member Email2@@ : "+member.getMem_email2());
		
		String id = (String)session.getAttribute("sId");
		map.put("id", id);

		String dbpasswd = memberService.getMemberPasswd(id);
		if(dbpasswd == null || !passwordEncoder.matches(map.get("oldPasswd"),dbpasswd)) {
			model.addAttribute("msg", "비밀번호가 일치하지 않습니다.");
			return "result/fail";
		}
		if(!map.get("mem_passwd").equals("")) {
			map.put("mem_passwd",passwordEncoder.encode(map.get("mem_passwd"))); //암호화된 새로운 비밀번호
		}
		
		int updateCount = memberService.modifyMember(map);
		
		if(updateCount > 0) {
			model.addAttribute("msg", "회원정보 수정성공");
			
			return"result/fail";
		}else {
			model.addAttribute("msg", "회원정보 수정실패\\n다시 확인해주세요 ");
			return"result/fail";
		}
	}
	
	@GetMapping("PasswdFinder")
	public String passwdFinder(String mem_email) {
		return"member/passwd_fineder";
	}
	
	/******************비밀번호 찾기*******미구현 ㅠㅠ************/
	@PostMapping("PasswdFinder")
	public String passwdFinderForm(String mem_email,String mem_name,MemberVO member, Model model,HttpSession session) {
	    try {
	        MemberVO DBmember = memberService.getMemberEmail(mem_email);
	        if (DBmember == null || !member.getMem_name().equals(mem_name)) {
	            model.addAttribute("msg", "이름또는 이메일이 일치하지 않습니다.");
	            return "result/fail";
	        }
	        
	        String temPasswd = GenerateRandomCode.getRandomCode(5);
	        System.out.println(temPasswd);
	        int updateCount = memberService.setTempPasswd(temPasswd,mem_email);
	        System.out.println("@@@@@@@@@@@@@@@@@@@@"+member);
	        
	        if(updateCount > 0) {
	        	MailAuthInfo mailAuthInfo = mailService.sendPasswdMail(DBmember, mem_email, temPasswd);
	        	System.out.println("인증정보 : " + mailAuthInfo);
	        	memberService.registMemberAuthInfo(mailAuthInfo);
	        	model.addAttribute("msg", "임시 비밀번호가 발급되었습니다\\n마이페이지에서 안전한 비밀번호로 변경해주세요");
	        	return "result/fail";
	        
	        }else {
	        	model.addAttribute("msg", "임시비밀번호 발급에 실패하였습니다.");
	        	return "result/fail";
	        }
	        
		    
	      
	    } catch (Exception e) {
	        model.addAttribute("msg", "비밀번호 재설정 중 오류가 발생했습니다.");
	        return "result/fail";
	    }
	}
	
//	@PostMapping("UpdatePassword")
//	public String updatePassword(String mem_passwd,HttpSession session,Model model,BCryptPasswordEncoder passwordEncoder) {
//	    Boolean isVerified = (Boolean) session.getAttribute("isVerified");
//	    if(isVerified == null || !isVerified) {
//	    	model.addAttribute("msg", "본인확인이 필요합니다");
//	    	return "result/fail";
//	    }
//	    String email = (String) session.getAttribute("email");
//	   
//	    // 비밀번호 암호화 및 저장
//        String encodedPassword = passwordEncoder.encode(mem_passwd);
//        memberService.updatePasswd(email, encodedPassword);
//        
//        session.invalidate();
//        
//        model.addAttribute("msg", "비밀번호가 성공적으로 변경되었습니다.");
//        return "result/success";
//	}
	/***********회원탈퇴*************/
	@GetMapping("MemberWithdraw")
	public String memberWithdraw(HttpSession session , Model model) {
		String id = (String) session.getAttribute("sId");
		if(id == null) {
			model.addAttribute("msg", "로그인 후 이용가능합니다");
			model.addAttribute("targetURL", "MemberLogin");
			return "member/fail";
		}
		return"my_page/member_withraw";
	}
	
	@PostMapping("MemberWithdraw")
	public String memberWithdrawForm(MemberVO member,String mem_passwd,BCryptPasswordEncoder passwordEncoder,HttpSession session ,Model model) {
		
		String id = (String) session.getAttribute("sId");
		String dbPasswd = memberService.getMemberPasswd(id);
		if(dbPasswd == null || !passwordEncoder.matches(mem_passwd, dbPasswd)) {
			model.addAttribute("msg", "권한이 없습니다");
			return "result/fail";
		}
		
		int withdrawResult = memberService.withdrawMember(id);
		
		if(withdrawResult > 0) {
			session.invalidate();
			model.addAttribute("msg", "탈퇴 처리가 완료되었습니다.");
			model.addAttribute("targetURL", "./");
			return "result/fail";
		} else {
			model.addAttribute("msg", "탈퇴 실패\\n관리자에게 문의 바랍니다.");
			return "result/fail";
		}
		
	}
	
	
}