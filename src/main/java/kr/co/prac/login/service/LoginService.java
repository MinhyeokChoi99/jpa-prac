package kr.co.prac.login.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.prac.login.dto.LoginRequest;
import kr.co.prac.login.dto.LoginResponse;

public interface LoginService {
	
	LoginResponse login(LoginRequest loginRequest, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);
	
	void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);
	
	

}
