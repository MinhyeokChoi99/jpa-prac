package kr.co.prac.member.controller;

import java.util.List;


import jakarta.servlet.http.HttpServletRequest;
import kr.co.prac.global.session.SessionUtil;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public MemberResponse apply(@RequestBody MemberCreateRequest mr) {
		MemberResponse appliedMember = memberService.apply(mr);
		return appliedMember;
	}
	
	@GetMapping("/{number}")
	public MemberResponse find(@PathVariable Long number) {
		MemberResponse mr = memberService.find(number);
		return mr;
	}
	
	@GetMapping
	public List<MemberResponse> findAll() {
		List<MemberResponse> members = memberService.findAll();
		return members;
	}
	
	@PutMapping("/{number}")
	public MemberResponse update(@PathVariable Long number, @RequestBody MemberUpdateRequest memberUpdateRequest) {
		return memberService.update(number, memberUpdateRequest);
	}
	
	@DeleteMapping("/{number}")
	public String delete(@PathVariable Long number) {
		memberService.delete(number);
		return "성공";
	}
	
	// 맴버기준조회
	@GetMapping("/{memberId}/orders")
	public List<OrderResponse> orderByMemberId(@PathVariable Long memberId) {
		List<OrderResponse> memberIdFound = orderService.memberIdFound(memberId);
		return memberIdFound;
	}

	@GetMapping("/me")
	public MemberResponse me(HttpServletRequest httpServletRequest) {
		Long loginMemberId = SessionUtil.getLoginMemberId(httpServletRequest);
		return memberService.find(loginMemberId);
	}

	@GetMapping("/me/orders")
	public List<OrderResponse> memberOrderList(HttpServletRequest httpServletRequest) {
		Long loginMemberId = SessionUtil.getLoginMemberId(httpServletRequest);
		return orderService.memberIdFound(loginMemberId);

	}
	
	
}
