package com.example.restfulapi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public WebResponse<List<AddressResponse>> getAll(
        User user,
        @PathVariable String contactId
    ) {
        List<AddressResponse> response = addressService.getAll(user, contactId);

        return WebResponse.<List<AddressResponse>>builder().data(response).build();
    }
}
