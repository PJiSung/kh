package com.kh.spring.admin.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.spring.admin.model.dao.AdminDAO;
import com.kh.spring.board.model.vo.Attachment;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.PageInfo;
import com.kh.spring.member.model.vo.Member;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	AdminDAO aDao;

	@Override
	public ArrayList<HashMap<String, Object>> newestBoardList() {
		return aDao.newestBoardList();
	}

	@Override
	public ArrayList<Member> selectMembers() {
		return aDao.selectMembers();
	}

	@Override
	public int updateInfo(Properties prop) {
		return aDao.updateInfo(prop);
	}

	@Override
	public int getListCount(int i) {
		return aDao.getListCount(i);
	}

	@Override
	public ArrayList<Board> selectBoardList(PageInfo pi, int i) {
		return aDao.selectBoardList(i, new RowBounds((pi.getCurrentPage()-1)*pi.getBoardLimit(), pi.getBoardLimit()));
	}

	@Override
	public int updateBoardStatus(HashMap<String, Object> map) {
		return aDao.updateBoardStatus(map);
	}

	@Override
	public int getAListCount(int i) {
		return aDao.getAListCount(i);
	}

	@Override
	public ArrayList<Attachment> selectAttmList() {
		return aDao.selectAttmList();
	}

	@Override
	public void updateAttmStatus(HashMap<String, Object> map) {
		aDao.updateAttmStatus(map);
	}

}
