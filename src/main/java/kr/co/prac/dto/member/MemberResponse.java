package kr.co.prac.dto.member;

import kr.co.prac.entity.Member;
import lombok.Getter;

@Getter

public class MemberResponse {
	
	private Long number;
	
	private String name;
	
	private String email;
	
	public MemberResponse(Member member) {
		this.number = member.getNumber();
		this.name = member.getName();
		this.email = member.getEmail();
		
	}
	
	
	
}
