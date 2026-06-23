package kr.co.prac.global.exception;

public class InvalidInputValue extends BusinessException{

	public InvalidInputValue() {
		super(ErrorCode.INVALID_INPUT_VALUE);
	}

}
