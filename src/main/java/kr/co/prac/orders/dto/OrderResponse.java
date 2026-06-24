package kr.co.prac.orders.dto;

import java.time.LocalDateTime;

import kr.co.prac.orders.entity.OrderStatus;
import kr.co.prac.orders.entity.Orders;
import lombok.Getter;

@Getter
public class OrderResponse {
	private Long number;
	private Long memberId;
	private LocalDateTime orderDate;
	private OrderStatus status;
	
	public OrderResponse(Orders orders) {
		this.number = orders.getNumber();
		this.memberId = orders.getMember().getNumber();
		this.orderDate = orders.getOrderDate();
		this.status = orders.getStatus();
	}
}
