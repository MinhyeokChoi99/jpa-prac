package kr.co.prac.product.entity;

import jakarta.persistence.*;
import kr.co.prac.global.entity.BaseTimeEntity;
import kr.co.prac.product.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Product extends BaseTimeEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long number;
	
	private String name;
	
	private int price;
	
	private int stock;

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
}
