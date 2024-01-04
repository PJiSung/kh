package com.kh.spring.member.model.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import com.kh.spring.member.model.vo.Member;

@Mapper
public interface MemberDAO {	// 인터페이스 안에 있는 추상 메소드가

	Member login(Member m);		// 추상메소드명(login)이 mapper의 id가 된다

	int insertMember(Member m);

	ArrayList<HashMap<String, Object>> selectMylist(String id);

	int checkId(String id);

	int checknickName(String nickName);

	int updateMember(Member m);

	int deleteMember(String id);

	int updatePassword(HashMap<String, String> map);


	
}
