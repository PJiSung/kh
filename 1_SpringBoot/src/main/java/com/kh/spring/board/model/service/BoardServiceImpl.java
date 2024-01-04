package com.kh.spring.board.model.service;

import java.util.ArrayList;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.spring.board.model.dao.BoardDAO;
import com.kh.spring.board.model.vo.Attachment;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.PageInfo;
import com.kh.spring.board.model.vo.Reply;

@Service
public class BoardServiceImpl implements BoardService{
	
	@Autowired
	private BoardDAO bDAO;

	@Override
	public int getListCount(int i) {
		return bDAO.getListCount(i);
	}

	@Override
	public ArrayList<Board> selectBoardList(PageInfo pi, int i) {
		int offset = (pi.getCurrentPage() -1)*pi.getBoardLimit();	
		int limit = pi.getBoardLimit();								 
		
		RowBounds rowBounds = new RowBounds(offset, limit);
		return (ArrayList)bDAO.selectBoardList(i, rowBounds);
	}

	@Override
	public int insertBoard(Board b) {
		return bDAO.insertBoard(b);
	}

	@Override
	public ArrayList<Reply> selectReply(int boardId) {
		return bDAO.selectReply(boardId);
	}

	@Override
	public Board selectBoard(int boardId, String id) {
		Board b = bDAO.selectBoard(boardId);
		if(b!= null && !b.getBoardWriter().equals(id)) {
			bDAO.updateCount(boardId);
		}
		return b;
	}

	@Override
	public int insertReply(Reply r) {
		return bDAO.insertReply(r);
	}

	@Override
	public int updateBoard(Board b) {
		return bDAO.updateBoard(b);
	}

	@Override
	public int deleteBoard(int boardId) {
		return bDAO.deleteBoard(boardId);
	}

	@Override
	public ArrayList<Attachment> selectAttmBoardList(Integer boardId) {
		return bDAO.selectAttmBoardList(boardId);
	}

	@Override
	public int insertAttm(ArrayList<Attachment> list) {
		return bDAO.insertAttm(list);
	}

	@Override
	public int deleteAttm(ArrayList<String> delRename) {
		return bDAO.deleteAttm(delRename);
	}

	@Override
	public void updateAttmLevel(int boardId) {
		bDAO.updateAttmLevel(boardId);
	}

	@Override
	public int statusNAttm(int boardId) {
		return bDAO.statusNAttm(boardId);
	}


}
