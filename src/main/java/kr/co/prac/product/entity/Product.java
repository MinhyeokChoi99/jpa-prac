package kr.co.prac.product.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
	
	public void addStock(int quantity) {
		this.stock += quantity;
	}
	
	public void removeStock(int quantity) {
		if(this.stock - quantity < 0) {
			throw new NotEnoughStockException();
		}
		this.stock -= quantity;
	}
}
