package kr.co.prac.admin.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import kr.co.prac.product.dto.ProductImageRequest;
import kr.co.prac.product.dto.ProductImageResponse;
import kr.co.prac.product.service.ProductImageService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/admin/products/{productNumber}/images")
@RequiredArgsConstructor
public class AdminProductImageController {
	
	private final ProductImageService productImageService;
	
	@GetMapping
	public List<ProductImageResponse> listProductImages(@PathVariable Long productNumber) {
		return productImageService.findProductImages(productNumber);
	}
	
	@PostMapping
	public ProductImageResponse addProductImage(@PathVariable Long productNumber, @RequestBody ProductImageRequest productImageRequest) {
		return productImageService.addProductImage(productNumber, productImageRequest);
	}
	
	@PutMapping("/{productImageNumber}")
	public ProductImageResponse updateProductImage(@PathVariable Long productNumber,@PathVariable Long productImageNumber , @RequestBody @Valid ProductImageRequest productImageRequest) {
		return productImageService.updateProductImage(productNumber, productImageNumber, productImageRequest);
	}
	
	@PatchMapping("/{productImageNumber}/thumbnail")
	public ProductImageResponse markThumbnail(@PathVariable Long productNumber,@PathVariable Long productImageNumber) {
		return productImageService.markAsThumbnail(productNumber, productImageNumber);
	}
	
	@DeleteMapping("/{productImageNumber}")
	public ResponseEntity<Void> deleteProductImage(@PathVariable Long productNumber,@PathVariable Long productImageNumber) {
		productImageService.deleteProductImage(productNumber, productImageNumber);
		return ResponseEntity.noContent().build();
	}
		
	
	
}
