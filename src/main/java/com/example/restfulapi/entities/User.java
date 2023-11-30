package com.example.restfulapi.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String token;

    @Column(nullable = false)
    private String name;

    private Long tokenExpiredAt;

    @OneToMany(mappedBy = "user")
    private List<Contact> contact;
}
