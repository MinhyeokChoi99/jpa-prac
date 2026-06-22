package kr.co.prac.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {
	//존재하지 않는 Member
	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 회원입니다."),
	//이미 존재하는 맴버
	ALREADY_EXIST_MEMBER(HttpStatus.BAD_REQUEST,"이미 존재하는 회원입니다"),
	//상품이 담기지 않음(빈주문)
	EMPTY_ITEM_ORDER(HttpStatus.BAD_REQUEST,"상품이 담기지 않았습니다"),
	//존재하지 않는 주문
	ORDER_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 주문입니다"),
	//이미 취소된 주문
	ALREADY_CANCELLED_ORDER(HttpStatus.BAD_REQUEST,"이미 취소된 주문입니다"),
	//재고부족
	NOT_ENOUGH_STOCK(HttpStatus.BAD_REQUEST,"재고가 부족합니다"),
	// 서버오류
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"서버 내류의 오류가 발생했습니다");

	private HttpStatus httpStatus;
	private String errorMessage;
	
	ErrorCode(HttpStatus httpStatus, String errorMessage) {
		this.httpStatus = httpStatus;
		this.errorMessage = errorMessage;
	}
}
