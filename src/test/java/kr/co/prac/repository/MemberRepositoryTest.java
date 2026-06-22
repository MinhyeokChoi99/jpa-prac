package kr.co.prac.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import kr.co.prac.member.entity.Member;
import kr.co.prac.member.repository.MemberRepository;

@DataJpaTest
public class MemberRepositoryTest {

	@Autowired
	MemberRepository memberRepository;
	
	@Test
	@DisplayName("중복이메일O")
	void duplicated() {
		//
		String email = "abc@gmail.com";
		Member member1 = new Member();
		member1.setName("memberA");
		member1.setEmail(email);
		memberRepository.save(member1);
	
		boolean existsByEmail = memberRepository.existsByEmail(email);
		Assertions.assertThat(existsByEmail).isTrue();
	}
	
	@Test
	@DisplayName("중복이메일X")
	void notDuplicated() {
		//
		String email = "abc@gmail.com";
		String compareEmail = "cba@gmail.com";
		Member member1 = new Member();
		member1.setName("memberA");
		member1.setEmail(email);
		memberRepository.save(member1);
	
		boolean existsByEmail = memberRepository.existsByEmail(compareEmail);
		Assertions.assertThat(existsByEmail).isFalse();
	}
}
