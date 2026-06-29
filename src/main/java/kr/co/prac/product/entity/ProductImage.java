package kr.co.prac.product.entity;

import jakarta.persistence.*;
import kr.co.prac.global.entity.BaseTimeEntity;
import lombok.Getter;

@Entity
@Getter
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_product_image_product_sort_order",
                        columnNames = {"product_id", "sort_order"}
                )
        }
)
public class ProductImage extends BaseTimeEntity {

    protected ProductImage() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "image_url", nullable = false, length = 1000)
    private String imageUrl;

    @Column(nullable = false)
    private boolean thumbnail;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    public static ProductImage create(Product product, String imageUrl, boolean thumbnail, int sortOrder) {
        ProductImage productImage = new ProductImage();
        productImage.product = product;
        productImage.imageUrl = imageUrl;
        productImage.thumbnail = thumbnail;
        productImage.sortOrder = sortOrder;
        return productImage;
    }

    public void markAsThumbnail() {
        this.thumbnail = true;
    }

    public void unmarkAsThumbnail() {
        this.thumbnail = false;
    }

    public void update(String imageUrl, int sortOrder) {
        this.imageUrl = imageUrl;
        this.sortOrder = sortOrder;
    }
}
