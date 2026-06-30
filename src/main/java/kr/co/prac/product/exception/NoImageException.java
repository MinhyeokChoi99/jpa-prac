package kr.co.prac.product.exception;

import kr.co.prac.global.exception.BusinessException;
import kr.co.prac.global.exception.ErrorCode;

public class NoImageException extends BusinessException{

	public NoImageException() {
		super(ErrorCode.NO_SUCH_IMAGE);
		// TODO Auto-generated constructor stub
	}

}
