package kr.co.prac.product.exception;

import kr.co.prac.global.exception.BusinessException;
import kr.co.prac.global.exception.ErrorCode;

public class ProductNotFound extends BusinessException{

	public ProductNotFound() {
		super(ErrorCode.PRODUCT_NOT_FOUND);
	}

}
