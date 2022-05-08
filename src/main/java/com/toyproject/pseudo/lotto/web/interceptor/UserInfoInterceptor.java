package com.toyproject.pseudo.lotto.web.interceptor;

import com.toyproject.pseudo.lotto.domain.common.StringUtil;
import com.toyproject.pseudo.lotto.domain.user.User;
import com.toyproject.pseudo.lotto.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@RequiredArgsConstructor
public class UserInfoInterceptor implements HandlerInterceptor {

    private final UserRepository userRepository;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Long id = Long.parseLong(String.valueOf(session.getAttribute("userId")));
            Optional<User> user = userRepository.findById(id);
            if (user.isPresent()) {
                System.out.println(user.toString());
                modelAndView.addObject("user", user.get());
                modelAndView.addObject("balance", StringUtil.toMoneyFormat(user.get().getBalance()));
            }
        }
    }
}
