package kr.co.prac.orders.exception;

import kr.co.prac.global.exception.BusinessException;
import kr.co.prac.global.exception.ErrorCode;

public class EmptyItemOrder extends BusinessException {

	public EmptyItemOrder() {
		super(ErrorCode.EMPTY_ITEM_ORDER);
	}

}
