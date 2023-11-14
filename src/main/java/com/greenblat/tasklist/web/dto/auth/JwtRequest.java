package com.greenblat.tasklist.web.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request for login")
public class JwtRequest {

    @Schema(description = "email", example = "johndoe@gmail.com")
    @NotBlank(message = "Username must be not blank")
    private String username;

    @Schema(description = "password", example = "12345")
    @NotBlank(message = "Password must be not blank")
    private String password;

}
