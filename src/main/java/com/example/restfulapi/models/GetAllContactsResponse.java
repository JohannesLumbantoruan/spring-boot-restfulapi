package com.example.restfulapi.models;

import java.util.List;
import java.util.Map;

import com.example.restfulapi.entities.Contact;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GetAllContactsResponse {
    private List<Contact> content;

    private Map<String, Integer> page;
}
