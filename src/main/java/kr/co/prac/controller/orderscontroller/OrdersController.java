package kr.co.prac.controller.orderscontroller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import kr.co.prac.dto.OrderCreateRequest;
import kr.co.prac.dto.OrdersResponse;
import kr.co.prac.service.orderservice.OrderService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OrdersController {
	
	private final OrderService orderServiceImpl;
	
	// 전체
	@GetMapping("/orders")
	public List<OrdersResponse> orderList() {
		return orderServiceImpl.findAll();
	}
	// 단건
	@GetMapping("/orders/{orderId}")
	public OrdersResponse orderById(@PathVariable Long orderId) {
		return orderServiceImpl.findOne(orderId);
	}
	
	// 맴버기준조회
		@GetMapping("/orders/{memberId}/orders")
		public List<OrdersResponse> orderByMemberId(@PathVariable Long memberId) {
			List<OrdersResponse> memberIdFound = orderServiceImpl.memberIdFound(memberId);
			return memberIdFound;
		}
	
	// 생성
	@PostMapping("/orders")
	public OrdersResponse createOrder(@RequestBody @Valid List<OrderCreateRequest> orderCreateRequest) {
		return orderServiceImpl.createOrder(orderCreateRequest);
	}
	// 삭제
	@DeleteMapping("/orders/{orderId}")
	public void deleteOrder(@PathVariable Long orderId) {
		orderServiceImpl.deleteOrders(orderId);
	}
	

}
