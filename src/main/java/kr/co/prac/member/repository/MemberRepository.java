package kr.co.prac.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.prac.member.entity.Member;
import java.util.List;
import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long>{
	
	boolean existsByEmail(String email);
	
	Optional<Member> findByEmail(String email);
	
	Optional<Member> findByPassword(String password);
}
