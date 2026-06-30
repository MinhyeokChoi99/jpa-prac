package kr.co.prac.cartitem.dto;


import kr.co.prac.cartitem.entity.CartItem;
import lombok.Getter;

@Getter
public class CartItemResponse {

	private Long productId;
	
	private int count;
	
	private String thumbnailImageUrl;
	
	public CartItemResponse(Long productId, int count, String thumbnailImageUrl) {
		this.productId = productId;
		this.count = count;
		this.thumbnailImageUrl = thumbnailImageUrl;
	}
	
}
