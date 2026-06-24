package kr.co.prac.orders.dto;

import kr.co.prac.orders.entity.OrderItem;
import lombok.Getter;

@Getter
public class OrderItemResponse {

	private Long number;
	private String productName;
	private int productPrice;
	private int count;
	private int totalPrice;

	public OrderItemResponse(OrderItem orderItem) {
		this.number = orderItem.getNumber();
		this.productName = orderItem.getProduct().getName();
		this.productPrice = orderItem.getUnitPrice();
		this.count = orderItem.getCount();
		this.totalPrice = productPrice * count;
	}

}
