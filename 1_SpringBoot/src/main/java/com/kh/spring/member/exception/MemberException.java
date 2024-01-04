package com.kh.spring.member.exception;

public class MemberException extends RuntimeException {
	public MemberException() {}
	public MemberException(String msg) {
		super(msg);
	}

}
