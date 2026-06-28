package kr.co.prac.product.repository;

import kr.co.prac.product.entity.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.prac.product.entity.Product;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long>{
    List<Product> findAllByProductStatus(ProductStatus productStatus);
}
