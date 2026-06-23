package kr.co.prac.member.exception;

import kr.co.prac.global.exception.BusinessException;
import kr.co.prac.global.exception.ErrorCode;

public class AlreadyExistMemberException extends BusinessException {
    public AlreadyExistMemberException() {
        super(ErrorCode.ALREADY_EXIST_MEMBER);
    }
}
