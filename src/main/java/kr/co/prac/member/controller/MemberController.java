package kr.co.prac.member.controller;

import java.util.List;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kr.co.prac.global.security.SecurityUtil;
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
	public MemberResponse updateMe(@RequestBody MemberUpdateRequest memberUpdateRequest) {
		Long memberId = SecurityUtil.getMemberId();
		return memberService.update(memberId, memberUpdateRequest);
	}

	@GetMapping("/me")
	public MemberResponse me() {
		Long memberId = SecurityUtil.getMemberId();
		return memberService.find(memberId);
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
