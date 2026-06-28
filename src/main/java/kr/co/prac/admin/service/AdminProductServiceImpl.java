package kr.co.prac.admin.service;


import kr.co.prac.product.dto.ProductCreateRequest;
import kr.co.prac.product.dto.ProductResponse;
import kr.co.prac.product.dto.ProductUpdateRequest;
import kr.co.prac.product.entity.Product;
import kr.co.prac.product.entity.ProductStatus;
import kr.co.prac.product.exception.ProductNotFoundException;
import kr.co.prac.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminProductServiceImpl implements AdminProductService{

    private final ProductRepository productRepository;


    @Override
    public List<ProductResponse> productList() {
        return productRepository.findAll().stream().map(ProductResponse::new).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse find(Long productNumber) {
        Product product = productRepository.findById(productNumber).orElseThrow(ProductNotFoundException::new);
        return new ProductResponse(product);
    }

    @Override
    public ProductResponse create(ProductCreateRequest productCreateRequest) {
        Product product = Product.create(
                productCreateRequest.getName(),
                productCreateRequest.getPrice(),
                productCreateRequest.getStock(),
                productCreateRequest.getDescription()
                );

        productRepository.save(product);
        return new ProductResponse(product);

    }

    @Override
    public ProductResponse update(Long productNumber, ProductUpdateRequest productUpdateRequest) {
        Product product = productRepository.findById(productNumber).orElseThrow(ProductNotFoundException::new);
        product.update(
                productUpdateRequest.getName(),
                productUpdateRequest.getPrice(),
                productUpdateRequest.getStock(),
                productUpdateRequest.getDescription()
        );

        return new ProductResponse(product);

    }

    @Override
    public void delete(Long productNumber) {
        Product product = productRepository.findById(productNumber).orElseThrow(ProductNotFoundException::new);
        product.delete();
    }

    @Override
    @Transactional
    public ProductResponse hide(Long productNumber) {
        Product product = productRepository.findById(productNumber)
                .orElseThrow(ProductNotFoundException::new);

        product.hide();

        return new ProductResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse show(Long productNumber) {
        Product product = productRepository.findById(productNumber)
                .orElseThrow(ProductNotFoundException::new);

        product.show();

        return new ProductResponse(product);
    }

}
