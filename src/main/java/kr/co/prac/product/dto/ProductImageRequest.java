package kr.co.prac.product.dto;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class ProductImageRequest {
	

	
	@NotBlank(message = "url반드시 포함")
	private String imageUrl;
}
