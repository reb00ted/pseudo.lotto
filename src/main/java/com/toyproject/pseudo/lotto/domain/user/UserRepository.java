package com.toyproject.pseudo.lotto.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;

public interface UserRepository extends JpaRepository<User, Long> {

    public User findByLoginId(String loginId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public User findWithLockById(Long id);

}
