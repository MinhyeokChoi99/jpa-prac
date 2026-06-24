package kr.co.prac.admin.exception;

import kr.co.prac.global.exception.BusinessException;
import kr.co.prac.global.exception.ErrorCode;

public class NotAuthorizedAdminException extends BusinessException{

	public NotAuthorizedAdminException() {
		super(ErrorCode.NOT_AUTHORIZED_ADMIN);
		
	}

}
