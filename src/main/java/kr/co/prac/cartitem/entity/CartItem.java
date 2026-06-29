package kr.co.prac.cartitem.entity;



import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import kr.co.prac.global.entity.BaseTimeEntity;
import kr.co.prac.member.entity.Member;
import kr.co.prac.product.entity.Product;
import lombok.Getter;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_cart_item_member_product",
                        columnNames = {"member_id", "product_id"}
                )
        }
)
@Getter
public class CartItem extends BaseTimeEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long number;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;
	
	private int count;
	
	public void addCount(int count) {
		this.count += count;
	}
	
	public void addCount() {
	    this.count += 1;
	}
	
	public void subCount(int  count) {
		this.count -= count;
	}
	
	public void subCount() {
		this.count--;
	}
	
	
	public static CartItem create(Member member, Product product, int count) {
		CartItem cartItem = new CartItem();
		cartItem.member = member;
		cartItem.product = product;
		cartItem.count = count;
		return cartItem;
		
	}
}
