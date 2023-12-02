package com.example.restfulapi.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.restfulapi.entities.Contact;
import com.example.restfulapi.entities.User;
import com.example.restfulapi.models.CreateContactRequest;
import com.example.restfulapi.models.CreateContactResponse;
import com.example.restfulapi.models.GetAllContactsResponse;
import com.example.restfulapi.models.WebResponse;
import com.example.restfulapi.repositories.ContactRepository;
import com.example.restfulapi.services.ContactService;

@RestController
public class ContactController {
    @Autowired
    private ContactService contactService;

    @Autowired
    private ContactRepository contactRepository;

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

    @GetMapping(
        path = "/api/contacts",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<GetAllContactsResponse> getAllContacts(
        User user,
        @RequestParam(name = "page", defaultValue = "1") int pageNumber
    ) {
        GetAllContactsResponse response = contactService.get(user, pageNumber);

        return WebResponse.<GetAllContactsResponse>builder().data(response).build();
    }
}
