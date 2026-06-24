package kr.co.prac.admin.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.prac.global.session.SessionUtil;
import kr.co.prac.member.dto.MemberResponse;
import kr.co.prac.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/members")

public class AdminMemberController {

    private final MemberService memberService;

    @GetMapping
    public List<MemberResponse> findAll(HttpServletRequest httpServletRequest) {
        SessionUtil.requireAdmin(httpServletRequest);
        return memberService.findAll();
    }

    @GetMapping("/{number}")
    public MemberResponse findMember(@PathVariable Long number, HttpServletRequest httpServletRequest) {
        SessionUtil.requireAdmin(httpServletRequest);
        return memberService.find(number);
    }

    @DeleteMapping("/{number}")
    public String delete(@PathVariable Long number, HttpServletRequest httpServletRequest) {
        SessionUtil.requireAdmin(httpServletRequest);
        memberService.delete(number);
        return "성공";
    }
}
