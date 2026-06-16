package kr.co.prac.dto;

import java.time.LocalDateTime;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import kr.co.prac.entity.Member;
import kr.co.prac.entity.Orders;
import kr.co.prac.entity.Status;
import lombok.Getter;

@Getter
public class OrdersResponse {
	private Long number;
	private Member member;
	private LocalDateTime order_date;
	@Enumerated(EnumType.STRING)
	private Status status;
	
	public OrdersResponse(Orders orders) {
		this.number = orders.getNumber();
		this.member = orders.getMember();
		this.order_date = orders.getOrder_date();
		this.status = orders.getStatus();
	}
}
