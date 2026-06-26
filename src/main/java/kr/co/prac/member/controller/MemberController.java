package kr.co.prac.member.controller;

import java.util.List;
import jakarta.validation.Valid;
import kr.co.prac.global.security.CustomUserDetails;
import kr.co.prac.global.security.SecurityUtil;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import kr.co.prac.member.dto.MemberCreateRequest;
import kr.co.prac.member.dto.MemberResponse;
import kr.co.prac.member.dto.MemberUpdateRequest;
import kr.co.prac.member.service.MemberService;
import kr.co.prac.orders.dto.OrderResponse;
import kr.co.prac.orders.service.OrderService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {


	private final MemberService memberService;
	private final OrderService orderService;
	
	@PostMapping
	public MemberResponse join(@RequestBody @Valid MemberCreateRequest memberCreateRequest) {
        return memberService.apply(memberCreateRequest);
	}
	
	
	@PutMapping("/me")
	public MemberResponse updateMe(@RequestBody MemberUpdateRequest memberUpdateRequest, @AuthenticationPrincipal CustomUserDetails userDetails) {
		return memberService.update(userDetails.getMemberId(), memberUpdateRequest);
	}

	@GetMapping("/me")
	public MemberResponse me(@AuthenticationPrincipal CustomUserDetails userDetails) {
		return memberService.find(userDetails.getMemberId());
	}

	@GetMapping("/me/orders")
	public List<OrderResponse> memberOrderList() {
		Long memberId = SecurityUtil.getMemberId();
		return orderService.memberIdFound(memberId);
	}

	@DeleteMapping("/me")
	public void deleteMember() {
		Long memberId = SecurityUtil.getMemberId();
		memberService.delete(memberId);
	}
	
	
}
