package kr.co.prac.controller.membercontroller;

import java.util.List;


import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kr.co.prac.dto.member.MemberCreateRequest;
import kr.co.prac.dto.member.MemberResponse;
import kr.co.prac.dto.member.MemberUpdateRequest;
import kr.co.prac.entity.Member;
import kr.co.prac.service.memberservice.MemberService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberController {


	private final MemberService memberServiceImpl;

	
	@PostMapping("/member/apply") 
	public MemberResponse apply(@RequestBody MemberCreateRequest mr) {
		MemberResponse appliedMember = memberServiceImpl.apply(mr);
		return appliedMember;
	}
	
	@GetMapping("/member/{number}")
	public MemberResponse find(@PathVariable Long number) {
		MemberResponse mr = memberServiceImpl.find(number);
		return mr;
	}
	
	@GetMapping("/member")
	public List<Member> findAll() {
		List<Member> members = memberServiceImpl.findAll();
		return members;
	}
	
	@PutMapping("/member/{number}")
	public MemberResponse update(@PathVariable Long number, @RequestBody MemberUpdateRequest memberUpdateRequest) {
		return memberServiceImpl.update(number, memberUpdateRequest);
	}
	
	@DeleteMapping("member/{number}")
	public String delete(@PathVariable Long number) {
		memberServiceImpl.delete(number);
		return "성공";
	}
	
	
}
