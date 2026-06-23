package kr.co.prac.orders.exception;

import kr.co.prac.global.exception.BusinessException;
import kr.co.prac.global.exception.ErrorCode;

public class EmptyItemOrderException extends BusinessException {

	public EmptyItemOrderException() {
		super(ErrorCode.EMPTY_ITEM_ORDER);
	}

}
