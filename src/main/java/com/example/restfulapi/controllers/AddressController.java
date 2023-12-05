package com.example.restfulapi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.restfulapi.entities.User;
import com.example.restfulapi.models.AddressRequest;
import com.example.restfulapi.models.AddressResponse;
import com.example.restfulapi.models.WebResponse;
import com.example.restfulapi.services.AddressService;

@RestController
public class AddressController {
    @Autowired
    private AddressService addressService;

    @PostMapping(
        path = "/api/contacts/{contactId}/addresses",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AddressResponse> post(
        User user,
        @PathVariable String contactId,
        @RequestBody AddressRequest request
    ) {
        AddressResponse address = addressService.post(contactId, request);

        return WebResponse.<AddressResponse>builder().data(address).build();
    }

    @GetMapping(
        path = "/api/contacts/{contactId}/addresses",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<Page<AddressResponse>> getAll(
        User user,
        @PathVariable String contactId,
        @RequestParam(name = "id", required = false) String id,
        @RequestParam(name = "street", required = false) String street,
        @RequestParam(name = "city", required = false) String city,
        @RequestParam(name = "province", required = false) String province,
        @RequestParam(name = "country", required = false) String country,
        @RequestParam(name = "postalCode", required = false) String postalCode,
        @RequestParam(name = "page", defaultValue = "1") int page,
        @RequestParam(name = "size", defaultValue = "5") int size
    ) {
        Page<AddressResponse> response = addressService.search(
            user, contactId, id, street, city, province, country, postalCode, page, size);

        return WebResponse.<Page<AddressResponse>>builder().data(response).build();
    }
}
