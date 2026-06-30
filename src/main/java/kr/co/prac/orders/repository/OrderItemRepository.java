package kr.co.prac.orders.repository;

import java.util.List;

import kr.co.prac.orders.dto.OrderItemResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.prac.orders.entity.OrderItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
	 List<OrderItem> findByOrdersNumber(Long ordersNumber);

	@Query("""
        select new kr.co.prac.orders.dto.OrderItemResponse(
            p.name,
            oi.unitPrice,
            oi.count,
            pi.imageUrl
        )
        from OrderItem oi
        join oi.product p
        left join ProductImage pi
            on pi.product = p
           and pi.thumbnail = true
        where oi.orders.number = :orderId
        """)
	List<OrderItemResponse> findOrderItemResponsesByOrderId(@Param("orderId") Long orderId);
}
