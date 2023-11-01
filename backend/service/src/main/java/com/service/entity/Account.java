package com.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "account_id", unique = true)
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    // Bcrypt hashed
    @Column(name = "password")
    @JsonIgnore
    private String password;

    @Column(name = "role")
    private String role;

    @Column(name = "date_created")
    private Date dateCreated;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "is_reported")
    private Boolean isReported;
}

