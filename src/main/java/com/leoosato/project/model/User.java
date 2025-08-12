package com.leoosato.project.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.util.UUID;

import java.time.OffsetDateTime;

@Entity @Table(name = "users")
public class User extends PanacheEntityBase {
    @Id @GeneratedValue public java.util.UUID id;

    @Column(unique = true, nullable = false)
    public String username;

    @Column(nullable = false)
    public String passwordHash;

    @Column(nullable = false)
    public String role; // USER / ADMIN

    @Column(nullable = false)
    public OffsetDateTime createdAt = OffsetDateTime.now();
}