package kr.co.prac.login.dto;

import kr.co.prac.member.entity.Member;
import kr.co.prac.member.entity.Role;
import lombok.Getter;

@Getter
public class LoginResponse {
	
	private Long memberId;
	private String name;
	private String email;
	private Role role;
	
	
	public LoginResponse(Member member) {
		this.memberId = member.getNumber();
		this.name = member.getName();
		this.email = member.getEmail();
		this.role = member.getRole();
	}
}
