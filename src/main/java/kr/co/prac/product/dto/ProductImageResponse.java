package kr.co.prac.product.dto;

import kr.co.prac.product.entity.ProductImage;
import lombok.Getter;

@Getter
public class ProductImageResponse {
	
	private Long productNumber;
	
	private String imageUrl;
	
	public ProductImageResponse(ProductImage productImage) {
		this.productNumber = productImage.getProduct().getNumber();
		this.imageUrl = productImage.getImageUrl();
	}

}
