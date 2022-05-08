package com.toyproject.pseudo.lotto.domain.user;

import com.google.common.hash.Hashing;
import com.toyproject.pseudo.lotto.domain.common.CreationTimeEntity;
import com.toyproject.pseudo.lotto.web.user.UserDto;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class User extends CreationTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login_id")
    @Size(min = 4, max = 16)
    private String loginId;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    @Email
    private String email;

    @Column(name = "balance")
    private Long balance;

    @Column(name = "last_login")
    private LocalDateTime lastLoginAt;

    public User() {}

    public User(String loginId, String password, String email) {
        this.loginId = loginId;
        this.password = password;
        this.email = email;
        this.balance = 0L;
    }

    public static User parse(UserDto user) {
        String password = Hashing.sha256().hashString(user.getPassword() + user.getLoginId(), StandardCharsets.UTF_8).toString();
        return new User(user.getLoginId(), password, user.getEmail());
    }

    public User(UserDto User) {
        this.loginId = User.getLoginId();
        this.password = Hashing.sha256().hashString(User.getPassword(), StandardCharsets.UTF_8).toString();
        this.email = User.getEmail();
    }

    public void charge(Long amount) {
        this.balance += amount;
    }

    public void withdraw(Long amount) {
        this.balance -= amount;
    }

    public void spend(long amount) {
        this.balance -= 1000;
    }

    public void earn(Long amount) {
        this.balance += amount;
    }
}
