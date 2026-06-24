package kr.co.prac.orders.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import kr.co.prac.member.exception.MemberNotFoundException;
import kr.co.prac.member.repository.MemberRepository;
import kr.co.prac.orders.dto.OrderCreateRequest;
import kr.co.prac.orders.dto.OrderDetailResponse;
import kr.co.prac.orders.dto.OrderItemResponse;
import kr.co.prac.orders.dto.OrderResponse;
import kr.co.prac.orders.entity.OrderItem;
import kr.co.prac.orders.entity.OrderStatus;
import kr.co.prac.orders.entity.Orders;
import kr.co.prac.orders.exception.AlreadyCancelledOrderException;
import kr.co.prac.orders.exception.EmptyItemOrderException;
import kr.co.prac.orders.exception.NotAuthorizedCancelException;
import kr.co.prac.orders.exception.NotAuthorizedMemberException;
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
	public OrderResponse createOrder(Long memberId, List<OrderCreateRequest> orderCreateRequests) {
		// 빈리스트로 요청이 들어왔을 경우 검증
		if(orderCreateRequests == null || orderCreateRequests.isEmpty()) {
			throw new EmptyItemOrderException();
		}
		Orders order = new Orders();
		order.setMember(memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new));
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
			orderItem.setUnitPrice(product.getPrice());
			orderItemRepository.save(orderItem);
		}
		
		return new OrderResponse(savedOrder);
	}


	@Override// 단건 조회
	@Transactional(readOnly = true)
	public OrderDetailResponse findOne(Long orderId, Long loginMemberId) {
		Orders order = ordersRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
		if(!order.getMember().getNumber().equals(loginMemberId)) {
			throw new NotAuthorizedMemberException();
		}
		
		List<OrderItemResponse> orderItems = orderItemRepository.
				findByOrdersNumber(orderId)
				.stream()
				.map(OrderItemResponse::new)
				.toList();
		
		return new OrderDetailResponse(order, orderItems);
		
	}


	@Override// 전체조회
	@Transactional(readOnly = true)
	public List<OrderResponse> findAll() {
		List<OrderResponse> orderResponses = new ArrayList<>();
		for(Orders order : ordersRepository.findAll()) {
			orderResponses.add(new OrderResponse(order));
		}
		return orderResponses;
	}


	@Override// 삭제
	public void cancelOrder(Long ordersId, Long loginMemberId) {
		Orders orders = ordersRepository.findById(ordersId).orElseThrow(OrderNotFoundException::new);
		if(orders.getStatus() == OrderStatus.CANCEL) {
			throw new AlreadyCancelledOrderException();
		}
		
		if(!orders.getMember().getNumber().equals(loginMemberId)) {
			throw new NotAuthorizedCancelException();
		}
		
		List<OrderItem> orderItems = orderItemRepository.findByOrdersNumber(ordersId);
		for(OrderItem orderItem : orderItems) {		
			Product product = orderItem.getProduct();
			product.addStock(orderItem.getCount());
		}
		orders.setStatus(OrderStatus.CANCEL);
	}


	@Override
	public List<OrderResponse> memberIdFound(Long memberId) {
		List<Orders> memberOrders = ordersRepository.findByMemberNumber(memberId);
		List<OrderResponse> list = memberOrders.stream().map(OrderResponse::new).toList();
		return list;
	}
	
	//memberId로 order 조회
	
	

}
