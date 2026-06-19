package kr.co.prac.controller.productcontorller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import kr.co.prac.dto.product.ProductResponse;
import kr.co.prac.repository.ProductRepository;
import kr.co.prac.service.productService.ProductService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ProductController {
	
	private final ProductService productServiceImpl;
	
	@GetMapping("/product")
	public List<ProductResponse> productList() {
		return productServiceImpl.productList();
	}
	

}
