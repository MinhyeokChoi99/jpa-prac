package kr.co.prac.admin.service;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.prac.admin.exception.NotAuthorizedAdminException;
import kr.co.prac.global.session.SessionUtil;
import kr.co.prac.member.entity.Role;
import kr.co.prac.member.service.MemberService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminAuthService {
	
	private final MemberService memberService;
	
	// 관리자 확인(인가)
	public void requireAdmin(HttpServletRequest httpServletRequest) {
        Long loginMemberId = SessionUtil.getLoginMemberId(httpServletRequest);
        if (memberService.find(loginMemberId).getRole() != Role.ADMIN) {
            throw new NotAuthorizedAdminException();
        }
    }

}
