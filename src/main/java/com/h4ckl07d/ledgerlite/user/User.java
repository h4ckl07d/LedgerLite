package com.h4ckl07d.ledgerlite.user;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "user_unique_email", columnNames = "email")
        }

)
public class User {

    @Id
    @GeneratedValue(
            strategy = GenerationType.UUID)
    @Column(
            name = "id",
            nullable = false
    )
    private UUID id;

    @Setter
    @Column(
            name = "name",
            nullable = false
    )
    private String name;

    @Setter
    @Column(
            name = "email",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private  String email;

    @Setter
    @Column(
            name = "password_hash",
            nullable  = false
    )
    private String password_hash;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(
            name = "role",
            nullable = false
    )
    private Role role = Role.USER;

    @Column(
            name = "created_At",
            nullable = false
    )
    private LocalDateTime created_At;
    @PrePersist
    protected void onCreate() {this.created_At = LocalDateTime.now();}


    public User(String email, String password_hash, Role role, String name) {
        this.email = email;
        this.password_hash = password_hash;
        this.role = role;
        this.name = name;
    }

}
