package kr.co.prac.admin.service;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.prac.admin.exception.NotAuthorizedAdminException;
import kr.co.prac.global.session.SessionUtil;
import kr.co.prac.member.entity.Member;
import kr.co.prac.member.entity.Role;
import kr.co.prac.member.exception.MemberNotFoundException;
import kr.co.prac.member.repository.MemberRepository;
import kr.co.prac.member.service.MemberService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminAuthService {
	
	private final MemberRepository memberRepository;
	
	// 관리자 확인(인가)
	public void requireAdmin(HttpServletRequest httpServletRequest) {
        Long loginMemberId = SessionUtil.getLoginMemberId(httpServletRequest);
        Member member = memberRepository.findById(loginMemberId).orElseThrow(MemberNotFoundException::new);
        if (member.getRole() != Role.ADMIN) {
            throw new NotAuthorizedAdminException();
        }
    }

}
