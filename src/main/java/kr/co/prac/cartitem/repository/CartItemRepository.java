package kr.co.prac.cartitem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.prac.cartitem.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long>{
	Optional<CartItem> findByMemberNumberAndProductNumber(Long memberId, Long productId);
	List<CartItem> findByMemberNumber(Long memberId);
	
	Optional<CartItem> findByMemberNumberAndCartItemNumber(Long memberId, Long CartItemId);
}
