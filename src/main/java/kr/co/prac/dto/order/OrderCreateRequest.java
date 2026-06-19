package kr.co.prac.dto.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
	@Positive(message = "1건이상")
	private Integer count;
}
