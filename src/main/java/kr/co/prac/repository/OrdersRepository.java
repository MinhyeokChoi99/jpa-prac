package kr.co.prac.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.prac.entity.Orders;


public interface OrdersRepository extends JpaRepository<Orders,Long>{

}
