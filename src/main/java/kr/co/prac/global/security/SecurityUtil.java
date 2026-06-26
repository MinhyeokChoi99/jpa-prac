package kr.co.prac.global.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import kr.co.prac.login.exception.LoginRequiredException;

public class SecurityUtil {
	
	private SecurityUtil() {}

	 
	 public static Long getMemberId() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		 if(authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			 throw new LoginRequiredException();
		 }
		 
		 Object principal =  authentication.getPrincipal();
		 
		 if( !(principal instanceof CustomUserDetails customUserDetails)) {
			 throw new LoginRequiredException();
		 }
		 
		 
		 return customUserDetails.getMemberId();

	 }

}
