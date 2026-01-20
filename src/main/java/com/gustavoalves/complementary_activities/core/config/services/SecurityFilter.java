package com.gustavoalves.complementary_activities.core.config.services;

import com.gustavoalves.complementary_activities.core.config.entities.SystemUser;
import com.gustavoalves.complementary_activities.domain.user.User;
import com.gustavoalves.complementary_activities.domain.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

@Service
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        final String token = tokenService.recoverToken(request);

        if (token != null && !token.isBlank()) {
            final String enrollment = tokenService.validate(token);

            final User user = userRepository.getByEnrollment(Long.parseLong(enrollment));

            if (user != null) {
                final SystemUser systemUser = new SystemUser(user);

                var authentication = new UsernamePasswordAuthenticationToken(
                        systemUser,
                        null,
                        systemUser.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                throw new AccessDeniedException("Invalid token");
            }
        }

        filterChain.doFilter(request, response);
    }
}
