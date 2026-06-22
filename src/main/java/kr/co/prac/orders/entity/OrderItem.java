package kr.co.prac.orders.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kr.co.prac.global.entity.BaseTimeEntity;
import kr.co.prac.product.entity.Product;
import lombok.Getter;
import lombok.Setter;
@Entity
@Getter @Setter
public class OrderItem extends BaseTimeEntity {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long number;
	
	@ManyToOne
	@JoinColumn(name = "order_id")
	private Orders orders;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
	
	private int orderPrice;
	
	private int count;
}
