package com.tttm.Whear.App.service;

import com.tttm.Whear.App.entity.Token;

import java.util.List;
import java.util.Optional;

public interface TokenService {
    Optional<Token> findByToken(String token);
    void saveToken(Token token);
    boolean findTokenWithNotExpiredAndNotRevoked(String token);
    List<Token> findAllValidTokenByUser(String userID);
    void revokeAllUserTokens(List<Token> tokens);
}
