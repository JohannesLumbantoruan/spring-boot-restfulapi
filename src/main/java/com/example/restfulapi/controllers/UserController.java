package com.example.restfulapi.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.restfulapi.entities.User;
import com.example.restfulapi.models.RegisterUserRequest;
import com.example.restfulapi.models.UpdateUserRequest;
import com.example.restfulapi.models.UserResponse;
import com.example.restfulapi.models.WebResponse;
import com.example.restfulapi.repositories.UserRepository;
import com.example.restfulapi.services.UserService;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping(
        path = "/api/users",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> register(@RequestBody RegisterUserRequest request) {
        userService.register(request);

        return WebResponse.<String>builder().data("OK").build();
    }

    @GetMapping("/api/users")
    public WebResponse<List<UserResponse>> getAllUsers() {
        List<User> users = userRepository.findAll();

        List<UserResponse> userResponses = users
            .stream()
            .map(
                user -> UserResponse
                    .builder()
                    .username(user.getUsername())
                    .name(user.getName())
                    .build()
            )
            .collect(Collectors.toList());


        return WebResponse.<List<UserResponse>>builder().data(userResponses).build();
    }

    @GetMapping("/api/users/current")
    public WebResponse<UserResponse> getUser(User user) {
        UserResponse userResponse = userService.getUser(user);

        return WebResponse
            .<UserResponse>builder()
            .data(userResponse)
            .build();
    }

    @PutMapping("/api/users")
    public WebResponse<UserResponse> updateUser(
        User user, @RequestBody UpdateUserRequest request
    ) {
        UserResponse userResponse = userService.updateUser(user, request);

        return WebResponse.<UserResponse>builder().data(userResponse).build();
    }
}
