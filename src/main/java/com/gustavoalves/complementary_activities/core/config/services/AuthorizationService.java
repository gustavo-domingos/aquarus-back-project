package com.gustavoalves.complementary_activities.core.config.services;

import com.gustavoalves.complementary_activities.core.config.entities.SystemUser;
import com.gustavoalves.complementary_activities.domain.user.User;
import com.gustavoalves.complementary_activities.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String enrollment) {
        final User user = userRepository.getByEnrollment(Long.parseLong(enrollment));
        if (user != null) {
            return new SystemUser(user);
        }
        return null;
    }
}
