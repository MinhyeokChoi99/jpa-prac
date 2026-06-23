package kr.co.prac.product.exception;

import kr.co.prac.global.exception.BusinessException;
import kr.co.prac.global.exception.ErrorCode;

public class NotEnoughStockException extends BusinessException{
	public NotEnoughStockException() {
		super(ErrorCode.NOT_ENOUGH_STOCK);
	}
}
