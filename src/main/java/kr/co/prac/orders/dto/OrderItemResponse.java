package kr.co.prac.orders.dto;

import kr.co.prac.orders.entity.OrderItem;
import lombok.Getter;

@Getter
public class OrderItemResponse {

	private Long number;
	private String product;
	private int orderPrice;
	private int count;

	public OrderItemResponse(OrderItem orderItem) {
		this.number = orderItem.getNumber();
		this.product = orderItem.getProduct().getName();
		this.orderPrice = orderItem.getOrderPrice();
		this.count = orderItem.getCount();
	}

}
