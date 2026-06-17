package kr.co.prac.service.memberservice;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.prac.dto.member.MemberCreateRequest;
import kr.co.prac.dto.member.MemberResponse;
import kr.co.prac.dto.member.MemberUpdateRequest;
import kr.co.prac.entity.Member;
import kr.co.prac.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

	private final MemberRepository memberRepository;
	
	@Override
	public MemberResponse apply(MemberCreateRequest memberCreateRequest) {
		if(memberRepository.existsByEmail(memberCreateRequest.getEmail()) ) {
			throw new IllegalArgumentException("중복");
		}
		Member member = new Member();
		member.setName(memberCreateRequest.getName());
		member.setEmail(memberCreateRequest.getEmail());
		Member savedMember = memberRepository.save(member);
		return new MemberResponse(savedMember);
	}

	@Override
	public MemberResponse find(Long id) {
		Member findMember = memberRepository.findById(id).orElseThrow( () -> new IllegalArgumentException("X"));
		return new MemberResponse(findMember);
	}

	@Override
	public List<Member> findAll() {
		List<Member> members = memberRepository.findAll();
		return members;
	}

	@Override
	public void delete(Long id) {
		Member member = memberRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("X"));
		memberRepository.delete(member);
		
	}
	
	@Transactional
	@Override
	public MemberResponse update(Long id, MemberUpdateRequest memberUpdateRequest) {
		Member member = memberRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("X"));
		if(memberUpdateRequest.getName() != null && memberUpdateRequest.getName() != "") {
			member.setName(memberUpdateRequest.getName());			
		}
		if(memberUpdateRequest.getEmail() != null && memberUpdateRequest.getName() !="") {
			member.setEmail(memberUpdateRequest.getEmail());			
		}
//		memberRepository.save(member); ??
		return new MemberResponse(member);
	}
	
	


}
