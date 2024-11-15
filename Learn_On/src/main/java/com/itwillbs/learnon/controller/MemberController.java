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

import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.itwillbs.learnon.handler.MailAuthenticator;
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
	public String login(MemberVO member,Model model,HttpSession session,BCryptPasswordEncoder passwordEncoder) {
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
			return "redirect:/";
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

		if(!member.getEmail().equals(dbmember.getEmail())) {
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
		System.out.println(mem_id);
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
		System.out.println(mem_nick);
		member = memberService.getMember(member);
		boolean isDuplicate = false;
		if(member != null) { //아이디 중복
			isDuplicate= true;
		}
		return isDuplicate+"";
	}
	
	@GetMapping("MemberLogout")
	public String logout(HttpSession session) {
		session.invalidate();
		
		return "redirect:/";
	}

}