package kr.co.prac.product.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.prac.product.dto.ProductImageRequest;
import kr.co.prac.product.dto.ProductImageResponse;
import kr.co.prac.product.entity.Product;
import kr.co.prac.product.entity.ProductImage;
import kr.co.prac.product.exception.NoImageException;
import kr.co.prac.product.exception.ProductNotFoundException;
import kr.co.prac.product.repository.ProductImageRepository;
import kr.co.prac.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductImageServiceImpl implements ProductImageService{
	
	private final ProductImageRepository productImageRepository;
	private final ProductRepository productRepository;
	
	@Override
	@Transactional(readOnly = true)
	public List<ProductImageResponse> findProductImages(Long productNumber) {
		productRepository.findById(productNumber).orElseThrow(ProductNotFoundException::new);
		
		return productImageRepository.findByProductNumberOrderBySortOrderAsc(productNumber).stream().map(ProductImageResponse::new).toList();
		
	}

	@Override
	public ProductImageResponse addProductImage(Long productNumber,ProductImageRequest productImageRequest) {
		Product product = productRepository.findById(productNumber).orElseThrow(ProductNotFoundException::new);
		boolean isFirst = false;
		
		int nextSortOrder = findNextSortOrder(productNumber);
		if(nextSortOrder == 1) {
			isFirst = true;
		}
		ProductImage productImage = ProductImage.create(product, productImageRequest.getImageUrl(), isFirst, nextSortOrder);
		productImageRepository.save(productImage);
		return new ProductImageResponse(productImage);
	}
	
	@Override
	public ProductImageResponse updateProductImage(Long productNumber,Long productImageNumber ,ProductImageRequest productImageRequest) {
		productRepository.findById(productNumber).orElseThrow(ProductNotFoundException::new);
		ProductImage productImage = productImageRepository.findByNumberAndProductNumber(productImageNumber, productNumber).orElseThrow(NoImageException::new);
		productImage.update(productImageRequest.getImageUrl());
		return new ProductImageResponse(productImage);
	}


	@Override
	public ProductImageResponse markAsThumbnail(Long productNumber, Long productImageNumber) {

		ProductImage productImage = productImageRepository.findByNumberAndProductNumber(productImageNumber, productNumber).orElseThrow(NoImageException::new);
		
		List<ProductImage> productImages = productImageRepository.findByProductNumberAndThumbnailTrue(productNumber);
		
		for (ProductImage p : productImages) {
			p.unmarkAsThumbnail();
		}
		
		productImage.markAsThumbnail();
		
		return new ProductImageResponse(productImage);
	}

	@Override
	public void deleteProductImage(Long productNumber, Long productImageNumber) {
		ProductImage productImage = productImageRepository.findByNumberAndProductNumber(productImageNumber, productNumber).orElseThrow(NoImageException::new);
		
		boolean isThumbnail = productImage.isThumbnail();
		productImageRepository.delete(productImage);
		productImageRepository.flush();
		
		if(isThumbnail) {
			Optional<ProductImage> restImage = productImageRepository.findTopByProductNumberOrderBySortOrder(productNumber);
			if(restImage.isPresent()) {
				restImage.get().markAsThumbnail();
			}
		}
	}
	
	private int findNextSortOrder(Long productNumber) {
		Optional<ProductImage> productOptional = productImageRepository.findTopByProductNumberOrderBySortOrderDesc(productNumber);
		if(productOptional.isPresent()) {
			return productOptional.get().getSortOrder() + 1;
		} else {
			return 1;
		}
	}


}
