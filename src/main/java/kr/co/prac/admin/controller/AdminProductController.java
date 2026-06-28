package kr.co.prac.admin.controller;


import jakarta.validation.Valid;
import kr.co.prac.admin.service.AdminProductService;
import kr.co.prac.product.dto.ProductCreateRequest;
import kr.co.prac.product.dto.ProductResponse;
import kr.co.prac.product.dto.ProductUpdateRequest;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/products")
public class AdminProductController {
	/**
	 * 관리자는 목록 추가, 수정, 삭제
	 */

	private final AdminProductService adminProductService;

	@GetMapping("/{productNumber}")
	public ProductResponse findProduct(@PathVariable Long productNumber) {
		return adminProductService.find(productNumber);
	}

	@PostMapping
	public ProductResponse createProduct(@RequestBody @Valid ProductCreateRequest productCreateRequest) {
		return adminProductService.create(productCreateRequest);
	}

	@PutMapping("/{productNumber}")
	public ProductResponse updateProduct(@PathVariable Long productNumber, @RequestBody @Valid ProductUpdateRequest productUpdateRequest) {
		return adminProductService.update(productNumber, productUpdateRequest);
	}

	@DeleteMapping("/{productNumber}")
	public void deleteProduct(@PathVariable Long productNumber) {
		adminProductService.delete(productNumber);
	}

}
