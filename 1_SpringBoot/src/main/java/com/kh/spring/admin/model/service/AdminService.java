package com.kh.spring.admin.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import com.kh.spring.board.model.vo.Attachment;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.PageInfo;
import com.kh.spring.member.model.vo.Member;

public interface AdminService {
	
	ArrayList<HashMap<String, Object>> newestBoardList();

	ArrayList<Member> selectMembers();

	int updateInfo(Properties prop);

	int getListCount(int i);

	ArrayList<Board> selectBoardList(PageInfo pi, int i);

	int updateBoardStatus(HashMap<String, Object> map);

	int getAListCount(int i);

	ArrayList<Attachment> selectAttmList();

	void updateAttmStatus(HashMap<String, Object> map);

}
