package kr.co.prac.global.exception;

public class InvalidInputValueException extends BusinessException{

	public InvalidInputValueException() {
		super(ErrorCode.INVALID_INPUT_VALUE);
	}

}
