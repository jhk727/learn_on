package com.itwillbs.learnon.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.learnon.vo.MailAuthInfo;
import com.itwillbs.learnon.vo.MemberVO;

@Mapper
public interface MemberMapper {
	//회원 정보 조회
	MemberVO selectMember(MemberVO member);

	//닉네임 조회
	MemberVO selectMemberNick(MemberVO member);

	//회원 추가
	int insertMember(MemberVO member);

	//회원정보 수정
	int updateMember(Map<String, String> map);

	// mail_auth_info 테이블에 이메일 아이디와 ,UUID로된 code 추가
	void insertMailAuthInfo(MailAuthInfo mailauthInfo);
	// ?
	void updateMailAuthInfo(MailAuthInfo mailauthInfo);

	MailAuthInfo selectMailAuthInfo(MailAuthInfo mailauthInfo);

	// 이메일 인증 후 회원상태 변경
	void updateMailAuthStatus(MailAuthInfo mailAuthInfo);

	//메일 인증 후 mail_auth_info 테이블 데이터 삭제
	void deleteMailAuthInfo(MailAuthInfo mailAuthInfo);
	
	// 회원 패스워드 조회
	String selectMemberPasswd(String id);
	
	//비번찾기
//	MemberVO selectEmail(MemberVO member);
	MemberVO selectEmail(String mem_email);
	
	
	//회원탈퇴
	int updateMemberStatus(
			@Param("mem_id") String id
			, @Param("mem_status") int mem_status);

	//새로운 비번 올리기
	int updateTempPasswd(
			@Param("heshePasswd") String heshePasswd , 
			@Param("mem_email") String mem_email);

	//이메일 중복체크
	MemberVO selectEmailId(MemberVO member);




	
}
