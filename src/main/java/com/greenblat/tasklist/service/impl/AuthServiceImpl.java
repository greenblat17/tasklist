package com.greenblat.tasklist.service.impl;

import com.greenblat.tasklist.service.AuthService;
import com.greenblat.tasklist.service.UserService;
import com.greenblat.tasklist.web.dto.auth.JwtRequest;
import com.greenblat.tasklist.web.dto.auth.JwtResponse;
import com.greenblat.tasklist.web.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public JwtResponse login(JwtRequest loginRequest) {
        var jwtResponse = new JwtResponse();

        var token = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        authenticationManager.authenticate(token);

        var user = userService.getByUsername(loginRequest.getUsername());

        return jwtTokenProvider.createJwtResponse(jwtResponse, user.getId(), user.getUsername(), user.getRoles());
    }

    @Override
    public JwtResponse refresh(String refreshToken) {
        return jwtTokenProvider.refreshUserTokens(refreshToken);
    }
}
