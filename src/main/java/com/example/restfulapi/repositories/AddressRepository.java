package com.example.restfulapi.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.restfulapi.entities.Address;
import com.example.restfulapi.entities.Contact;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {
    // Page<Address> findAllByContactId(String contactId, Pageable pageable);

    @Query(
        "SELECT a FROM Address a WHERE "+
        "(a.contact = :contact) AND "+
        "(:id IS NULL OR a.id LIKE %:id%) AND "+
        "(:street IS NULL OR a.street LIKE %:street%) AND "+
        "(:city IS NULL OR a.city LIKE %:city%) AND "+
        "(:province IS NULL OR a.province LIKE %:province%) AND "+
        "(:country IS NULL OR a.country LIKE %:country%) AND "+
        "(:postalCode IS NULL OR a.postalCode LIKE %:postalCode%)"
    )
    Page<Address> findAddressesByAttributes(
        @Param("contact") Contact contact,
        @Param("id") String id,
        @Param("street") String street,
        @Param("city") String city,
        @Param("province") String province,
        @Param("country") String country,
        @Param("postalCode") String postalCode,
        Pageable pageable
    );
}
