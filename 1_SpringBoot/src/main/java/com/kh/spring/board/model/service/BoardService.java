package com.kh.spring.board.model.service;

import java.util.ArrayList;

import com.kh.spring.board.model.vo.Attachment;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.PageInfo;
import com.kh.spring.board.model.vo.Reply;

public interface BoardService {

	int getListCount(int i);

	ArrayList<Board> selectBoardList(PageInfo pi, int i);

	int insertBoard(Board b);

	ArrayList<Reply> selectReply(int boardId);

	Board selectBoard(int boardId, String id);

	int insertReply(Reply r);

	int updateBoard(Board b);

	int deleteBoard(int boardId);

	ArrayList<Attachment> selectAttmBoardList(Integer boardId);

	int insertAttm(ArrayList<Attachment> list);

	int deleteAttm(ArrayList<String> delRename);

	void updateAttmLevel(int boardId);

	int statusNAttm(int boardId);


}
