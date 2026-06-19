package kr.co.prac.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.prac.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
	 List<OrderItem> findByOrdersNumber(Long ordersNumber);
}
