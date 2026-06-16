package kr.co.prac.dto.member;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberCreateRequest {
	private String name;
	private String email;
}
