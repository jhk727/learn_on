package com.itwillbs.learnon.service;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.learnon.mapper.AdminMapper;
import com.itwillbs.learnon.vo.AdminVO;
import com.itwillbs.learnon.vo.CourseSupportVO;
import com.itwillbs.learnon.vo.CourseVO;
import com.itwillbs.learnon.vo.MemberVO;

@Service
public class AdminService {
	@Autowired
	private AdminMapper mapper;
	
	
	
	// 카테고리
	public List<Map<String, String>> getCategory() {
		return mapper.getCategory();
	}
	public List<Map<String, String>> getMainCate() {
		return mapper.getMainCate();
	}
	public List<Map<String, String>> getSubCate() {
		return mapper.getSubCate();
	}
	public List<Map<String, Object>> selectSubCate(AdminVO admin) {
		return mapper.selectSubCate(admin);
	}
	public int insertMainCate(AdminVO VO) {
		return mapper.insertMainCate(VO);
	}
	public int insertSubCate(AdminVO VO) {
		return mapper.insertSubCate(VO);
	}
	public int deleteMainCate(String CODEID) {
		return mapper.deleteMainCate(CODEID);
	}
	public int deleteSubCate(String old_codetype_subcate) {
		return mapper.deleteSubCate(old_codetype_subcate);
	}
	public int updateMainCate(AdminVO updateVO) {
		return mapper.updateMainCate(updateVO);
	}
	public int updateSubCate(AdminVO updateVO) {
		return mapper.updateSubCate(updateVO);
	}
	
	// 클래스
	public int registClass(AdminVO vO) {
		return mapper.insertClass(vO);
	}
	public int curriculum(AdminVO insertCur) {
		return mapper.curriculum(insertCur);
	}
	public List<AdminVO> getClassList() {
		return mapper.getClassList();
	}
	public List<AdminVO> getClass(AdminVO vO) {
		return mapper.getClass(vO);
	}
	public List<CourseVO> updateClass(AdminVO vO) {
		return mapper.updateClass(vO);
	}
	
	//	=================== 멤버 ==============================
	//	일반회원 조회
	public List<MemberVO> getNomalMemberList(int startRow, int listLimit, String searchKeyword, String searchType, String sort) {
		return mapper.getNomalMemberList(startRow, listLimit, searchKeyword, searchType, sort);
	}
	//	일반회원 카운트
	public int getNomalMemberListCount(String searchKeyword, String searchType) {
		return mapper.getNomalMemberListCount(searchKeyword, searchType);
	}
	
	//	강사회원 조회
	public List<MemberVO> getInstructorMemberList() {
		return mapper.getInstructorMemberList();
	}
	//	탈퇴회원 조회
	public List<MemberVO> getWithdrawMemberList() {
		return mapper.getWithdrawMemberList();
	}
	
	public MemberVO getMemberList(String mem_id) {
		return mapper.getMemberList(mem_id);
	}
	//	멤버상태 변경(1:승인, 2:대기 3:탈퇴)
	public int changeMemStatus(Map<String, String> map) {
		return mapper.changeMemStatus(map);
	}
	//	회원 삭제
	public int removeMember(String mem_id) {
		return mapper.deleteMember(mem_id);
	}
	//	회원 등급 변경(일반 OR 강사)
	public int changeGradeMember(MemberVO member) {
		return mapper.updateGrade(member);
	}
	
	public int insertClassPic(AdminVO vO) {
		return mapper.insertClassPic(vO);
		
	}
	public int insertCurVideo(AdminVO vO) {
		return mapper.insertCurVideo(vO);
	}
	public List<CourseVO> getCurriculum(AdminVO class_id) {
		return mapper.getCurriculum(class_id);
	}
	public int getClassId() {
		return mapper.getClassId();
	}


	
	// =============== 1:1 문의
	
	
	// 수강 문의 게시판
	// 문의 목록 조회 (강의별 수강문의 게시판)
	public List<CourseSupportVO> getCourserSupportListToAdm(int startRow, int listLimit) {
		return mapper.selectCourseSupportList(startRow, listLimit);
	}
	// 문의 답변 작성/수정 업데이트
	public int answerSupport(CourseSupportVO cSupport) {
		return mapper.updateCourseSupport(cSupport);
	}
	public int deleteClass(int i) {
		return mapper.deleteClass(i);
	}
	public int deleteCurriculum(int i) {
		return mapper.deleteCurriculum(i);
	}

}
