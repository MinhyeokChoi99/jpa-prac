package kr.co.prac.orders.service;

import java.util.ArrayList;
import java.util.List;

import kr.co.prac.cartitem.entity.CartItem;
import kr.co.prac.cartitem.exception.EmptyCartItemException;
import kr.co.prac.cartitem.repository.CartItemRepository;
import kr.co.prac.product.entity.ProductStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.prac.member.entity.Member;
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
	private final CartItemRepository cartItemRepository;


	@Override // 주문생성 -> 여러건 처리할수있게 수정
	public OrderDetailResponse createOrder(Long memberId, List<OrderCreateRequest> orderCreateRequests) {
		// 빈리스트로 요청이 들어왔을 경우 검증
		if(orderCreateRequests == null || orderCreateRequests.isEmpty()) {
			throw new EmptyItemOrderException();
		}

		Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
		Orders order = Orders.create(member);
		Orders savedOrder = ordersRepository.save(order);

		List<OrderItemResponse> list = new ArrayList<>();

		for(OrderCreateRequest orderCreateRequest : orderCreateRequests) {
			Product product = productRepository.findById(orderCreateRequest.getProductNumber()).orElseThrow(ProductNotFoundException::new);

			if (product.getProductStatus() != ProductStatus.ACTIVE) {
				throw new ProductNotFoundException();
			}
			
			product.removeStock(orderCreateRequest.getCount());

			OrderItem orderItem = OrderItem.create(savedOrder, product, orderCreateRequest.getCount());
			orderItemRepository.save(orderItem);
			list.add(new OrderItemResponse(orderItem));
		}

		return new OrderDetailResponse(savedOrder,list);
	}

	@Override // 장바구니 아이템 주문
	public OrderDetailResponse orderFromCart(Long memberId) {
		Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);

		List<CartItem> cartItemByMemberId = cartItemRepository.findByMemberNumber(memberId);
		if(cartItemByMemberId.isEmpty()) {
			throw new EmptyCartItemException();
		}

		Orders order = Orders.create(member);
		Orders savedOrder = ordersRepository.save(order);

		List<OrderItemResponse> list = new ArrayList<>();

		for (CartItem cartItem : cartItemByMemberId) {
			int count = cartItem.getCount();

			Product product = productRepository.findById(cartItem.getProduct().getNumber()).orElseThrow(ProductNotFoundException::new);
			if(product.getProductStatus() != ProductStatus.ACTIVE) {
				throw new ProductNotFoundException();
			}
			product.removeStock(count);

			OrderItem orderItem = OrderItem.create(savedOrder, product, count);
			orderItemRepository.save(orderItem);
			list.add(new OrderItemResponse(orderItem));
		}

		cartItemRepository.deleteAll(cartItemByMemberId);

		return new OrderDetailResponse(savedOrder,list);

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
		orders.cancel();
	}

	//memberId로 order 조회
	@Override
	@Transactional(readOnly = true)
	public List<OrderResponse> memberIdFound(Long memberId) {
		List<Orders> memberOrders = ordersRepository.findByMemberNumber(memberId);
		List<OrderResponse> list = memberOrders.stream().map(OrderResponse::new).toList();
		return list;
	}

	// 관리자 전용 단건 조회
	@Override
	@Transactional(readOnly = true)
	public OrderDetailResponse findOneForAdmin(Long orderId) {
		Orders order = ordersRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

		List<OrderItemResponse> orderItems = orderItemRepository.
				findByOrdersNumber(orderId)
				.stream()
				.map(OrderItemResponse::new)
				.toList();

		return new OrderDetailResponse(order, orderItems);

	}
}
