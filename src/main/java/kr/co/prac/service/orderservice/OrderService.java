package kr.co.prac.service.orderservice;

import java.util.List;

import kr.co.prac.dto.order.OrderCreateRequest;
import kr.co.prac.dto.order.OrdersResponse;
import kr.co.prac.entity.Orders;

public interface OrderService {
	
	OrdersResponse createOrder(List<OrderCreateRequest> orderCreateRequests);
	
	OrdersResponse findOne(Long orderId);
	
	List<OrdersResponse> memberIdFound(Long memberId);
	
	List<OrdersResponse> findAll();
	
	void deleteOrders(Long ordersId);
	
	
	
	
	
	
}
