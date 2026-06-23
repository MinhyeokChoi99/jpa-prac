package kr.co.prac.login.exception;

import kr.co.prac.global.exception.BusinessException;
import kr.co.prac.global.exception.ErrorCode;

public class LoginRequiredException extends BusinessException {
    public LoginRequiredException() {
        super(ErrorCode.LOGIN_REQUIRED);
    }
}
