package kr.co.prac.product.dto;

import kr.co.prac.product.entity.ProductImage;
import lombok.Getter;

@Getter
public class ProductImageResponse {

	private Long number;

	private Long productNumber;

	private String imageUrl;

	private boolean thumbnail;

	private int sortOrder;

	public ProductImageResponse(ProductImage productImage) {
		this.number = productImage.getNumber();
		this.productNumber = productImage.getProduct().getNumber();
		this.imageUrl = productImage.getImageUrl();
		this.thumbnail = productImage.isThumbnail();
		this.sortOrder = productImage.getSortOrder();
	}
}