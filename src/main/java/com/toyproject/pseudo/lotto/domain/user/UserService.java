package com.toyproject.pseudo.lotto.domain.user;

import com.google.common.hash.Hashing;
import com.toyproject.pseudo.lotto.domain.common.ApiResult;
import com.toyproject.pseudo.lotto.domain.statistics.Statistics;
import com.toyproject.pseudo.lotto.domain.statistics.StatisticsRepository;
import com.toyproject.pseudo.lotto.web.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final StatisticsRepository statisticsRepository;

    public ApiResult<User> findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return ApiResult.fail("유저정보를 찾을 수 없습니다.");
        }
        return ApiResult.success(user.get());
    }

    public ApiResult<User> search(String loginId) {
        User user = userRepository.findByLoginId(loginId);
        if (user == null) {
            return ApiResult.fail("회원 정보를 찾을 수 없습니다.");
        }
        return ApiResult.success(user);
    }

    public ApiResult<User> join(UserDto input) {
        if (userRepository.findByLoginId(input.getLoginId()) != null) {
            return ApiResult.fail("이미 존재하는 아이디입니다.");
        }

        User user = User.parse(input);
        user = userRepository.save(user);
        Statistics statistics = new Statistics(user);
        statisticsRepository.save(statistics);
        return ApiResult.success(user);
    }

    public ApiResult<User> login(String loginId, String password) {
        User user = userRepository.findByLoginId(loginId);
        if (user == null || !user.getPassword().equals(passwordHash(loginId, password))) {
            return ApiResult.fail("아이디 또는 비밀번호가 잘못되었습니다.");
        }

        return ApiResult.success(user);
    }

    private String passwordHash(String loginId, String password) {
        return Hashing.sha256().hashString(password + loginId, StandardCharsets.UTF_8).toString();
    }
}
