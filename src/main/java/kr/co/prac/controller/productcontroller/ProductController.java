package kr.co.prac.controller.productcontroller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.prac.dto.product.ProductResponse;
import kr.co.prac.service.productService.ProductService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProductController {
	
	private final ProductService productServiceImpl;
	
	@GetMapping("/products")
	public List<ProductResponse> productList() {
		return productServiceImpl.productList();
	}
	

}
