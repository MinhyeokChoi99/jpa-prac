package kr.co.prac.admin.service;

import kr.co.prac.product.dto.ProductCreateRequest;
import kr.co.prac.product.dto.ProductResponse;
import kr.co.prac.product.dto.ProductUpdateRequest;

import java.util.List;

public interface AdminProductService {

    List<ProductResponse> productList();

    ProductResponse find(Long productNumber);

    ProductResponse create(ProductCreateRequest productCreateRequest);

    ProductResponse update(Long productNumber, ProductUpdateRequest productUpdateRequest);

    void delete(Long productNumber);

    ProductResponse hide(Long productNumber);

    ProductResponse show(Long productNumber);
}
