package kr.co.prac.product.exception;

import kr.co.prac.global.exception.BusinessException;
import kr.co.prac.global.exception.ErrorCode;

public class NotEnoughStock extends BusinessException{
	public NotEnoughStock() {
		super(ErrorCode.NOT_ENOUGH_STOCK);
	}
}
