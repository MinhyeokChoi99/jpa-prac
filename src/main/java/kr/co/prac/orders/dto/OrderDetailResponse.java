package kr.co.prac.orders.dto;

import java.time.LocalDateTime;
import java.util.List;

import kr.co.prac.orders.entity.OrderStatus;
import kr.co.prac.orders.entity.Orders;
import lombok.Getter;

@Getter
public class OrderDetailResponse {
	private Long number;
	private Long memberId;
	private LocalDateTime orderDate;
	private OrderStatus status;
	private List<OrderItemResponse> orderItems;
	private int totalPrice;
	
	public OrderDetailResponse(Orders orders, List<OrderItemResponse> orderItems) {
		this.number = orders.getNumber();
		this.memberId = orders.getMember().getNumber();
		this.orderDate = orders.getOrderDate();
		this.status = orders.getStatus();
		this.orderItems = orderItems;
		this.totalPrice = orderItems.stream().mapToInt(i->i.getTotalPrice()).sum();
		
	}
	
	
}
