package com.example.restfulapi.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.restfulapi.entities.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, String> {
    List<Contact> findAllByUserUsername(String username);

    Optional<Contact> findByUserUsernameAndId(String username, String id);

    boolean existsByUserUsernameAndId(String username, String id);

    void deleteByUserUsernameAndId(String username, String id);

    @Query(value =
        "SELECT c.* FROM contacts c WHERE "+
        "(:id IS null OR c.id LIKE %:id%) AND "+
        "(:firstName IS null OR c.first_name LIKE %:firstName%) AND "+
        "(:lastName IS null OR c.last_name LIKE %:lastName%) AND "+
        "(:email IS null OR c.email LIKE %:email%) AND "+
        "(:phone IS null OR c.phone LIKE %:phone%)",
        nativeQuery = true
    )
    List<Contact> findContactsByAttributes(
        @Param("id") String id,
        @Param("firstName") String firstName,
        @Param("lastName") String lastName,
        @Param("email") String email,
        @Param("phone") String phone
    );
}
