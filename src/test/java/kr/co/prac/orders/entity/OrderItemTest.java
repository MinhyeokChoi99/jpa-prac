package kr.co.prac.orders.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kr.co.prac.member.entity.Member;
import kr.co.prac.product.entity.Product;

public class OrderItemTest {
	
	@Test
	@DisplayName("OrderItem 생성 시 order, product, unitPrice, count가 저장된다")
	void createOrderItem() {
	    // given
	    Member member = Member.create("user", "user@test.com", "encodedPassword");
	    Orders orders = Orders.create(member);

	    Product product = Product.create(
	            "Keyboard",
	            30000,
	            10,
	            "기계식 키보드"
	    );

	    // when
	    OrderItem orderItem = OrderItem.create(orders, product, 2);

	    // then
	    assertThat(orderItem.getOrders()).isEqualTo(orders);
	    assertThat(orderItem.getProduct()).isEqualTo(product);
	    assertThat(orderItem.getProduct().getName()).isEqualTo("Keyboard");
	    assertThat(orderItem.getUnitPrice()).isEqualTo(30000);
	    assertThat(orderItem.getCount()).isEqualTo(2);
	}

	@Test
	@DisplayName("OrderItem의 totalPrice는 unitPrice * count로 계산된다")
	void calculateTotalPrice() {
	    // given
	    Member member = Member.create("user", "user@test.com", "encodedPassword");
	    Orders orders = Orders.create(member);

	    Product product = Product.create(
	            "Keyboard",
	            30000,
	            10,
	            "기계식 키보드"
	    );

	    OrderItem orderItem = OrderItem.create(orders, product, 2);

	    // when
	    int totalPrice = orderItem.getTotalPrice();

	    // then
	    assertThat(totalPrice).isEqualTo(60000);
	}

}
