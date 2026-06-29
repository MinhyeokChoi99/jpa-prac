package kr.co.prac.member.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kr.co.prac.global.entity.BaseTimeEntity;
import kr.co.prac.member.dto.MemberCreateRequest;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter 

public class Member extends BaseTimeEntity {
	
	protected Member() {}

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long number;
	
	private String name;
	
	private String email;
	
	private String password;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
    public static Member create(String name, String email, String encodedPassword) {
        Member member = new Member();
        member.name = name;
        member.email = email;
        member.password = encodedPassword;
        member.role = Role.USER;
        return member;
    }

    public static Member createAdmin(String name, String email, String encodedPassword) {
        Member member = new Member();
        member.name = name;
        member.email = email;
        member.password = encodedPassword;
        member.role = Role.ADMIN;
        return member;
    }

    public void update(String name, String email) {
        this.name = name;
        this.email = email;
    }
	
}
