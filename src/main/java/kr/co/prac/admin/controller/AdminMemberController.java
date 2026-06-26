package kr.co.prac.admin.controller;

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
    public List<MemberResponse> findAll() {
        return memberService.findAll();
    }

    @GetMapping("/{number}")
    public MemberResponse findMember(@PathVariable Long number) {
        return memberService.find(number);
    }

    @DeleteMapping("/{number}")
    public void delete(@PathVariable Long number) {
        memberService.delete(number);
    }
}
