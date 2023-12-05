package com.example.restfulapi.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.restfulapi.entities.Contact;
import com.example.restfulapi.entities.User;
import com.example.restfulapi.models.AddressRequest;
import com.example.restfulapi.models.AddressResponse;
import com.example.restfulapi.models.WebResponse;
import com.example.restfulapi.repositories.AddressRepository;
import com.example.restfulapi.repositories.ContactRepository;
import com.example.restfulapi.repositories.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class AddressControllerTest {
    private User user;

    private Contact contact;

    private AddressRequest request;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private AddressRepository addressRepository;

    @BeforeEach
    void SetUp() {
        userRepository.deleteAll();

        user = new User();
        user.setUsername("johndoe");
        user.setName("John Doe");
        user.setPassword("password");
        user.setToken("token");
        user.setTokenExpiredAt(System.currentTimeMillis() + (1000 * 60 * 24 * 30));
        userRepository.save(user);

        contact = new Contact();
        contact.setId("contact-12345");
        contact.setFirstName("John");
        contact.setLastName("Doe");
        contact.setEmail("johndoe@mail.com");
        contact.setPhone("+1-2222-3333-4444");
        contact.setUser(user);
        contactRepository.save(contact);

        request = new AddressRequest();
        request.setStreet("Jl. Batik Kumeli No. 50");
        request.setCity("Bandung");
        request.setProvince("Jawa Barat");
        request.setCountry("Indonesia");
        request.setPostalCode("41352");
    }

    @Test
    void postSuccess() throws Exception {
        mockMvc.perform(
            post("/api/contacts/" + contact.getId() + "/addresses")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("X-API-TOKEN", user.getToken())
        ).andExpectAll(
            status().isOk()
        ).andDo(result -> {
            WebResponse<AddressResponse> response = objectMapper.readValue(
                result.getResponse().getContentAsString(), new TypeReference<>() {
                });

            assertNull(response.getErrors());
            assertNotNull(response.getData());
            assertNotNull(response.getData().getId());
            assertEquals(response.getData().getStreet(), request.getStreet());
            assertEquals(response.getData().getCity(), request.getCity());
            assertEquals(response.getData().getProvince(), request.getProvince());
            assertEquals(response.getData().getCountry(), request.getCountry());
            assertEquals(response.getData().getPostalCode(), request.getPostalCode());
        });
    }

    @Test
    void postFailedCountryFieldEmpty() throws Exception {
        AddressRequest request = new AddressRequest();
        request.setCountry("");

        mockMvc.perform(
            post("/api/contacts/" + contact.getId() + "/addresses")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("X-API-TOKEN", user.getToken())
        ).andExpectAll(
            status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                result.getResponse().getContentAsString(), new TypeReference<>() {
                });

            assertNull(response.getData());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void postFailedTokenExpired() throws Exception {
        user.setTokenExpiredAt(System.currentTimeMillis() - (1000 * 60 * 24 * 30));
        userRepository.save(user);

        mockMvc.perform(
            post("/api/contacts/" + contact.getId() + "/addresses")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("X-API-TOKEN", user.getToken())
        ).andExpectAll(
            status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                result.getResponse().getContentAsString(), new TypeReference<>() {
                });

            assertNull(response.getData());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void postFailedContactNotFound() throws Exception {
        mockMvc.perform(
            post("/api/contacts/contact-notfound/addresses")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("X-API-TOKEN", user.getToken())
        ).andExpectAll(
            status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                result.getResponse().getContentAsString(), new TypeReference<>() {
                });

            assertNull(response.getData());
            assertNotNull(response.getErrors());
        });
    }
}
