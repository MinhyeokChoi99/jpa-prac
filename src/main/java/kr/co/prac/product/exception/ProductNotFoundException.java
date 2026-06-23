package kr.co.prac.product.exception;

import kr.co.prac.global.exception.BusinessException;
import kr.co.prac.global.exception.ErrorCode;

public class ProductNotFoundException extends BusinessException{

	public ProductNotFoundException() {
		super(ErrorCode.PRODUCT_NOT_FOUND);
	}

}
