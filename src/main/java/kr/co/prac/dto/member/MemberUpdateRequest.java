package kr.co.prac.dto.member;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberUpdateRequest {
	private Long number;
	
	private String name;
	
	private String email;
}
