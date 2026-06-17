package kr.co.prac.intergration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kr.co.prac.dto.member.MemberCreateRequest;
import kr.co.prac.dto.member.MemberResponse;
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
		
		
		
		
	}
	
	
	
}
