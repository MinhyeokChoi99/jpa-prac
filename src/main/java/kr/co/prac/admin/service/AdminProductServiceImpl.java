package kr.co.prac.admin.service;


import kr.co.prac.product.dto.ProductCreateRequest;
import kr.co.prac.product.dto.ProductResponse;
import kr.co.prac.product.dto.ProductUpdateRequest;
import kr.co.prac.product.entity.Product;
import kr.co.prac.product.exception.ProductNotFoundException;
import kr.co.prac.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminProductServiceImpl implements AdminProductService{

    private final ProductRepository productRepository;


    @Override
    @Transactional(readOnly = true)
    public ProductResponse find(Long productNumber) {
        Product product = productRepository.findById(productNumber).orElseThrow(ProductNotFoundException::new);
        return new ProductResponse(product);
    }

    @Override
    public ProductResponse create(ProductCreateRequest productCreateRequest) {
        Product product = new Product();
        product.setName(productCreateRequest.getName());
        product.setPrice(productCreateRequest.getPrice());
        product.setStock(productCreateRequest.getStock());
        product.setDescription(productCreateRequest.getDescription());
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
        productRepository.delete(product);
    }
}
