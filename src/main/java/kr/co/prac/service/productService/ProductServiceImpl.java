package kr.co.prac.service.productService;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.prac.dto.product.ProductResponse;
import kr.co.prac.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
	
	@Override
	public List<ProductResponse> productList() {
		return productRepository.findAll().stream().map(ProductResponse::new).toList();
	}

	
}
