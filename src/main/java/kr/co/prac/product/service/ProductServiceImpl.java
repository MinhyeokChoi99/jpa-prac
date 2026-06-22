package kr.co.prac.product.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.querydsl.jpa.impl.JPAQuery;

import kr.co.prac.product.dto.ProductResponse;
import kr.co.prac.product.repository.ProductRepository;
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
