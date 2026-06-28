package kr.co.prac.product.dto;

import kr.co.prac.product.entity.Product;
import kr.co.prac.product.entity.ProductStatus;
import lombok.Getter;

@Getter
public class ProductResponse {
	private Long number;
	private String name;
	private Integer price;
	private Integer stock;
	private String description;
	private ProductStatus productStatus;
	
	public ProductResponse(Product product) {
		this.number = product.getNumber();
		this.name = product.getName();
		this.price = product.getPrice();
		this.stock = product.getStock();
		this.description = product.getDescription();
		this.productStatus = product.getProductStatus();
	}
}
