package kr.co.prac.service.memberservice;

import java.util.List;
import org.springframework.stereotype.Service;

import kr.co.prac.dto.MemberCreateRequest;
import kr.co.prac.dto.MemberResponse;
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
		memberRepository.delete(id);
		
	}
	
	


}
