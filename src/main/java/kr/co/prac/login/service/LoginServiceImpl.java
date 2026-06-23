package kr.co.prac.login.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.prac.login.dto.LoginRequest;
import kr.co.prac.login.dto.LoginResponse;
import kr.co.prac.login.exception.InvalidPasswordException;
import kr.co.prac.member.entity.Member;
import kr.co.prac.member.exception.MemberNotFoundException;
import kr.co.prac.member.repository.MemberRepository;
import kr.co.prac.member.service.MemberService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService{
	
	private final MemberRepository memberRepository;
	@Override
	public LoginResponse login(LoginRequest loginRequest) {
		Optional<Member> memberOptional = memberRepository.findByEmail(loginRequest.getEmail());
		Member member = memberOptional.orElseThrow(MemberNotFoundException::new);
		String requestPassword = loginRequest.getPassword();
		String dbPassword = member.getPassword();
		if(!requestPassword.equals(dbPassword)) {
			throw new InvalidPasswordException();
		}
		return new LoginResponse(member);
	}
	@Override
	public void logout(HttpServletRequest httpServletRequest) {
		HttpSession session = httpServletRequest.getSession(false);
		if(session != null) {
			session.invalidate();
		}
	}
	
	
}
