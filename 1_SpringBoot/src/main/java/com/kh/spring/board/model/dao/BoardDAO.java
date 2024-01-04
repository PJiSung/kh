package com.kh.spring.board.model.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.kh.spring.board.model.vo.Attachment;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.PageInfo;
import com.kh.spring.board.model.vo.Reply;

@Mapper
public interface BoardDAO {

	int getListCount(int i);

	ArrayList<Board> selectBoardList(int i, RowBounds rowBounds);

	int insertBoard(Board b);

	ArrayList<Reply> selectReply(int boardId);

	Board selectBoard(int boardId);

	int updateCount(int boardId);

	int insertReply(Reply r);

	int updateBoard(Board b);

	int deleteBoard(int boardId);

	ArrayList<Attachment> selectAttmBoardList(Integer boardId);

	int insertAttm(ArrayList<Attachment> list);

	int deleteAttm(ArrayList<String> delRename);

	void updateAttmLevel(int boardId);

	int statusNAttm(int boardId);

	
}
