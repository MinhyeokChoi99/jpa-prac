package kr.co.prac.orders.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class OrderCreateRequest {

	@NotNull(message = "제품필수")
	private Long productNumber;
	
	@NotNull(message = "수량필수")
	@Positive(message = "1건이상")
	private Integer count;
}
