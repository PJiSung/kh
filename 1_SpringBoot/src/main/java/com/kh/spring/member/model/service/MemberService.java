package com.kh.spring.member.model.service;

import java.util.ArrayList;
import java.util.HashMap;

import com.kh.spring.member.model.vo.Member;

public interface MemberService {

	Member login(Member m);

	int insertMember(Member m);

	ArrayList<HashMap<String, Object>> selectMylist(String id);

	int checkId(String id);

	int checknickName(String nickName);

	int updateMember(Member m);

	int deleteMember(String id);

	int updatePassword(HashMap<String, String> map);

}
