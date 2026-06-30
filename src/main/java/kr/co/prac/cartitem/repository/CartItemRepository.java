package kr.co.prac.cartitem.repository;

import java.util.List;
import java.util.Optional;

import kr.co.prac.cartitem.dto.CartItemResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.prac.cartitem.entity.CartItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartItemRepository extends JpaRepository<CartItem, Long>{
	Optional<CartItem> findByMemberNumberAndProductNumber(Long memberId, Long productId);
	List<CartItem> findByMemberNumber(Long memberId);
	
	Optional<CartItem> findByNumberAndMemberNumber(Long cartItemId, Long memberId);

	@Query("""
            select new kr.co.prac.cartitem.dto.CartItemResponse(
                p.number,
                ci.count,
                pi.imageUrl
            )
            from CartItem ci
            join ci.product p
            left join ProductImage pi
                on pi.product = p
               and pi.imageUrl = true
            where ci.member.number = :memberId
            """)
	List<CartItemResponse> findCartItemResponsesByMemberId(@Param("memberId") Long memberId);
}
