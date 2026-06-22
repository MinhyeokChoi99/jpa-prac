package kr.co.prac.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.prac.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{
	
	boolean existsByEmail(String email);
}
