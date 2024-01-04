package com.kh.spring.admin.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.kh.spring.board.model.vo.Attachment;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.member.model.vo.Member;

@Mapper
public interface AdminDAO {
	
	ArrayList<HashMap<String, Object>> newestBoardList();

	ArrayList<Member> selectMembers();

	int updateInfo(Properties prop);

	int getListCount(int i);

	ArrayList<Board> selectBoardList(int i, RowBounds rowBounds);

	int updateBoardStatus(HashMap<String, Object> map);

	int getAListCount(int i);

	ArrayList<Attachment> selectAttmList();

	void updateAttmStatus(HashMap<String, Object> map);
}
