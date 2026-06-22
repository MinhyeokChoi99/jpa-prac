package kr.co.prac.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberUpdateRequest {
//	private Long number; // 정보 수정시 id변경은 불가능해야함 param으로 id 받기
	
	private String name;
	
	private String email;
}
