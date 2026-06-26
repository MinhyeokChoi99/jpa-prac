package kr.co.prac.orders.controller;

import java.util.List;

import kr.co.prac.global.security.SecurityUtil;
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
	public OrderDetailResponse orderById(@PathVariable Long orderId) {
		Long memberId = SecurityUtil.getMemberId();
		return orderService.findOne(orderId, memberId);
	}
	
	// 주문 생성
	@PostMapping("/orders")
	public OrderDetailResponse createOrder(@RequestBody List<@Valid OrderCreateRequest> orderCreateRequest) {
		Long memberId = SecurityUtil.getMemberId();
		return orderService.createOrder(memberId, orderCreateRequest);
	}
	// 주문 취소
	@PostMapping("/orders/{orderId}/cancel")
	public void cancelOrder(@PathVariable Long orderId) {
		Long memberId = SecurityUtil.getMemberId();
		orderService.cancelOrder(orderId, memberId);
	}
	

}
