package kr.co.prac.cartitem.exception;

import kr.co.prac.global.exception.BusinessException;
import kr.co.prac.global.exception.ErrorCode;

public class EmptyCartItemException extends BusinessException {

	public EmptyCartItemException() {
		super(ErrorCode.EMPTY_CART_ITEM);
	}

}
