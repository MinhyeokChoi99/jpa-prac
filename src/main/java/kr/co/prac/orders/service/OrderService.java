package kr.co.prac.orders.service;

import java.util.List;

import kr.co.prac.orders.dto.OrderCreateRequest;
import kr.co.prac.orders.dto.OrderDetailResponse;
import kr.co.prac.orders.dto.OrderResponse;

public interface OrderService {
	
	OrderDetailResponse createOrder(Long memberId, List<OrderCreateRequest> orderCreateRequests);

	OrderDetailResponse orderFromCart(Long memberId);
	
	OrderDetailResponse findOne(Long orderId,Long loginMemberId);
	
	List<OrderResponse> memberIdFound(Long memberId);
	
	List<OrderResponse> findAll();

	OrderDetailResponse findOneForAdmin(Long orderId);
	
	void cancelOrder(Long ordersId, Long loginMemberId);
}
