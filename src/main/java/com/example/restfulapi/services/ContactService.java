package com.example.restfulapi.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.restfulapi.entities.Contact;
import com.example.restfulapi.entities.User;
import com.example.restfulapi.models.CreateContactRequest;
import com.example.restfulapi.models.CreateContactResponse;
import com.example.restfulapi.models.GetAllContactsResponse;
import com.example.restfulapi.repositories.ContactRepository;

import jakarta.transaction.Transactional;

@Service
public class ContactService {
    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public CreateContactResponse createContact(User user, CreateContactRequest request) {
        validationService.validate(request);

        Contact contact = new Contact();
        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        contact.setUser(user);
        contact.setId("contact-" + System.currentTimeMillis());

        contactRepository.save(contact);

        return CreateContactResponse
            .builder()
            .id(contact.getId())
            .firstName(contact.getFirstName())
            .lastName(contact.getLastName())
            .email(contact.getEmail())
            .phone(contact.getPhone())
            .build();
    }

    public GetAllContactsResponse get(User user, int pageNumber) {
        List<Contact> contacts = contactRepository.findAllByUserUsername(user.getUsername());

        Map<String, Integer> page = new HashMap<>();

        page.put("number", pageNumber);
        page.put("size", 5);
        page.put("totalPages", (int)Math.ceil(contacts.size() / 5.0));
        page.put("totalSize", contacts.size());

        return GetAllContactsResponse.builder().content(contacts).page(page).build();
    }
}
