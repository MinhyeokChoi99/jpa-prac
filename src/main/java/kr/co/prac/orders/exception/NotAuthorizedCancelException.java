package kr.co.prac.orders.exception;

import kr.co.prac.global.exception.BusinessException;
import kr.co.prac.global.exception.ErrorCode;

public class NotAuthorizedCancelException extends BusinessException{

	public NotAuthorizedCancelException() {
		super(ErrorCode.NOT_AUTHORIZED_CANCEL);
	}

}
