package com.example.restfulapi.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ContactResponse {
    private String id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private List<String> addresses;
}
