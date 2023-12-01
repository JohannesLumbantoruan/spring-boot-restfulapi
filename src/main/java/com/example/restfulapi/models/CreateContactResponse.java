package com.example.restfulapi.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CreateContactResponse {
    private String id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;
}
