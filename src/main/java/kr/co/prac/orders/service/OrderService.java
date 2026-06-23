package kr.co.prac.orders.service;

import java.util.List;

import kr.co.prac.orders.dto.OrderCreateRequest;
import kr.co.prac.orders.dto.OrdersResponse;

public interface OrderService {
	
	OrdersResponse createOrder(Long memberId, List<OrderCreateRequest> orderCreateRequests);
	
	OrdersResponse findOne(Long orderId);
	
	List<OrdersResponse> memberIdFound(Long memberId);
	
	List<OrdersResponse> findAll();
	
	void deleteOrders(Long ordersId);
	
	
	
	
	
	
}
