package com.example.restfulapi.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class ResponsePageImpl<T> extends PageImpl<T> {
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ResponsePageImpl(
        @JsonProperty("content") List<T> content,
        @JsonProperty("pageable") JsonNode pageable,
        @JsonProperty("last") boolean last,
        @JsonProperty("totalElements") Long totalElements,
        @JsonProperty("totalPages") int totalPages,
        @JsonProperty("first") boolean first,
        @JsonProperty("numberOfElements") int numberOfElements,
        @JsonProperty("size") int size,
        @JsonProperty("number") int number,
        @JsonProperty("sort") JsonNode sort,
        @JsonProperty("empty") boolean empty
    ) {
        super(content, PageRequest.of(number, size), totalElements);
    }

    public ResponsePageImpl(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public ResponsePageImpl(List<T> content) {
        super(content);
    }

    public ResponsePageImpl() {
        super(new ArrayList<T>());
    }
}
