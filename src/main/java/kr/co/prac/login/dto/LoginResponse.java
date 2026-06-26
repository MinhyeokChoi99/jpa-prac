package kr.co.prac.login.dto;

import kr.co.prac.global.security.CustomUserDetails;
import kr.co.prac.member.entity.Member;
import kr.co.prac.member.entity.Role;
import lombok.Getter;

@Getter
public class LoginResponse {

	private String name;
	
	private String email;

	private Role role;

	public LoginResponse(Member member) {
		
		this.name = member.getName();
		this.email = member.getEmail();
		this.role = member.getRole();
	}

	public LoginResponse(CustomUserDetails principal) {

		this.name = principal.getName();
		this.email = principal.getUsername();
		this.role = principal.getRole();
	}
}
