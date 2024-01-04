package com.kh.spring.member.model.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.spring.member.model.dao.MemberDAO;
import com.kh.spring.member.model.vo.Member;

@Service
public class MemberServiceImpl implements MemberService{
	
	@Autowired
	private MemberDAO mDAO;

	@Override
	public Member login(Member m) {
		return mDAO.login(m);
	}

	@Override
	public int insertMember(Member m) {
		return mDAO.insertMember(m);
	}

	@Override
	public ArrayList<HashMap<String, Object>> selectMylist(String id) {
		return mDAO.selectMylist(id);
	}

	@Override
	public int checkId(String id) {
		return mDAO.checkId(id);
	}

	@Override
	public int checknickName(String nickName) {
		return mDAO.checknickName(nickName);
	}

	@Override
	public int updateMember(Member m) {
		return mDAO.updateMember(m);
	}

	@Override
	public int deleteMember(String id) {
		return mDAO.deleteMember(id);
	}

	@Override
	public int updatePassword(HashMap<String, String> map) {
		return mDAO.updatePassword(map);
	}			
}
