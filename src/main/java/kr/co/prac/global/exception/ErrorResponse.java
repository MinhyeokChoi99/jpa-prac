package kr.co.prac.global.exception;


public record ErrorResponse (int status, String code, String message) {
	public  static ErrorResponse from(ErrorCode errorCode) {
		return new ErrorResponse(errorCode.getHttpStatus().value(),
				errorCode.name(),
				errorCode.getErrorMessage());
	}
}
