package kr.co.prac.dto;

import jakarta.validation.constraints.NotNull;
import kr.co.prac.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
public class OrderCreateRequest {

	@NotNull(message = "id필수")
	private Long memberId;
	
	@NotNull(message = "제품필수")
	private Long productNumber;
	
	@NotNull(message = "수량필수")
	private Integer count;
}
