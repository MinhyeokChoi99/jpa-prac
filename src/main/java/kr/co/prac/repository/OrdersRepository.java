package kr.co.prac.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.prac.entity.Orders;


public interface OrdersRepository extends JpaRepository<Orders,Long>{
	List<Orders>findByMemberNumber(Long memberId);
}
