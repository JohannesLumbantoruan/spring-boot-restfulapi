package com.example.restfulapi.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.restfulapi.entities.Contact;
import com.example.restfulapi.entities.User;
import com.example.restfulapi.models.ContactRequest;
import com.example.restfulapi.models.ContactResponse;
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
    public ContactResponse createContact(User user, ContactRequest request) {
        validationService.validate(request);

        Contact contact = new Contact();
        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        contact.setUser(user);
        contact.setId("contact-" + System.currentTimeMillis());

        contactRepository.save(contact);

        return ContactResponse
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
        List<Contact> copiedContacts = new ArrayList<>(contacts);

        Map<String, Integer> page = new HashMap<>();

        int total = contacts.size();
        int size = 5;
        int min = (pageNumber - 1) * size;
        int max = pageNumber * size;

        if (contacts.size() < max) max = contacts.size();
        if (contacts.size() < min) contacts.clear();
        if (min != max) {
            contacts.clear();

            for (int i = min; i < max; i++) {
                contacts.add(copiedContacts.get(i));
            }
        }

        page.put("number", pageNumber);
        page.put("size", contacts.size());
        page.put("totalPages", (int)Math.ceil(total / 5.0));
        page.put("totalSize", total);

        return GetAllContactsResponse.builder().content(contacts).page(page).build();
    }

    public Contact getById(User user, String id) {
        Contact contact = contactRepository.findByUserUsernameAndId(user.getUsername(), id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "contact not found"));

        return contact;
    }

    public ContactResponse update(User user, String id, ContactRequest request) {
        validationService.validate(request);

        Contact contact = contactRepository.findByUserUsernameAndId(user.getUsername(), id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "contact not found"));

        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());

        contactRepository.save(contact);

        return ContactResponse
            .builder()
            .firstName(contact.getFirstName())
            .lastName(contact.getLastName())
            .email(contact.getEmail())
            .phone(contact.getPhone())
            .build();
    }

    public void delete(User user, String contactId) {
        if (contactRepository.existsById(contactId)) {
            contactRepository.deleteById(contactId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "contact tidak ditemukan");
        }
    }
}
