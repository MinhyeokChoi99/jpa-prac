package kr.co.prac.orders.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.prac.member.exception.MemberNotFoundException;
import kr.co.prac.member.repository.MemberRepository;
import kr.co.prac.orders.dto.OrderCreateRequest;
import kr.co.prac.orders.dto.OrdersResponse;
import kr.co.prac.orders.entity.OrderItem;
import kr.co.prac.orders.entity.OrderStatus;
import kr.co.prac.orders.entity.Orders;
import kr.co.prac.orders.exception.AlreadyCancelledOrderException;
import kr.co.prac.orders.exception.EmptyItemOrderException;
import kr.co.prac.orders.exception.OrderNotFoundException;
import kr.co.prac.orders.repository.OrderItemRepository;
import kr.co.prac.orders.repository.OrdersRepository;
import kr.co.prac.product.entity.Product;
import kr.co.prac.product.exception.ProductNotFoundException;
import kr.co.prac.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService{

	private final OrdersRepository ordersRepository;
	private final MemberRepository memberRepository;
	private final OrderItemRepository orderItemRepository;
	private final ProductRepository productRepository;

	
	@Override // 주문생성 -> 여러건 처리할수있게 수정
	public OrdersResponse createOrder(List<OrderCreateRequest> orderCreateRequests) {
		// 빈리스트로 요청이 들어왔을 경우 검증
		if(orderCreateRequests == null || orderCreateRequests.isEmpty()) {
			throw new EmptyItemOrderException();
		}
		Orders order = new Orders();
		order.setMember(memberRepository.findById(orderCreateRequests.get(0).getMemberId()).orElseThrow(MemberNotFoundException::new));
		order.setOrderDate(LocalDateTime.now());
		order.setStatus(OrderStatus.READY);
		Orders savedOrder = ordersRepository.save(order);
		
		for(OrderCreateRequest orderCreateRequest : orderCreateRequests) {
			Product product = productRepository.findById(orderCreateRequest.getProductNumber()).orElseThrow(ProductNotFoundException::new);
			product.removeStock(orderCreateRequest.getCount());
			
			OrderItem orderItem = new OrderItem();
			orderItem.setCount(orderCreateRequest.getCount());
			orderItem.setOrders(savedOrder);
			orderItem.setProduct(product);
			orderItem.setOrderPrice(product.getPrice() * orderCreateRequest.getCount());
			orderItemRepository.save(orderItem);
		}
		
		return new OrdersResponse(savedOrder);
	}


	@Override// 단건 조회
	@Transactional(readOnly = true)
	public OrdersResponse findOne(Long orderId) {
		Orders order = ordersRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
		OrdersResponse ordersResponse = new OrdersResponse(order);
		return ordersResponse;
		
	}


	@Override// 전체조회
	@Transactional(readOnly = true)
	public List<OrdersResponse> findAll() {
		List<OrdersResponse> ordersResponses = new ArrayList<>();
		for(Orders order : ordersRepository.findAll()) {
			ordersResponses.add(new OrdersResponse(order));
		}
		return ordersResponses;
	}


	@Override// 삭제
	public void deleteOrders(Long ordersId) {
		Orders orders = ordersRepository.findById(ordersId).orElseThrow(OrderNotFoundException::new);
		if(orders.getStatus() == OrderStatus.CANCEL) {
			throw new AlreadyCancelledOrderException();
		}
		List<OrderItem> orderItems = orderItemRepository.findByOrdersNumber(ordersId);
		for(OrderItem orderItem : orderItems) {		
			Product product = orderItem.getProduct();
			product.addStock(orderItem.getCount());
		}
		orders.setStatus(OrderStatus.CANCEL);
	}


	@Override
	public List<OrdersResponse> memberIdFound(Long memberId) {
		List<Orders> memberOrders = ordersRepository.findByMemberNumber(memberId);
		List<OrdersResponse> list = memberOrders.stream().map(OrdersResponse::new).toList();
		return list;
	}
	
	//memberId로 order 조회
	
	

}
