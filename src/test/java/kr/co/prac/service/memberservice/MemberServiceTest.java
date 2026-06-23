package kr.co.prac.service.memberservice;

import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.co.prac.member.dto.MemberCreateRequest;
import kr.co.prac.member.dto.MemberResponse;
import kr.co.prac.member.entity.Member;
import kr.co.prac.member.exception.AlreadyExistMemberException;
import kr.co.prac.member.repository.MemberRepository;
import kr.co.prac.member.service.MemberServiceImpl;


@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
	@Mock
	MemberRepository memberRepository;
	
	@InjectMocks
	MemberServiceImpl memberServiceImpl;
	
	@Test
	@DisplayName("등록:정상")
	void apply1() {
		MemberCreateRequest memberCreateRequest = new MemberCreateRequest();
		memberCreateRequest.setName("memberA");
		memberCreateRequest.setEmail("abc@gmail.com");
		
		Member savedMember = new Member();
		savedMember.setName(memberCreateRequest.getName());
		savedMember.setEmail(memberCreateRequest.getEmail());
		
		Mockito.when(memberRepository.existsByEmail(memberCreateRequest.getEmail())).thenReturn(false);
		Mockito.when(memberRepository.save(any(Member.class))).thenReturn(savedMember);
		
		
		MemberResponse appliedMemberResponse = memberServiceImpl.apply(memberCreateRequest);
		assertThat(appliedMemberResponse.getName()).isEqualTo(memberCreateRequest.getName());
		assertThat(appliedMemberResponse.getEmail()).isEqualTo(memberCreateRequest.getEmail());
		Mockito.verify(memberRepository).save(any(Member.class));
	}
	
	@Test
	@DisplayName("등록:이메일중복")
	void apply2() {
		MemberCreateRequest memberCreateRequest = new MemberCreateRequest();
		memberCreateRequest.setName("memberA");
		memberCreateRequest.setEmail("abc@gmail.com");
		
		Mockito.when(memberRepository.existsByEmail(memberCreateRequest.getEmail())).thenReturn(true);
		assertThrows(AlreadyExistMemberException.class, () -> memberServiceImpl.apply(memberCreateRequest));
	} // 다시보기
	
	@Test
	@DisplayName("등록:필드가 null")
	void apply3() {
		
	}
	
}
