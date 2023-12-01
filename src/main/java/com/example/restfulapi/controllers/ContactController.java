package com.example.restfulapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.restfulapi.entities.User;
import com.example.restfulapi.models.CreateContactRequest;
import com.example.restfulapi.models.CreateContactResponse;
import com.example.restfulapi.models.WebResponse;
import com.example.restfulapi.services.ContactService;

@RestController
public class ContactController {
    @Autowired
    private ContactService contactService;

    @PostMapping(
        path = "/api/contacts",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<CreateContactResponse> createContact(
        User user, 
        @RequestBody CreateContactRequest request
    ) {
        CreateContactResponse response = contactService.createContact(user, request);

        return WebResponse.<CreateContactResponse>builder().data(response).build();
    }
}
