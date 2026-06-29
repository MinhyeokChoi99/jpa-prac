package kr.co.prac.orders.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kr.co.prac.global.entity.BaseTimeEntity;

import kr.co.prac.product.entity.Product;
import lombok.Getter;

@Entity
@Getter
public class OrderItem extends BaseTimeEntity {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long number;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private Orders orders;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;
	
	private int unitPrice;
	
	private int count;
	
	public static OrderItem create(Orders orders,Product product, int count) {
		OrderItem orderItem = new OrderItem();
        orderItem.orders = orders;
        orderItem.product = product;
        orderItem.unitPrice = product.getPrice();
        orderItem.count = count;
        return orderItem;
        
    }
	
	public int getTotalPrice() {
	    return unitPrice * count;
	}
}
