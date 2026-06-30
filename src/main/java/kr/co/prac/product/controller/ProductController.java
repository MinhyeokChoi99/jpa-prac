package kr.co.prac.product.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import kr.co.prac.product.dto.ProductImageResponse;
import kr.co.prac.product.dto.ProductResponse;
import kr.co.prac.product.service.ProductImageService;
import kr.co.prac.product.service.ProductService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProductController {
	
	private final ProductService productService;
	private final ProductImageService productImageService;
	
	@GetMapping("/products")
	public List<ProductResponse> productList() {
		return productService.productList();
	}
	
	@GetMapping("/products/{productNumber}")
	public List<ProductImageResponse> productImageList(@PathVariable Long productNumber) {
		return productImageService.findProductImages(productNumber);
	}
}
