package com.gustavoalves.complementary_activities.core.config.entities;

import com.gustavoalves.complementary_activities.domain.user.User;

import java.util.List;

public class SystemUser extends org.springframework.security.core.userdetails.User {
    private final User user;

    public SystemUser(User user) {
        super(
                String.valueOf(user.getEnrollment()),
                user.getPassword(),
                true,
                true,
                true,
                true,
                List.of()
        );
        this.user = user;
    }

    public User getUserData(){
        return this.user;
    }
}
