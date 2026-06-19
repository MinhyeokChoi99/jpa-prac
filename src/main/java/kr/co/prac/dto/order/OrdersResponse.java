package kr.co.prac.dto.order;

import java.time.LocalDateTime;
import kr.co.prac.entity.Orders;
import kr.co.prac.entity.Status;
import lombok.Getter;

@Getter
public class OrdersResponse {
	private Long number;
//	private Member member; 엔티티가 딸려가면 X
	private Long memberId;
	private LocalDateTime orderDate;
	private Status status;
	
	public OrdersResponse(Orders orders) {
		this.number = orders.getNumber();
//		this.member = orders.getMember();
		this.memberId = orders.getMember().getNumber();
		this.orderDate = orders.getOrderDate();
		this.status = orders.getStatus();
	}
}
