package kr.co.prac.member.service;

import java.util.List;

import kr.co.prac.member.exception.AlreadyExistMember;
import kr.co.prac.member.exception.MemberNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.prac.member.dto.MemberCreateRequest;
import kr.co.prac.member.dto.MemberResponse;
import kr.co.prac.member.dto.MemberUpdateRequest;
import kr.co.prac.member.entity.Member;
import kr.co.prac.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService{

	private final MemberRepository memberRepository;
	
	@Override
	public MemberResponse apply(MemberCreateRequest memberCreateRequest) {
		if(memberRepository.existsByEmail(memberCreateRequest.getEmail()) ) {
			throw new AlreadyExistMember();
		}
		Member member = new Member();
		member.setName(memberCreateRequest.getName());
		member.setEmail(memberCreateRequest.getEmail());
		Member savedMember = memberRepository.save(member);
		return new MemberResponse(savedMember);
	}

	@Override
	@Transactional(readOnly = true)
	public MemberResponse find(Long id) {
		Member findMember = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
		return new MemberResponse(findMember);
	}

	@Override
	@Transactional(readOnly = true)
	public List<MemberResponse> findAll() {
		List<Member> members = memberRepository.findAll();
		return members.stream().map(MemberResponse::new).toList();
		
	}

	@Override
	public void delete(Long id) {
		Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
		memberRepository.delete(member);
		
	}
	
	@Override
	public MemberResponse update(Long id, MemberUpdateRequest memberUpdateRequest) {
		Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
		if(memberUpdateRequest.getName() != null && !memberUpdateRequest.getName().equals("")) {
			member.setName(memberUpdateRequest.getName());			
		}
		if(memberUpdateRequest.getEmail() != null && !memberUpdateRequest.getEmail().equals("")) {
			member.setEmail(memberUpdateRequest.getEmail());			
		}	
		
		
		return new MemberResponse(member);
	}
	
	


}
