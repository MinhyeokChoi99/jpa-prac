package kr.co.prac.orders.entity;

import kr.co.prac.member.entity.Member;
import kr.co.prac.orders.exception.AlreadyCancelledOrderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrdersTest {

    @Test
    @DisplayName("주문 생성 시 상태는 READY가 된다")
    void createOrder_statusReady() {
        // given
        Member member = Member.create( 
        		"testUser",
                "test@test.com",
                "encodedPassword");

        // when
        Orders orders = Orders.create(member);

        // then
        assertThat(orders.getMember()).isEqualTo(member);
        assertThat(orders.getStatus()).isEqualTo(OrderStatus.READY);
        assertThat(orders.getOrderDate()).isNotNull();
    }

    @Test
    @DisplayName("주문을 취소하면 상태가 CANCEL이 된다")
    void cancelOrder() {
    	// given
        Member member = Member.create( 
        		"testUser",
                "test@test.com",
                "encodedPassword");
        Orders orders = Orders.create(member);

        // when
        orders.cancel();

        // then
        assertThat(orders.getStatus()).isEqualTo(OrderStatus.CANCEL);
    }

    @Test
    @DisplayName("이미 취소된 주문을 다시 취소하면 예외가 발생한다")
    void cancelAlreadyCancelledOrder_fail() {
    	// given
        Member member = Member.create( 
        		"testUser",
                "test@test.com",
                "encodedPassword");
        Orders orders = Orders.create(member);
        orders.cancel();

        // when & then
        assertThrows(AlreadyCancelledOrderException.class, orders::cancel);
    }
}