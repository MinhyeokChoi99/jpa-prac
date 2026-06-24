package kr.co.prac.admin.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.prac.global.session.SessionUtil;
import kr.co.prac.member.dto.MemberResponse;
import kr.co.prac.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor

public class AdminMemberController {

    private final MemberService memberService;

    @GetMapping("/admin/members")
    public List<MemberResponse> findAll(HttpServletRequest httpServletRequest) {
        SessionUtil.requireAdmin(httpServletRequest);
        return memberService.findAll();

    }

    @DeleteMapping("/admin/members/{number}")
    public String delete(@PathVariable Long number,HttpServletRequest httpServletRequest) {
        SessionUtil.requireAdmin(httpServletRequest);
        memberService.delete(number);
        return "성공";
    }
}
