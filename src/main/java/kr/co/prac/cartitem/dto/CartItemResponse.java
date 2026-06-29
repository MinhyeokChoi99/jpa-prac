package kr.co.prac.cartitem.dto;


import kr.co.prac.cartitem.entity.CartItem;
import lombok.Getter;

@Getter
public class CartItemResponse {

	private Long productId;
	
	private int count;
	
	public CartItemResponse(CartItem cartItem) {
		this.productId = cartItem.getProduct().getNumber();
		this.count = cartItem.getCount();
	}
	
}
