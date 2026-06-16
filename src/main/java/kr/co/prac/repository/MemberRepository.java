package kr.co.prac.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.prac.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{
	
	boolean existsByEmail(String email);
}
