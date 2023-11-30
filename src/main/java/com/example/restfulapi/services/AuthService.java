package com.example.restfulapi.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.restfulapi.entities.User;
import com.example.restfulapi.models.LoginUserRequest;
import com.example.restfulapi.models.TokenResponse;
import com.example.restfulapi.repositories.UserRepository;
import com.example.restfulapi.securities.BCrypt;

import jakarta.transaction.Transactional;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public TokenResponse login(LoginUserRequest request) {
        validationService.validate(request);

        User user = userRepository.findById(request.getUsername())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password wrong"));

        if (BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            user.setToken(UUID.randomUUID().toString());
            user.setTokenExpiredAt(next30Days());
            userRepository.save(user);

            return TokenResponse
                .builder()
                .token(user.getToken())
                .expiredAt(user.getTokenExpiredAt())
                .build();
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password wrong");
        }
    }

    public Long next30Days() {
        return System.currentTimeMillis() + (1000 * 60 * 24 * 30);
    }
}
