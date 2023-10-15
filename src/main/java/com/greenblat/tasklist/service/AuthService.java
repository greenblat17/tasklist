package com.greenblat.tasklist.service;

import com.greenblat.tasklist.web.dto.auth.JwtRequest;
import com.greenblat.tasklist.web.dto.auth.JwtResponse;

public interface AuthService {

    JwtResponse login(JwtRequest loginRequest);

    JwtResponse refresh(String refreshToken);

}
