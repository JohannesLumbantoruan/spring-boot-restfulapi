package com.example.restfulapi.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.MockMvc.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.restfulapi.entities.Contact;
import com.example.restfulapi.entities.User;
import com.example.restfulapi.models.ContactRequest;
import com.example.restfulapi.models.ContactResponse;
import com.example.restfulapi.models.GetAllContactsResponse;
import com.example.restfulapi.models.WebResponse;
import com.example.restfulapi.repositories.ContactRepository;
import com.example.restfulapi.repositories.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class ContactControllerTest {
    private User user;

    private ContactRequest request;

    private Contact contact;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void SetUp() {
        contactRepository.deleteAll();
        userRepository.deleteAll();

        user = new User();

        user.setUsername("johndoe");
        user.setName("John Doe");
        user.setPassword("password");
        user.setToken("token");
        user.setTokenExpiredAt(System.currentTimeMillis() + (1000 * 60 * 24 * 30));

        userRepository.save(user);

        request = new ContactRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("Johndoe@mail.com");
        request.setPhone("+1-2345-6789-0000");

        contact = new Contact();
        contact.setId("contact-12345");
        contact.setFirstName("John");
        contact.setUser(user);

        contactRepository.save(contact);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        contactRepository.deleteAll();
    }

    @Test
    void createContactSuccess() throws Exception {
        mockMvc.perform(
            post("/api/contacts")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("X-API-TOKEN", "token")
        ).andExpectAll(
            status().isOk()
        ).andDo(result -> {
            WebResponse<ContactResponse> response = objectMapper.readValue(
                result.getResponse().getContentAsString(), new TypeReference<>() {}
            );

            assertNull(response.getErrors());
            assertNotNull(response.getData());
        });
    }

    @Test
    void createContactFailedInvalidToken() throws Exception {
        mockMvc.perform(
            post("/api/contacts")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("X-API-TOKEN", "invalidtoken")
        ).andExpectAll(
            status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<ContactResponse> response = objectMapper.readValue(
                result.getResponse().getContentAsString(), new TypeReference<>() {}
            );

            assertNull(response.getData());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void createContactFailedNoHeader() throws Exception {
        mockMvc.perform(
            post("/api/contacts")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
            status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<ContactResponse> response = objectMapper.readValue(
                result.getResponse().getContentAsString(), new TypeReference<>() {}
            );

            assertNull(response.getData());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void createContactFailedExpiredToken() throws Exception {
        user.setTokenExpiredAt(System.currentTimeMillis() - (1000 * 60 * 24 * 30));
        userRepository.save(user);

        mockMvc.perform(
            post("/api/contacts")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("X-API-TOKEN", "token")
        ).andExpectAll(
            status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<ContactResponse> response = objectMapper.readValue(
                result.getResponse().getContentAsString(), new TypeReference<>() {}
            );

            assertNull(response.getData());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void getAllContactsSuccess() throws Exception {
        mockMvc.perform(
            get("/api/contacts")
                .accept(MediaType.APPLICATION_JSON)
                .header("X-API-TOKEN", "token")
        ).andExpectAll(
            status().isOk()
        ).andDo(result -> {
            WebResponse<GetAllContactsResponse> response = objectMapper.readValue(
                result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());
            assertNotNull(response.getData());
        });
    }

    @Test
    void getAllContactsFailedInvalidToken() throws Exception {
        mockMvc.perform(
            get("/api/contacts")
                .accept(MediaType.APPLICATION_JSON)
                .header("X-API-TOKEN", "invalidtoken")
        ).andExpectAll(
            status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<GetAllContactsResponse> response = objectMapper.readValue(
                result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getData());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void getAllContactsFailedTokenExpired() throws Exception {
        user.setTokenExpiredAt(System.currentTimeMillis() - (1000 * 60 * 24 * 30));
        userRepository.save(user);

        mockMvc.perform(
            get("/api/contacts")
                .accept(MediaType.APPLICATION_JSON)
                .header("X-API-TOKEN", user.getToken())
        ).andExpectAll(
            status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<GetAllContactsResponse> response = objectMapper.readValue(
                result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getData());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void getContactByIdSuccess() throws Exception {        
        mockMvc.perform(
            get("/api/contacts/" + contact.getId())
                .accept(MediaType.APPLICATION_JSON)
                .header("X-API-TOKEN", user.getToken())
        ).andExpectAll(
            status().isOk()
        ).andDo(result -> {
            WebResponse<Contact> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getData());
            assertNull(response.getErrors());
        });
    }

    @Test
    void getContactByIdFailedNotFound() throws Exception {
        mockMvc.perform(
            get("/api/contacts/contact-notfound")
                .accept(MediaType.APPLICATION_JSON)
                .header("X-API-TOKEN", user.getToken())
        ).andExpectAll(
            status().isNotFound()
        ).andDo(result -> {
            WebResponse<Contact> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
            assertNull(response.getData());
        });
    }

    @Test
    void getContactByIdFailedInvalidToken() throws Exception {
        mockMvc.perform(
            get("/api/contacts/" + contact.getId())
                .accept(MediaType.APPLICATION_JSON)
                .header("X-API-TOKEN", "invalidtoken")
        ).andExpectAll(
            status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<Contact> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
            assertNull(response.getData());
        });
    }

    @Test
    void getContactByIdFailedExpiredToken() throws Exception {
        user.setTokenExpiredAt(System.currentTimeMillis() - (1000 * 60 * 24 * 30));
        userRepository.save(user);

        mockMvc.perform(
            get("/api/contacts/" + contact.getId())
                .accept(MediaType.APPLICATION_JSON)
                .header("X-API-TOKEN", user.getToken())
        ).andExpectAll(
            status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<Contact> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
            assertNull(response.getData());
        });
    }
}