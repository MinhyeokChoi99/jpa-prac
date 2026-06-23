package kr.co.prac.login.exception;

import kr.co.prac.global.exception.BusinessException;
import kr.co.prac.global.exception.ErrorCode;

public class InvalidPasswordException extends BusinessException {

	public InvalidPasswordException() {
		super(ErrorCode.INVALID_PASSWORD);
		// TODO Auto-generated constructor stub
	}

}
