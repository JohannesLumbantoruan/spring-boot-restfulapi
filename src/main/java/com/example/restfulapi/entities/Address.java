package com.example.restfulapi.entities;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "addresses")
public class Address {
    @Id
    private String id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(
        foreignKey = @ForeignKey(name = "fkey_addresses_contacts"),
        nullable = false,
        referencedColumnName = "id"
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Contact contact;

    private String street;

    private String province;

    @Column(nullable = false)
    private String country;

    private String postalCode;
}
