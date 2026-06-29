package kr.co.prac.cartitem.exception;

import kr.co.prac.global.exception.BusinessException;
import kr.co.prac.global.exception.ErrorCode;

public class CartItemNotFoundException extends BusinessException {

	public CartItemNotFoundException() {
		super(ErrorCode.CARTITEM_NOT_FOUND);
	}

}
