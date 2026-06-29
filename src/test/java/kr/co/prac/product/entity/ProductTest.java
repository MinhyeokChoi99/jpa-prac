package kr.co.prac.product.entity;

import kr.co.prac.product.exception.NotEnoughStockException;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ProductTest {

    @Test
    @DisplayName("상품을 생성하면 ACTIVE 상태로 생성된다")
    void createProduct() {
        // given & when
        Product product = Product.create(
                "Keyboard",
                30000,
                10,
                "기계식 키보드"
        );

        // then
        assertThat(product.getName()).isEqualTo("Keyboard");
        assertThat(product.getPrice()).isEqualTo(30000);
        assertThat(product.getStock()).isEqualTo(10);
        assertThat(product.getDescription()).isEqualTo("기계식 키보드");
        assertThat(product.getProductStatus()).isEqualTo(ProductStatus.ACTIVE);
    }

    @Test
    @DisplayName("재고를 증가시킬 수 있다")
    void addStock() {
        // given
        Product product = Product.create(
                "Keyboard",
                30000,
                10,
                "기계식 키보드"
        );

        // when
        product.addStock(5);

        // then
        assertThat(product.getStock()).isEqualTo(15);
    }

    @Test
    @DisplayName("현재 재고 이하의 수량은 재고 감소에 성공한다")
    void removeStock_success() {
        // given
        Product product = Product.create(
                "Keyboard",
                30000,
                10,
                "기계식 키보드"
        );

        // when
        product.removeStock(7);

        // then
        assertThat(product.getStock()).isEqualTo(3);
    }

    @Test
    @DisplayName("현재 재고보다 많은 수량을 감소시키면 예외가 발생한다")
    void removeStock_notEnoughStock_fail() {
        // given
        Product product = Product.create(
                "Keyboard",
                30000,
                10,
                "기계식 키보드"
        );

        // when & then
        assertThrows(NotEnoughStockException.class, () -> product.removeStock(11));
   
    }

    @Test
    @DisplayName("상품을 제거하면 DELETED 상태가 된다")
    void deleteProduct() {
        // given
        Product product = Product.create(
                "Keyboard",
                30000,
                10,
                "기계식 키보드"
        );

        // when
        product.delete();

        // then
        assertThat(product.getProductStatus()).isEqualTo(ProductStatus.DELETED);
    }
}