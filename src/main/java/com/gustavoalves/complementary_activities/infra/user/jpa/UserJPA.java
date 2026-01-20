package com.gustavoalves.complementary_activities.infra.user.jpa;

import com.gustavoalves.complementary_activities.domain.user.UserType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UserJPA {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true)
    UUID uuid;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    String document;

    @Column(nullable = false, unique = true)
    long enrollment;

    @Column
    String email;

    @Column
    String phone;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    UserType type;

    @Column(nullable = false)
    String password;
}
