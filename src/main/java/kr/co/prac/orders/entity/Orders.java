package kr.co.prac.orders.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kr.co.prac.global.entity.BaseTimeEntity;
import kr.co.prac.member.entity.Member;
import kr.co.prac.orders.exception.AlreadyCancelledOrderException;
import lombok.Getter;


@Entity
@Getter
public class Orders extends BaseTimeEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long number;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;
	
	private LocalDateTime orderDate;
	
	@Enumerated(EnumType.STRING)
	private OrderStatus status;
	
    public static Orders create(Member member) {
        Orders orders = new Orders();
        orders.member = member;
        orders.orderDate = LocalDateTime.now();
        orders.status = OrderStatus.READY;
        return orders;
    }

    public void cancel() {
        if (this.status == OrderStatus.CANCEL) {
            throw new AlreadyCancelledOrderException();
        }

        this.status = OrderStatus.CANCEL;
    }
}
