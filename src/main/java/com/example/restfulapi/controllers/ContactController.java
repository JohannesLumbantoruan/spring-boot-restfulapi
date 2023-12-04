package com.example.restfulapi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.restfulapi.entities.Contact;
import com.example.restfulapi.entities.User;
import com.example.restfulapi.models.ContactRequest;
import com.example.restfulapi.models.ContactResponse;
import com.example.restfulapi.models.GetAllContactsResponse;
import com.example.restfulapi.models.WebResponse;
import com.example.restfulapi.repositories.ContactRepository;
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
    public WebResponse<ContactResponse> createContact(
        User user, 
        @RequestBody ContactRequest request
    ) {
        ContactResponse response = contactService.createContact(user, request);

        return WebResponse.<ContactResponse>builder().data(response).build();
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

    @GetMapping(
        path = "/api/contacts/{contactId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<Contact> getContactById(
        User user, 
        @PathVariable(name = "contactId") String id
    ) {
        Contact response = contactService.getById(user, id);

        return WebResponse.<Contact>builder().data(response).build();
    }

    @PatchMapping(
        path = "/api/contacts/{contactId}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ContactResponse> update(
        User user,
        @PathVariable(name = "contactId") String id,
        @RequestBody ContactRequest request
    ) {
        ContactResponse response = contactService.update(user, id, request);

        return WebResponse
            .<ContactResponse>builder()
            .data(response)
            .build();
    }

    @DeleteMapping(
        path = "/api/contacts/{contactId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> delete(
        User user,
        @PathVariable String contactId
    ) {
        contactService.delete(user, contactId);

        return WebResponse.<String>builder().data("Ok").build();
    }

    @GetMapping(
        path = "/api/contacts/search",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<Page<ContactResponse>> search(
        User user,
        @RequestParam(name = "id", required = false) String id,
        @RequestParam(name = "firstName", required = false) String firstName,
        @RequestParam(name = "lastName", required = false) String lastName,
        @RequestParam(name = "email", required = false) String email,
        @RequestParam(name = "phone", required = false) String phone,
        @RequestParam(name = "page", defaultValue = "1") int page,
        @RequestParam(name = "size", defaultValue = "5") int size
    ) {
        Page<ContactResponse> response = contactService.search(user, id, firstName, lastName, email, phone, page, size);

        return WebResponse.<Page<ContactResponse>>builder().data(response).build();
    }
}
