package com.example.restfulapi.services;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.restfulapi.entities.User;
import com.example.restfulapi.models.RegisterUserRequest;
import com.example.restfulapi.models.UpdateUserRequest;
import com.example.restfulapi.models.UserResponse;
import com.example.restfulapi.repositories.UserRepository;
import com.example.restfulapi.securities.BCrypt;

import jakarta.transaction.Transactional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public void register(RegisterUserRequest request) {
        validationService.validate(request);

        if (userRepository.existsById(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username already registered");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        user.setName(request.getName());

        userRepository.save(user);
    }

    public UserResponse getUser(User user) {
        return UserResponse
            .builder()
            .username(user.getUsername())
            .name(user.getName())
            .build();
    }

    @Transactional
    public UserResponse updateUser(User user, UpdateUserRequest request) {
        validationService.validate(request);

        if (Objects.nonNull(request.getName())) {
            user.setName(request.getName());
        }

        if (Objects.nonNull(request.getPassword())) {
            user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        }
        
        userRepository.save(user);

        return UserResponse
            .builder()
            .username(user.getUsername())
            .name(user.getName())
            .build();
    }
}
