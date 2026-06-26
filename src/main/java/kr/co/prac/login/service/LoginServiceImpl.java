package kr.co.prac.login.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.co.prac.global.security.CustomUserDetails;
import kr.co.prac.login.dto.LoginRequest;
import kr.co.prac.login.dto.LoginResponse;
import kr.co.prac.login.exception.InvalidPasswordException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService{

	private final SecurityContextRepository securityContextRepository;
	private final AuthenticationManager authenticationManager;
	
	@Override
	public LoginResponse login(LoginRequest loginRequest, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		try {
			Authentication authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.getEmail(),loginRequest.getPassword()));
			SecurityContext context = SecurityContextHolder.createEmptyContext();
			context.setAuthentication(authentication);	
			SecurityContextHolder.setContext(context);
			securityContextRepository.saveContext(context, httpServletRequest, httpServletResponse);	
			CustomUserDetails principal = (CustomUserDetails)authentication.getPrincipal();
			return new LoginResponse(principal);

		
		} catch (AuthenticationException e) {
			SecurityContextHolder.clearContext();
			throw new InvalidPasswordException();
		}
	}
	@Override
	public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		SecurityContextHolder.clearContext();
		HttpSession session = httpServletRequest.getSession(false);
		if(session != null) {			
			session.invalidate();
		}
		
		Cookie cookie = new Cookie("JSESSIONID", "");
		cookie.setPath("/");
		cookie.setMaxAge(0);
		httpServletResponse.addCookie(cookie);
	}
	
	
}
