package kr.co.prac.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {
	//존재하지 않는 Member
	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 회원입니다."),
	//이미 존재하는 맴버
	ALREADY_EXIST_MEMBER(HttpStatus.CONFLICT,"이미 존재하는 회원입니다"),
	//상품이 담기지 않음(빈주문)
	EMPTY_ITEM_ORDER(HttpStatus.BAD_REQUEST,"상품이 담기지 않았습니다"),
	//존재하지 않는 주문
	ORDER_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 주문입니다"),
	//이미 취소된 주문
	ALREADY_CANCELLED_ORDER(HttpStatus.BAD_REQUEST,"이미 취소된 주문입니다"),
	//재고부족
	NOT_ENOUGH_STOCK(HttpStatus.BAD_REQUEST,"재고가 부족합니다"),
	//존재하지 않는 상품을 주문하려고 할때
	PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다"),
	//존재하지 않는 상품을 주문하려고 할때
	CARTITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "장바구니에 존재하지 않는 상품입니다"),
	//장바구니가 비었는데 주문하려고 할 때
	EMPTY_CART_ITEM(HttpStatus.BAD_REQUEST, "장바구니가 비어있습니다"),
	// INPUT을 잘못주었을때
	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "잘못된 입력값입니다"),
	// 비밀번호를 잘못주었을때
	INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "잘못된 비밀번호입니다"),
	// 로그인이 안되었을 떄
	LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다"),
	// 자신의 주문이 아닐 때
	NOT_AUTHORIZED_CANCEL(HttpStatus.FORBIDDEN, "주문 취소 권한이 없습니다"),
	// 자신의 주문이 아닐 때
	NOT_AUTHORIZED_MEMBER(HttpStatus.FORBIDDEN, "조회 권한이 없는 회원입니다"),
	// 관리자가 아닐 때
	NOT_AUTHORIZED_ADMIN(HttpStatus.FORBIDDEN, "관리자 권한이 필요합니다"),
	// 서버오류 -> GlobalExceptionHandler(Exception.class)
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"서버 내부의 오류가 발생했습니다");

	private final HttpStatus httpStatus;
	private final String errorMessage;
	
	ErrorCode(HttpStatus httpStatus, String errorMessage) {
		this.httpStatus = httpStatus;
		this.errorMessage = errorMessage;
	}
}
