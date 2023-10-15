package com.greenblat.tasklist.web.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class JwtRequest {

    @NotBlank(message = "Username must be not blank")
    private String username;

    @NotBlank(message = "Password must be not blank")
    private String password;

}
