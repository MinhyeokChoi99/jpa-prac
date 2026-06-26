package kr.co.prac.product.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.prac.product.dto.ProductResponse;
import kr.co.prac.product.service.ProductService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProductController {
	
	private final ProductService productService;
	
	@GetMapping("/products")
	public List<ProductResponse> productList() {
		return productService.productList();
	}
}
