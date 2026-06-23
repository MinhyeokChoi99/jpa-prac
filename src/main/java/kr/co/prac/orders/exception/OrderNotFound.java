package kr.co.prac.orders.exception;

import kr.co.prac.global.exception.BusinessException;
import kr.co.prac.global.exception.ErrorCode;

public class OrderNotFound extends BusinessException{

	public OrderNotFound() {
		super(ErrorCode.ORDER_NOT_FOUND);
	}

}
