package kr.co.prac.orders.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.prac.global.session.SessionUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import kr.co.prac.orders.dto.OrderCreateRequest;
import kr.co.prac.orders.dto.OrderDetailResponse;
import kr.co.prac.orders.dto.OrderResponse;
import kr.co.prac.orders.service.OrderService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OrdersController {
	
	private final OrderService orderService;
	
	// 단건
	@GetMapping("/orders/{orderId}")
	public OrderDetailResponse orderById(@PathVariable Long orderId, HttpServletRequest httpServletRequest) {
		Long loginMemberId = SessionUtil.getLoginMemberId(httpServletRequest);
		return orderService.findOne(orderId,loginMemberId);
	}
	
	// 생성
	@PostMapping("/orders")
	public OrderResponse createOrder(HttpServletRequest httpServletRequest, @RequestBody List<@Valid OrderCreateRequest> orderCreateRequest) {
		Long loginMemberId = SessionUtil.getLoginMemberId(httpServletRequest);
		return orderService.createOrder(loginMemberId, orderCreateRequest);
	}
	// 취소
	@PostMapping("/orders/{orderId}/cancel")
	public void cancelOrder(@PathVariable Long orderId, HttpServletRequest httpServletRequest) {
		Long loginMemberId = SessionUtil.getLoginMemberId(httpServletRequest);
		orderService.cancelOrder(orderId,loginMemberId);
	}
	

}
