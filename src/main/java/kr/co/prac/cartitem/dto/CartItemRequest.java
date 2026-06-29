package kr.co.prac.cartitem.dto;

import lombok.Getter;

@Getter

public class CartItemRequest {
	
	
	private Long memberId;
	
	private Long productId;
	
	private int count;
}
