package com.example.restfulapi.entities;

import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "contacts")
public class Contact {
    @Id
    private String id;

    @Column(nullable = false)
    private String firstName;

    private String lastName;

    @ManyToOne()
    @JoinColumn(
        name = "username",
        referencedColumnName = "username",
        nullable = false,
        foreignKey = @ForeignKey(name = "fkey_contacts_users")
    )
    @JsonBackReference
    private User user;

    private String phone;

    private String email;

    @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL)
    private List<Address> address;
}
