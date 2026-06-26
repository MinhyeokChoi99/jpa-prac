package kr.co.prac.orders.controller;

import java.util.List;

import kr.co.prac.global.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import kr.co.prac.orders.dto.OrderCreateRequest;
import kr.co.prac.orders.dto.OrderDetailResponse;
import kr.co.prac.orders.service.OrderService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OrdersController {
	
	private final OrderService orderService;
	
	// 단건 조회
	@GetMapping("/orders/{orderId}")
	public OrderDetailResponse orderById(@PathVariable Long orderId, @AuthenticationPrincipal CustomUserDetails userDetails) {
		return orderService.findOne(orderId, userDetails.getMemberId());
	}
	
	// 주문 생성
	@PostMapping("/orders")
	public OrderDetailResponse createOrder(@RequestBody List<@Valid OrderCreateRequest> orderCreateRequest, @AuthenticationPrincipal CustomUserDetails userDetails) {
		return orderService.createOrder( userDetails.getMemberId(), orderCreateRequest);
	}
	// 주문 취소
	@PostMapping("/orders/{orderId}/cancel")
	public void cancelOrder(@PathVariable Long orderId, @AuthenticationPrincipal CustomUserDetails userDetails) {
		orderService.cancelOrder(orderId, userDetails.getMemberId());
	}
	

}
