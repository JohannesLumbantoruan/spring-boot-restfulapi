package com.example.restfulapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.restfulapi.entities.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {}
