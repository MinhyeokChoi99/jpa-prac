package kr.co.prac.product.dto;

import kr.co.prac.product.entity.Product;
import lombok.Getter;

@Getter
public class ProductResponse {
	private Long number;
	private String name;
	private int price;
	private int stock;
	
	public ProductResponse(Product product) {
		this.number = product.getNumber();
		this.name = product.getName();
		this.price = product.getPrice();
		this.stock = product.getStock();
	}
}
