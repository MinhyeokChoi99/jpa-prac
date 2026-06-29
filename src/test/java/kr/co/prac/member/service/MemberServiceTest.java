package kr.co.prac.member.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import kr.co.prac.member.dto.MemberCreateRequest;
import kr.co.prac.member.dto.MemberResponse;
import kr.co.prac.member.entity.Member;
import kr.co.prac.member.exception.AlreadyExistMemberException;
import kr.co.prac.member.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
	@Mock
	MemberRepository memberRepository;
	
	@Mock
	PasswordEncoder passwordEncoder;
	
	@InjectMocks
	MemberServiceImpl memberService;
	
	@Test
	@DisplayName("회원가입에 성공한다")
	void signup_success() {
	    // given
	    MemberCreateRequest c = new MemberCreateRequest();
		c.setName("testUser");  
		c.setEmail("test@test.com");
		c.setPassword("rawPassword");

	    given(memberRepository.existsByEmail(c.getEmail()))
	            .willReturn(false);

	    given(passwordEncoder.encode(c.getPassword()))
	            .willReturn("encodedPassword");

	    given(memberRepository.save(any(Member.class)))
	            .willAnswer(invocation -> invocation.getArgument(0));

	    // when
	    MemberResponse response = memberService.apply(c);

	    // then
	    assertThat(response.getEmail()).isEqualTo("test@test.com");
	}
	
	@Test
	// 이메일중복예외
	void dup_email() {
		MemberCreateRequest c = new MemberCreateRequest();
		c.setName("testUser");  
		c.setEmail("test@test.com");
		c.setPassword("rawPassword");
		
		given(memberRepository.existsByEmail(c.getEmail())).willReturn(true);
		Assertions.assertThrows(AlreadyExistMemberException.class, () -> memberService.apply(c));
		
		verify(passwordEncoder,never()).encode(any());
		verify(memberRepository,never()).save(any(Member.class));
	}
	
	@Test
	// 회원가입시 패스워드 엔코더 호출
	void encoder() {
		// given
	    MemberCreateRequest c = new MemberCreateRequest();
		c.setName("testUser");  
		c.setEmail("test@test.com");
		c.setPassword("rawPassword");

	    given(memberRepository.existsByEmail(c.getEmail()))
	            .willReturn(false);

	    given(passwordEncoder.encode(c.getPassword()))
	            .willReturn("encodedPassword");

	    given(memberRepository.save(any(Member.class)))
	            .willAnswer(invocation -> invocation.getArgument(0));

	    // when
	    MemberResponse response = memberService.apply(c);
	    verify(passwordEncoder,times(1)).encode(any());
	}

}
