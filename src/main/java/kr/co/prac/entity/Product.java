package kr.co.prac.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Product {

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
			throw new IllegalArgumentException("0미만 불가");
		}
		this.stock -= quantity;
	}
}
