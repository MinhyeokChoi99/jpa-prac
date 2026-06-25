package kr.co.prac.global.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import kr.co.prac.member.entity.Member;
import kr.co.prac.member.exception.MemberNotFoundException;
import kr.co.prac.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{

	private final MemberRepository memberRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Member member = memberRepository.findByEmail(username).orElseThrow(MemberNotFoundException::new);
		return new CustomUserDetails(member);
		
	}

}
