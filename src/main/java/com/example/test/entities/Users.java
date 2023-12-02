package com.example.test.entities;

import com.example.test.enums.Role;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Entity
@NoArgsConstructor
@Table(name =  "users")
@Data
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String status;


    @Column(name = "email", unique = true)
    private String email;

    @JsonBackReference
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @PrePersist
    public void setCreatedAt() {
        this.createdAt = new Date();
    }
}