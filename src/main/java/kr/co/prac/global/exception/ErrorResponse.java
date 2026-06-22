package kr.co.prac.global.exception;

import org.springframework.http.HttpStatus;

public class ErrorResponse {
	
	private HttpStatus httpstatus;
	private String errorMessage;
	
	public ErrorResponse(ErrorCode errorCode) {
		httpstatus = errorCode.getHttpStatus();
		errorMessage = errorCode.getErrorMessage();
	}
	
}
