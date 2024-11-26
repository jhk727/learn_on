package com.itwillbs.learnon.service;

import java.lang.reflect.Member;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.learnon.mapper.MemberMapper;
import com.itwillbs.learnon.vo.MailAuthInfo;
import com.itwillbs.learnon.vo.MemberVO;

@Service
public class MemberService {
	@Autowired
	private MemberMapper mapper;

	public int registMember(MemberVO member) {
		return mapper.insertMember(member);
	}



	public void registMemberAuthInfo(MailAuthInfo mailauthInfo) {
		MailAuthInfo dbMailAuthInfo = mapper.selectMailAuthInfo(mailauthInfo);
		if (dbMailAuthInfo == null) {
			mapper.insertMailAuthInfo(mailauthInfo);
		} else {
			mapper.updateMailAuthInfo(mailauthInfo);
		}

	}

	public boolean requestEmailAuth(MailAuthInfo mailAuthInfo) {
		boolean isAuthsuccess = false;


		MailAuthInfo dbMailAuthInfo = mapper.selectMailAuthInfo(mailAuthInfo);
		System.out.println("조회된 인증 정보" + dbMailAuthInfo);

		// 인증정보 조회결과 판별
		if (dbMailAuthInfo != null) {
			if (mailAuthInfo.getAuth_code().equals(dbMailAuthInfo.getAuth_code())) { // 인증코드가 일치
				mapper.updateMailAuthStatus(mailAuthInfo); //인증상태 업데이트
				mapper.deleteMailAuthInfo(mailAuthInfo);

				isAuthsuccess = true;
			}
		}

		return isAuthsuccess;
	}

	public String getMemberPasswd(String id) {
		return mapper.selectMemberPasswd(id);
	}

//	public int modifyMember(Map<String, String> map) {
//		// TODO Auto-generated method stub
//		return mapper.updateMember(map,mem_id);
//	}
//}

	public MemberVO getMember(MemberVO member) {
		return mapper.selectMember(member);
		
	}
	
	public MemberVO getMemberNick(MemberVO member) {
		return mapper.selectMemberNick(member);
		
	}



	public int modifyMember(Map<String, String> map) {
		System.out.println("@@@mem_passwd: " + map.get("mem_passwd"));
		System.out.println("@@@mem_name: " + map.get("mem_name"));

		return mapper.updateMember(map);
	}

	
	public MemberVO getMemberEmail(String mem_email) {
		return mapper.selectEmail(mem_email);
	}

	//회원상태 탈퇴 요청
	public int withdrawMember(String id) {
		return mapper.updateMemberStatus(id,3); //3 : 탈퇴
	}

	//비밀번호 찾기로 비밀번호 업데이트
	public int setTempPasswd(String temPasswd, String mem_email) {
		return mapper.updateTempPasswd(temPasswd,mem_email);
	}


}
