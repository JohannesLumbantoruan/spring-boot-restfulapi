package com.example.restfulapi.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.restfulapi.entities.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, String> {
    List<Contact> findAllByUserUsername(String username);

    Optional<Contact> findByUserUsernameAndId(String username, String id);
}
