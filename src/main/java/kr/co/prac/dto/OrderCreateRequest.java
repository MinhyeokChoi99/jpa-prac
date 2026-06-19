package kr.co.prac.dto;

import kr.co.prac.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
public class OrderCreateRequest {

	private Long memberId;
	
	private Long productNumber;
	
	private Integer count;
}
