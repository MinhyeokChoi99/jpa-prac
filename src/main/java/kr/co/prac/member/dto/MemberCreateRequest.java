package kr.co.prac.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberCreateRequest {
	private String name;
	private String email;
}
