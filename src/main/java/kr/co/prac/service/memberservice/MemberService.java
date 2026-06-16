package kr.co.prac.service.memberservice;

import java.util.List;

import kr.co.prac.dto.member.MemberCreateRequest;
import kr.co.prac.dto.member.MemberResponse;
import kr.co.prac.dto.member.MemberUpdateRequest;
import kr.co.prac.entity.Member;


public interface MemberService {
	
	
	MemberResponse apply(MemberCreateRequest memberCreateRequest);
	
	MemberResponse find(Long id);
	
	List<Member> findAll();
	
	MemberResponse update(Long id, MemberUpdateRequest memberUpdateRequest);
	
	void delete(Long id);

	

}
