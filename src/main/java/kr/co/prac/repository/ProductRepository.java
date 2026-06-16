package kr.co.prac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.prac.entity.Product;


public interface ProductRepository extends JpaRepository<Product, Long>{

}
