package kr.co.prac.orders.dto;

import kr.co.prac.orders.entity.OrderItem;
import lombok.Getter;

@Getter
public class OrderItemResponse {

	private Long number;
	private String productName;
	private int unitPrice;
	private int count;
	private int totalPrice;
	private String thumbnailUrl;

	public OrderItemResponse(String productName, int unitPrice, int count, String thumbnailUrl) {
		this.productName = productName;
		this.unitPrice = unitPrice;
		this.count = count;
		this.totalPrice = unitPrice * count;
		this.thumbnailUrl = thumbnailUrl;
	}

	public OrderItemResponse(OrderItem orderItem, String thumbnailUrl) {
		this.productName = orderItem.getProduct().getName();
		this.unitPrice = orderItem.getUnitPrice();
		this.count = orderItem.getCount();
		this.totalPrice = unitPrice * count;
		this.thumbnailUrl = thumbnailUrl;
	}

}
