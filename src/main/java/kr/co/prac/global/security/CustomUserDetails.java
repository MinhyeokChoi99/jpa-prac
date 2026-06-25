package kr.co.prac.global.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import kr.co.prac.member.entity.Member;
import kr.co.prac.member.entity.Role;

public class CustomUserDetails implements UserDetails{

	private final Member member;
	
	public CustomUserDetails(Member member) {
		this.member = member;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + member.getRole().name()));
	}

	@Override 
	public String getPassword() {
		return member.getPassword();
	}

	@Override 
	public String getUsername() {
		return member.getEmail();
	}
	
	public Long getMemberId() {
		return member.getNumber();
	}
	
	public String getName() {
		return member.getName();
	}
	
	public Role getRole() {
		return member.getRole();
	}
	

}
