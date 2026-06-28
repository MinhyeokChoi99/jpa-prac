package kr.co.prac.admin.service;

import kr.co.prac.product.dto.ProductCreateRequest;
import kr.co.prac.product.dto.ProductResponse;
import kr.co.prac.product.dto.ProductUpdateRequest;

public interface AdminProductService {

    ProductResponse find(Long productNumber);

    ProductResponse create(ProductCreateRequest productCreateRequest);

    ProductResponse update(Long productNumber, ProductUpdateRequest productUpdateRequest);

    void delete(Long productNumber);

    ProductResponse hide(Long productNumber);

    ProductResponse show(Long productNumber);
}
