package kr.co.prac.product.service;

import java.util.List;

import kr.co.prac.product.entity.ProductStatus;
import org.springframework.stereotype.Service;


import kr.co.prac.product.dto.ProductResponse;
import kr.co.prac.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
	
	@Override
	public List<ProductResponse> productList() {
		return productRepository.findAllByProductStatus(ProductStatus.ACTIVE).stream().map(ProductResponse::new).toList();
	}

	
}
