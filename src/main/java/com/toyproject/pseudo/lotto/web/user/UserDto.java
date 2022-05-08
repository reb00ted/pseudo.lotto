package com.toyproject.pseudo.lotto.web.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class UserDto {

    @Size(min = 4, max = 16)
    @NotEmpty
    String loginId;

    @Size(min = 8, max = 24)
    @NotEmpty
    String password;

    @Email
//    @NotEmpty
    String email;
}