package kr.co.prac.member.controller;

import java.util.List;


import jakarta.servlet.http.HttpServletRequest;
import kr.co.prac.global.session.SessionUtil;
import org.springframework.web.bind.annotation.GetMapping;
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
	public MemberResponse join(@RequestBody MemberCreateRequest memberCreateRequest) {
        return memberService.apply(memberCreateRequest);
	}
	
	@PutMapping("/me")
	public MemberResponse updateMe(@RequestBody MemberUpdateRequest memberUpdateRequest, HttpServletRequest httpServletRequest) {
		Long loginMemberId = SessionUtil.getLoginMemberId(httpServletRequest);
		return memberService.update(loginMemberId, memberUpdateRequest);
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
