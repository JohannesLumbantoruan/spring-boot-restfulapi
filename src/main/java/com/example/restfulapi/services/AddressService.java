package com.example.restfulapi.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.restfulapi.entities.Address;
import com.example.restfulapi.entities.Contact;
import com.example.restfulapi.entities.User;
import com.example.restfulapi.models.AddressRequest;
import com.example.restfulapi.models.AddressResponse;
import com.example.restfulapi.repositories.AddressRepository;
import com.example.restfulapi.repositories.ContactRepository;

import jakarta.transaction.Transactional;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private ContactRepository contactRepository;

    @Transactional
    public AddressResponse post(String contactId, AddressRequest request) {
        validationService.validate(request);

        Contact contact = contactRepository.findById(contactId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "contact not found"));

        Address address = new Address();
        address.setId("address-" + UUID.randomUUID().toString());
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setProvince(request.getProvince());
        address.setCountry(request.getCountry());
        address.setPostalCode(request.getPostalCode());
        address.setContact(contact);

        addressRepository.save(address);

        return AddressResponse
            .builder()
            .id(address.getId())
            .street(address.getStreet())
            .city(address.getCity())
            .province(address.getProvince())
            .country(address.getCountry())
            .postalCode(address.getPostalCode())
            .build();
    }

    public List<AddressResponse> getAll(User user, String contactId) {
        contactRepository.findByUserUsernameAndId(user.getUsername(), contactId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "contact not found"));

        List<Address> addresses = addressRepository.findAllByContactId(contactId);

        List<AddressResponse> addressResponses = addresses
            .stream()
            .map(
                address -> AddressResponse
                    .builder()
                    .id(address.getId())
                    .street(address.getStreet())
                    .city(address.getCity())
                    .province(address.getProvince())
                    .country(address.getCountry())
                    .postalCode(address.getPostalCode())
                    .build()
            )
            .collect(Collectors.toList());

        return addressResponses;
    }
}
