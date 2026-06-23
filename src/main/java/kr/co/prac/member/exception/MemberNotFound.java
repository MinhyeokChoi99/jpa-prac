package kr.co.prac.member.exception;

import kr.co.prac.global.exception.BusinessException;
import kr.co.prac.global.exception.ErrorCode;

public class MemberNotFound extends BusinessException {
    public MemberNotFound() {
        super(ErrorCode.MEMBER_NOT_FOUND);
    }
}
