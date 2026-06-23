package kr.co.prac.login.service;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.prac.login.dto.LoginRequest;
import kr.co.prac.login.dto.LoginResponse;

public interface LoginService {
	
	LoginResponse login(LoginRequest loginRequest);
	
	void logout(HttpServletRequest httpServletRequest);
	
	

}
