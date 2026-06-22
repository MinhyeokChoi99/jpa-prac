package kr.co.prac.member.exception;

import kr.co.prac.global.exception.BusinessException;
import kr.co.prac.global.exception.ErrorCode;

public class AlreadyExistMember extends BusinessException {
    public AlreadyExistMember() {
        super(ErrorCode.ALREADY_EXIST_MEMBER);
    }
}
