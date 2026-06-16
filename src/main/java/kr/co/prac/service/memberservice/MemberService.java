package kr.co.prac.service.memberservice;

import java.util.List;

import kr.co.prac.dto.MemberCreateRequest;
import kr.co.prac.dto.MemberResponse;
import kr.co.prac.entity.Member;


public interface MemberService {
	
	
	MemberResponse apply(MemberCreateRequest memberCreateRequest);
	
	MemberResponse find(Long id);
	
	List<Member> findAll();
	
	void delete(Long id);

	

}
