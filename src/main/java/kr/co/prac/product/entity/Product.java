package kr.co.prac.product.entity;

import jakarta.persistence.*;
import kr.co.prac.global.entity.BaseTimeEntity;
import kr.co.prac.product.exception.NotEnoughStockException;
import lombok.Getter;

@Entity
@Getter
public class Product extends BaseTimeEntity {

	protected Product() {}

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long number;
	
	private String name;
	
	private int price;
	
	private int stock;

	@Enumerated(EnumType.STRING)
	private ProductStatus productStatus;

	@Column(length = 1000) // 기본 255 이니 확장
	private String description;


	
	public void addStock(int quantity) {
		this.stock += quantity;
	}
	
	public void removeStock(int quantity) {
		if(this.stock - quantity < 0) {
			throw new NotEnoughStockException();
		}
		this.stock -= quantity;
	}

	public void update(String name, int price, int stock, String description) {
		this.name = name;
		this.price = price;
		this.stock = stock;
		this.description = description;
	}

	public static Product create(String name, int price, int stock, String description) {
		Product product = new Product();
		product.name = name;
		product.price = price;
		product.stock = stock;
		product.description = description;
		product.productStatus = ProductStatus.ACTIVE;
		return product;
	}

	public void hide() {
		this.productStatus = ProductStatus.HIDDEN;
	}

	public void show() {
		this.productStatus = ProductStatus.ACTIVE;
	}

	public void delete() {
		this.productStatus = ProductStatus.DELETED;
	}
}
