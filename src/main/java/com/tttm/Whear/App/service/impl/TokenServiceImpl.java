package com.tttm.Whear.App.service.impl;

import com.tttm.Whear.App.entity.Token;
import com.tttm.Whear.App.repository.TokenRepository;
import com.tttm.Whear.App.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;

    @Override
    public Optional<Token> findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public void saveToken(Token token) {
        tokenRepository.save(token);
    }

    @Override
    public boolean findTokenWithNotExpiredAndNotRevoked(String token) {
        return tokenRepository.findByToken(token)
                .map(t -> !t.isExpired() && !t.isRevoked())
                .orElse(false);
    }

    @Override
    public List<Token> findAllValidTokenByUser(String userID) {
        return tokenRepository.findAllValidTokenByUser(userID);
    }

    @Override
    public void revokeAllUserTokens(List<Token> tokens) {
        tokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(tokens);
    }
}
