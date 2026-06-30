package kr.co.prac.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ProductImageRequest {
	
	@NotNull
	private Long productNumber;
	
	@NotBlank(message = "url반드시 포함")
	private String imageUrl;
}
