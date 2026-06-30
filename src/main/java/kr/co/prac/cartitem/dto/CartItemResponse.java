package kr.co.prac.cartitem.dto;


import kr.co.prac.cartitem.entity.CartItem;
import lombok.Getter;

@Getter
public class CartItemResponse {

	private Long productId;
	
	private int count;
	
	private String thumbnailImageUrl;
	
	public CartItemResponse(CartItem cartItem, String thumbnailImageUrl) {
		this.productId = cartItem.getProduct().getNumber();
		this.count = cartItem.getCount();
		this.thumbnailImageUrl = thumbnailImageUrl;
	}
	
}
