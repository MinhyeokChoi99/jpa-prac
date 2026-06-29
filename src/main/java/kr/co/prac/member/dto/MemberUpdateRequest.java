package kr.co.prac.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberUpdateRequest {
//	private Long number; // 정보 수정시 id변경은 불가능해야함 param으로 id 받기
	
	@NotBlank
	private String name;
	
	@NotBlank
	private String email;
}
