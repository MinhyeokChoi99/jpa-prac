package kr.co.prac.product.repository;

import kr.co.prac.product.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductImageRepository extends JpaRepository<ProductImage,Long> {

    List<ProductImage> findByProduct_NumberOrderBySortOrderAsc(Long productNumber);

    List<ProductImage> findByProduct_NumberAndThumbnailTrue(Long productNumber);

    Optional<ProductImage> findByNumberAndProduct_Number(Long imageId, Long productNumber);

    // 지금까지 등록한 이미지 중에 sortOrder가 가장 높은 이미지 반환
    Optional<ProductImage> findTopByProduct_NumberOrderBySortOrderDesc(Long productNumber);
    
    Optional<ProductImage> findTopByProduct_NumberOrderBySortOrderAsc(Long productNumber);
}
