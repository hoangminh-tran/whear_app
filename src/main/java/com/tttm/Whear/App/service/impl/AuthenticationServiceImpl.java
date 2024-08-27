package com.tttm.Whear.App.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tttm.Whear.App.constant.ConstantMessage;
import com.tttm.Whear.App.entity.Token;
import com.tttm.Whear.App.entity.User;
import com.tttm.Whear.App.enums.TokenType;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.repository.TokenRepository;
import com.tttm.Whear.App.service.AuthenticationService;
import com.tttm.Whear.App.service.JwtService;
import com.tttm.Whear.App.service.TokenService;
import com.tttm.Whear.App.service.UserService;
import com.tttm.Whear.App.utils.request.AuthenticationRequest;
import com.tttm.Whear.App.utils.request.UserRequest;
import com.tttm.Whear.App.utils.response.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final TokenService tokenService;
    private static final Logger logger = LogManager.getLogger(AuthenticationServiceImpl.class);
    @Override
    public AuthenticationResponse register(UserRequest userRequest) throws CustomException {

        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        var user = userService.registerNewUsers(userRequest);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse
                .builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .user(userService.convertToUserResponse(user))
                .build();
    }

    @Override
    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) throws CustomException {
        if (authenticationRequest.getPassword().isBlank() || authenticationRequest.getPassword().isEmpty() ||
                authenticationRequest.getEmail().isEmpty() || authenticationRequest.getEmail().isBlank()) {
            throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );
        var user = userService.getUserByEmail(authenticationRequest.getEmail());
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse
            .builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .user(userService.convertToUserResponse(user))
            .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .userToken(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenService.saveToken(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenService.findAllValidTokenByUser(user.getUserID());
        if (validUserTokens.isEmpty())
            return;
        tokenService.revokeAllUserTokens(validUserTokens);
    }

    public AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return new AuthenticationResponse();
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            User user = null;
            try {
                user = userService.getUserByEmail(userEmail);
            } catch (CustomException e) {
                throw new RuntimeException(e);
            }
            if (jwtService.isValidToken(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse
                    .builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .user(userService.convertToUserResponse(user))
                    .build();

                return authResponse;
            }
        }
        return new AuthenticationResponse();
    }
}
