package kr.co.prac.cartitem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import kr.co.prac.cartitem.dto.CartItemRequest;
import kr.co.prac.cartitem.dto.CartItemResponse;
import kr.co.prac.cartitem.entity.CartItem;
import kr.co.prac.cartitem.exception.CartItemNotFoundException;
import kr.co.prac.cartitem.repository.CartItemRepository;
import kr.co.prac.member.entity.Member;
import kr.co.prac.member.exception.MemberNotFoundException;
import kr.co.prac.member.repository.MemberRepository;
import kr.co.prac.product.entity.Product;
import kr.co.prac.product.entity.ProductImage;
import kr.co.prac.product.exception.ProductNotFoundException;
import kr.co.prac.product.repository.ProductImageRepository;
import kr.co.prac.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CartItemServiceImpl implements CartItemService{
	
	private final MemberRepository memberRepository;
	private final CartItemRepository cartItemRepository;
	private final ProductRepository productRepository;
	private final ProductImageRepository productImageRepository;
	

	@Override
	public List<CartItemResponse> viewCart(Long memberId) {
		findMember(memberId);
		List<CartItem> cartItems = cartItemRepository.findByMemberNumber(memberId);
		for (CartItem cartItem : cartItems) {
			Long productNumber = cartItem.getProduct().getNumber();
			List<ProductImage> thumnailImages = productImageRepository.findByProduct_NumberAndThumbnailTrue(productNumber);
			if(thumnailImages.size() >)
			
		}
		
		return cartItems.stream().map(CartItemResponse::new).toList();
	}

	@Override
	public CartItemResponse addItem(Long memberId, CartItemRequest cartItemRequest) {
		Member member = findMember(memberId);
		Product product = productRepository.findById(cartItemRequest.getProductId()).orElseThrow(ProductNotFoundException::new);
		
		Optional<CartItem> cartItemOptional = cartItemRepository.findByMemberNumberAndProductNumber(memberId, cartItemRequest.getProductId());
		if(cartItemOptional.isEmpty()) {
			CartItem cartItem = CartItem.create(member, product, cartItemRequest.getCount());
			CartItem savedCartItem = cartItemRepository.save(cartItem);
			return new CartItemResponse(savedCartItem);
		} else {
			CartItem existCartItem = cartItemOptional.get();
			existCartItem.addCount(cartItemRequest.getCount());
			return new CartItemResponse(existCartItem);
		}
	}

	@Override
	public CartItemResponse addCount(Long memberId, Long cartItemId) {

		CartItem cartItem = findCartItem(cartItemId, memberId);
		cartItem.addCount();
		return new CartItemResponse(cartItem);
		
		
	}

	@Override
	public void subCount(Long memberId, Long cartItemId) {

		CartItem cartItem = findCartItem(cartItemId, memberId);
		if(cartItem.getCount() == 1) {
			cartItemRepository.delete(cartItem);
			return;
		}
		cartItem.subCount();
	}

	@Override
	public void deleteItem(Long memberId, Long cartItemId) {
		CartItem cartItem = findCartItem(cartItemId, memberId);

		cartItemRepository.delete(cartItem);

	}

	private Member findMember(Long memberId) {
		return memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
	}

	private CartItem findCartItem(Long cartItemId, Long memberId) {
		return cartItemRepository.findByNumberAndMemberNumber(cartItemId, memberId).orElseThrow(CartItemNotFoundException::new);
	}

}
