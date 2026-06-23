package kr.co.prac.orders.exception;

import kr.co.prac.global.exception.BusinessException;
import kr.co.prac.global.exception.ErrorCode;

public class AlreadyCancelledOrderException extends BusinessException{

	public AlreadyCancelledOrderException() {
		super(ErrorCode.ALREADY_CANCELLED_ORDER);
	}
	
}
