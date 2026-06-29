package kr.co.prac.orders.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;

import kr.co.prac.cartitem.repository.CartItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import kr.co.prac.member.entity.Member;
import kr.co.prac.member.repository.MemberRepository;
import kr.co.prac.orders.dto.OrderCreateRequest;
import kr.co.prac.orders.dto.OrderDetailResponse;
import kr.co.prac.orders.entity.OrderItem;
import kr.co.prac.orders.entity.OrderStatus;
import kr.co.prac.orders.entity.Orders;
import kr.co.prac.orders.exception.EmptyItemOrderException;
import kr.co.prac.orders.repository.OrderItemRepository;
import kr.co.prac.orders.repository.OrdersRepository;
import kr.co.prac.product.entity.Product;
import kr.co.prac.product.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

	@Mock
	OrdersRepository ordersRepository;

	@Mock
	OrderItemRepository orderItemRepository;

	@Mock
	ProductRepository productRepository;

	@Mock
	MemberRepository memberRepository;

	@Mock
	CartItemRepository cartItemRepository;

	@InjectMocks
	OrderServiceImpl orderService;

    /*
    1. 주문 생성 성공
    2. 빈 주문 요청이면 EmptyItemOrderException
    3. 회원이 없으면 MemberNotFoundException
    4. 상품이 없으면 ProductNotFoundException
    5. 주문 취소 성공
    */

	@Test
	public void create_order() {
		Long memberId = 1L;

		Member member = Member.create("testUser", "test@example.com", "encodedPassword");
		ReflectionTestUtils.setField(member, "number", memberId);

		Long productNumber = 1L;

		Product product = Product.create(
				"Keyboard",
				30000,
				10,
				"기계식 키보드"
		);

		List<OrderCreateRequest> list = List.of(
				createOrderCreateRequest(productNumber, 1)
		);

		given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
		given(ordersRepository.save(any(Orders.class)))
				.willAnswer(invocation -> invocation.getArgument(0));
		given(productRepository.findById(productNumber)).willReturn(Optional.of(product));
		given(orderItemRepository.save(any(OrderItem.class)))
				.willAnswer(invocation -> invocation.getArgument(0));

		OrderDetailResponse orderDetailResponse = orderService.createOrder(memberId, list);

		assertThat(orderDetailResponse.getMemberId()).isEqualTo(1L);
		assertThat(orderDetailResponse.getTotalPrice()).isEqualTo(30000);
		assertThat(orderDetailResponse.getStatus()).isEqualTo(OrderStatus.READY);
		assertThat(product.getStock()).isEqualTo(9);
	}

	@Test
	public void empty_request() {
		assertThrows(
				EmptyItemOrderException.class,
				() -> orderService.createOrder(1L, null)
		);
	}

	private OrderCreateRequest createOrderCreateRequest(Long productNumber, Integer count) {
		OrderCreateRequest request = new OrderCreateRequest();

		ReflectionTestUtils.setField(request, "productNumber", productNumber);
		ReflectionTestUtils.setField(request, "count", count);

		return request;
	}
}