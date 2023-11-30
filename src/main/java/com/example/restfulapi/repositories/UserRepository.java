package com.example.restfulapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.restfulapi.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {}