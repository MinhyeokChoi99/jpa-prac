package kr.co.prac.product.service;

import java.util.List;

import kr.co.prac.product.dto.ProductImageRequest;
import kr.co.prac.product.dto.ProductImageResponse;

public interface ProductImageService {

	List<ProductImageResponse> findProductImages(Long productNumber);
	
	ProductImageResponse addProductImage(Long productNumber, ProductImageRequest productImageRequest);
	
	ProductImageResponse updateProductImage(Long productNumber,Long productImageNumber, ProductImageRequest productImageRequest);
	
	ProductImageResponse markAsThumbnail(Long productNumber,Long ProductImageNumber);
	
	void deleteProductImage(Long productNumber, Long ProductImageNumber);
	
}
