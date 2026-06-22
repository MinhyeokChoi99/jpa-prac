package kr.co.prac.member.service;

import java.util.List;

import kr.co.prac.member.dto.MemberCreateRequest;
import kr.co.prac.member.dto.MemberResponse;
import kr.co.prac.member.dto.MemberUpdateRequest;


public interface MemberService {
	
	
	MemberResponse apply(MemberCreateRequest memberCreateRequest);
	
	MemberResponse find(Long id);
	
	List<MemberResponse> findAll();
	
	MemberResponse update(Long id, MemberUpdateRequest memberUpdateRequest);
	
	void delete(Long id);

	

}
