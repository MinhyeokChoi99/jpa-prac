package kr.co.prac.login.controller;


import jakarta.validation.Valid;
import kr.co.prac.global.session.SessionConst;
import kr.co.prac.login.exception.LoginRequiredException;
import kr.co.prac.member.dto.MemberResponse;
import kr.co.prac.member.service.MemberService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.prac.login.dto.LoginRequest;
import kr.co.prac.login.dto.LoginResponse;
import kr.co.prac.login.service.LoginService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LoginController {
	
	private final LoginService loginService;
	private final MemberService memberService;

	
	@PostMapping("/members/login")
	public LoginResponse logIn(@RequestBody @Valid LoginRequest request, HttpServletRequest httpServletRequest) {
		LoginResponse login = loginService.login(request);
		HttpSession session = httpServletRequest.getSession();
		session.setAttribute(SessionConst.LOGIN_MEMBER_ID, login.getMemberId());
		return login;
	}
	
	@PostMapping("/members/logout")
	public void logout(HttpServletRequest httpServletRequest) {
		loginService.logout(httpServletRequest);
	}

	@GetMapping("/members/me")
	public MemberResponse me(HttpServletRequest httpServletRequest) {
		HttpSession session = httpServletRequest.getSession(false);
		if(session == null) {
			throw new LoginRequiredException();
		}

		Long memberId = (Long) session.getAttribute(SessionConst.LOGIN_MEMBER_ID);

		if(memberId == null) {
			throw new LoginRequiredException();
		}
		return memberService.find(memberId);
	}

}
