package kr.co.prac.cartitem.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.prac.cartitem.dto.CartItemResponse;
import kr.co.prac.cartitem.service.CartItemService;
import kr.co.prac.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cart/items")
@RequiredArgsConstructor
public class CartItemController {
	
	private final CartItemService cartItemService;
	
	// 조회
	@GetMapping
	public List<CartItemResponse> viewCart(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		return cartItemService.viewCart(customUserDetails.getMemberId());
	}
	
	// 장바구니 등록
	
	
	// 증가, 감소
	@PatchMapping("/{cartItemId}/increase")
	public CartItemResponse addItemCount(@PathVariable Long cartItemId ,@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		return cartItemService.addCount(customUserDetails.getMemberId(), cartItemId);
	}
	
	@PatchMapping("/{cartItemId}/decrease")
	public  ResponseEntity<Void> subItemCount(@PathVariable Long cartItemId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
		cartItemService.subCount(customUserDetails.getMemberId(), cartItemId);
		 return ResponseEntity.noContent().build();
	}
	
	// 삭제
	@DeleteMapping("/{cartItemId}")
	public ResponseEntity<Void> deleteCartItem(@PathVariable Long cartItemId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
		cartItemService.deleteItem(customUserDetails.getMemberId(), cartItemId);
		 return ResponseEntity.noContent().build();
	}
	
}
