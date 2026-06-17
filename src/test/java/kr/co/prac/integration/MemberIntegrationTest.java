package kr.co.prac.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kr.co.prac.dto.member.MemberCreateRequest;
import kr.co.prac.dto.member.MemberResponse;
import kr.co.prac.dto.member.MemberUpdateRequest;
import kr.co.prac.entity.Member;
import kr.co.prac.repository.MemberRepository;
import kr.co.prac.service.memberservice.MemberServiceImpl;

@SpringBootTest
@Transactional
public class MemberIntegrationTest {
	//등록 조회 수정 삭제
	@Autowired 
	MemberServiceImpl memberServiceImpl;
	@Autowired
	MemberRepository memberRepository;
	
	@Test
	void 등록() {
		MemberCreateRequest memberCreateRequest = new MemberCreateRequest();
		memberCreateRequest.setName("memberA");
		memberCreateRequest.setEmail("abc@gmail.com");
		
		MemberResponse apply = memberServiceImpl.apply(memberCreateRequest);
		
		assertThat(apply.getName()).isEqualTo(memberCreateRequest.getName());
		assertThat(apply.getEmail()).isEqualTo(memberCreateRequest.getEmail());
	}
		
	@Test
	void 중복() {
		MemberCreateRequest memberCreateRequest1 = new MemberCreateRequest();
		memberCreateRequest1.setName("memberA");
		memberCreateRequest1.setEmail("abc@gmail.com");
		MemberResponse apply1 = memberServiceImpl.apply(memberCreateRequest1);
		
		MemberCreateRequest memberCreateRequest2 = new MemberCreateRequest();
		memberCreateRequest2.setName("memberB");
		memberCreateRequest2.setEmail("abc@gmail.com");
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> memberServiceImpl.apply(memberCreateRequest2));
	}	
	
	@Test
	void 단건조회() {
		MemberCreateRequest memberCreateRequest = new MemberCreateRequest();
		memberCreateRequest.setName("memberA");
		memberCreateRequest.setEmail("abc@gmail.com");
		MemberResponse apply = memberServiceImpl.apply(memberCreateRequest);
		
		MemberResponse findResponse = memberServiceImpl.find(apply.getNumber());
		
		assertThat(findResponse.getName()).isEqualTo("memberA");
		
		
	}
	
	@Test
	void 없는회원조회() {
		assertThrows(IllegalArgumentException.class, () -> memberServiceImpl.find(12345L));
	}
	
	@Test
	void 전체조회() {
		MemberCreateRequest memberCreateRequest1 = new MemberCreateRequest();
		memberCreateRequest1.setName("memberA");
		memberCreateRequest1.setEmail("abc@gmail.com");
		MemberResponse apply1 = memberServiceImpl.apply(memberCreateRequest1);
		MemberCreateRequest memberCreateRequest2 = new MemberCreateRequest();
		memberCreateRequest2.setName("memberB");
		memberCreateRequest2.setEmail("cba@gmail.com");
		MemberResponse apply2 = memberServiceImpl.apply(memberCreateRequest2);
		
		long count = memberRepository.count();
		List<Member> allMembers = memberServiceImpl.findAll();
		
		assertThat(count).isEqualTo(allMembers.size());
		
	}
	
	@Test
	void 삭제() {
		MemberCreateRequest memberCreateRequest = new MemberCreateRequest();
		memberCreateRequest.setName("memberA");
		memberCreateRequest.setEmail("abc@gmail.com");
		MemberResponse apply = memberServiceImpl.apply(memberCreateRequest);
		
		memberServiceImpl.delete(apply.getNumber());
		
		assertThrows(IllegalArgumentException.class, () -> memberServiceImpl.find(apply.getNumber()));
		
	}
	
	@Test
	void 없는회원삭제() {
		assertThrows(IllegalArgumentException.class, () -> memberServiceImpl.delete(1L));
	}
	
	@Test
	void 회원정보수정() {
		MemberCreateRequest memberCreateRequest = new MemberCreateRequest();
		memberCreateRequest.setName("memberA");
		memberCreateRequest.setEmail("abc@gmail.com");
		MemberResponse apply = memberServiceImpl.apply(memberCreateRequest);
		MemberUpdateRequest memberUpdateRequest = new MemberUpdateRequest();
		
		memberUpdateRequest.setName("memberB");
		memberUpdateRequest.setEmail("abc@gmail.com");
		MemberResponse update = memberServiceImpl.update(apply.getNumber(), memberUpdateRequest);
		
		assertThat(update.getName()).isEqualTo(memberUpdateRequest.getName());
	}
	
	@Test
	void 회원정보수정_NULL() {
		MemberCreateRequest memberCreateRequest = new MemberCreateRequest();
		memberCreateRequest.setName("memberA");
		memberCreateRequest.setEmail("abc@gmail.com");
		MemberResponse apply = memberServiceImpl.apply(memberCreateRequest);
		MemberUpdateRequest memberUpdateRequest = new MemberUpdateRequest();
		
		memberUpdateRequest.setName(null);
		memberUpdateRequest.setEmail(null);
		MemberResponse update = memberServiceImpl.update(apply.getNumber(), memberUpdateRequest);
		
		assertThat(update.getName()).isEqualTo(apply.getName());
		assertThat(update.getEmail()).isEqualTo(apply.getEmail());
	}
	
	@Test
	void 회원정보수정_빈문자열() {
		MemberCreateRequest memberCreateRequest = new MemberCreateRequest();
		memberCreateRequest.setName("memberA");
		memberCreateRequest.setEmail("abc@gmail.com");
		MemberResponse apply = memberServiceImpl.apply(memberCreateRequest);
		
		MemberUpdateRequest memberUpdateRequest = new MemberUpdateRequest();
		memberUpdateRequest.setName("");
		memberUpdateRequest.setEmail("");
		MemberResponse update = memberServiceImpl.update(apply.getNumber(), memberUpdateRequest);
		
		assertThat(update.getName()).isEqualTo(apply.getName());
		assertThat(update.getEmail()).isEqualTo(apply.getEmail());
		
		
	}
	
		
}
	
	
	

