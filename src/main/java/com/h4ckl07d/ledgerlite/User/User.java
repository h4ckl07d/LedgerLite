package com.h4ckl07d.ledgerlite.User;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;

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
    private Long id;

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
            name = "password",
            nullable  = false
    )
    private String password;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(
            name = "user_role",
            nullable = false
    )
    private RoleEnum role;

    @Column(
            name = "created_At",
            nullable = false
    )
    private LocalDateTime created_At;
    @PrePersist
    protected void onCreate() {this.created_At = LocalDateTime.now();}


    public User(String email, String password, RoleEnum role, String name) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.name = name;
    }

}
