package kr.co.prac.global.session;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.prac.login.exception.LoginRequiredException;

public class SessionUtil {
    private SessionUtil() {}

    public static Long getLoginMemberId(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession(false);
        if (session == null) {
            throw new LoginRequiredException();
        }

        Long memberId = (Long) session.getAttribute(SessionConst.LOGIN_MEMBER_ID);

        if (memberId == null) {
            throw new LoginRequiredException();
        }

        return memberId;
    }

}
