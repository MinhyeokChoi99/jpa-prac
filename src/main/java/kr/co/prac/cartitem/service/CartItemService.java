package kr.co.prac.cartitem.service;

import java.util.List;

import kr.co.prac.cartitem.dto.CartItemRequest;
import kr.co.prac.cartitem.dto.CartItemResponse;

public interface CartItemService {
	
	/**
	 *  조회, 생성, 수량수정, 삭제
	 */
	List<CartItemResponse> viewCart(Long memberId);
	
	CartItemResponse addItem(Long memberId, CartItemRequest cartItemRequest);
	
	CartItemResponse addCount(Long memberId, Long cartItemId);
	
	void subCount(Long memberId, Long cartItemId);
	
	void deleteItem(Long memberId, Long cartItemId);
}
