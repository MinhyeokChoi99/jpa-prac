package kr.co.prac.member.dto;

import kr.co.prac.member.entity.Member;
import kr.co.prac.member.entity.Role;
import lombok.Getter;

@Getter

public class MemberResponse {
	
	private Long number;
	
	private String name;
	
	private String email;

	private Role role;
	
	public MemberResponse(Member member) {
		this.number = member.getNumber();
		this.name = member.getName();
		this.email = member.getEmail();
		this.role = member.getRole();
		
	}
	
	
	
}
