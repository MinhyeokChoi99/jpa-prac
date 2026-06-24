package kr.co.prac.orders.exception;

import kr.co.prac.global.exception.BusinessException;
import kr.co.prac.global.exception.ErrorCode;

public class NotAuthorizedMemberException extends BusinessException{

	public NotAuthorizedMemberException() {
		super(ErrorCode.NOT_AUTHORIZED_MEMBER);
		
	}

}
