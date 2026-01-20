package com.gustavoalves.complementary_activities.domain.user;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class User {
    UUID uuid;
    String name;
    String document;
    long enrollment;
    String email;
    String phone;
    UserType type;
    String password;
}

