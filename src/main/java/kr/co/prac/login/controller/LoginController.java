package kr.co.prac.login.controller;


import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.prac.login.dto.LoginRequest;
import kr.co.prac.login.dto.LoginResponse;
import kr.co.prac.login.service.LoginService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LoginController {
	
	private final LoginService loginService;
	
	@PostMapping("/members/login")
	public LoginResponse logIn(@RequestBody @Valid LoginRequest loginRequest, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		
		return loginService.login(loginRequest,httpServletRequest,httpServletResponse);
		
	}
	
	@PostMapping("/members/logout")
	public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		
		loginService.logout(httpServletRequest, httpServletResponse);
		
	}



}
