package com.example.restfulapi.models;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GetAllContactsResponse {
    private List<ContactResponse> content;

    private Map<String, Integer> page;
}
