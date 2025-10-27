package com.gustavoalves.complementary_activities.domain.user;

import com.gustavoalves.complementary_activities.core.config.entities.SystemUser;
import com.gustavoalves.complementary_activities.core.config.services.TokenService;
import com.gustavoalves.complementary_activities.utils.entities.DefaultError;
import com.gustavoalves.complementary_activities.utils.entities.Pagination;
import com.gustavoalves.complementary_activities.utils.entities.Response;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.gustavoalves.complementary_activities.utils.fuctions.CPFUtils.isValidCpf;

@Service
public class UserUseCaseImpl implements UserUseCase {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Log logger = LogFactory.getLog(UserUseCaseImpl.class);

    private final DefaultError nameIsEmpty = new DefaultError("NAME_IS_EMPTY", "Name is empty");
    private final DefaultError documentIsInvalid = new DefaultError("DOCUMENT_IS_INVALID", "Document is invalid");
    private final DefaultError userAlreadyExists = new DefaultError("USER_ALREADY_EXISTS", "User already exists");
    private final DefaultError userNoExists = new DefaultError("USER_NO_EXISTS", "User not exists");
    private final DefaultError unexpected = new DefaultError("UNEXPECTED", "A unexpected error has occurred");
    private final DefaultError passwordIsEmpty = new DefaultError("PASSWORD_IS_EMPTY", "Password is empty");
    private final DefaultError enrollmentIsEmpty = new DefaultError("ENROLMENT_IS_EMPTY", "Enrollment is empty");

    @Override
    public Response<User> create(User user) {
        try {
            var exists = this.userRepository.getByEnrollment(user.enrollment);
            if (exists != null) {
                return new Response<>(userAlreadyExists);
            }
            if (user.name.isBlank()) {
                return new Response<>(nameIsEmpty);
            }
            if (!isValidCpf(user.document)) {
                return new Response<>(documentIsInvalid);
            }
            if (user.password.isBlank()) {
                return new Response<>(passwordIsEmpty);
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));

            return new Response<>(
                    this.userRepository.create(user)
            );
        } catch (Exception e) {
            logger.error("Error on create user", e);
            return new Response<>(unexpected);
        }
    }

    @Override
    public Response<User> update(User user) {
        try {
            var exists = this.userRepository.getByUUID(user.uuid);
            if (exists == null) {
                return new Response<>(userNoExists);
            }
            if (user.name.isBlank()) {
                return new Response<>(nameIsEmpty);
            }
            if (!isValidCpf(user.document)) {
                return new Response<>(documentIsInvalid);
            }
            if (!user.password.isBlank()) {
                return new Response<>(passwordIsEmpty);
            }
            return new Response<>(
                    this.userRepository.update(user)
            );
        } catch (Exception e) {
            logger.error("Error on update user", e);
            return new Response<>(unexpected);
        }
    }

    @Override
    public Response<User> getByUUID(UUID uuid) {
        try {
            var exists = this.userRepository.getByUUID(uuid);
            if (exists == null) {
                return new Response<>(userNoExists);
            }
            return new Response<>(exists);
        } catch (Exception e) {
            logger.error("Error on get user by uuid", e);
            return new Response<>(unexpected);
        }
    }

    @Override
    public Response<User> getByEnrollment(long enrollment) {
        try {
            var exists = this.userRepository.getByEnrollment(enrollment);
            if (exists == null) {
                return new Response<>(userNoExists);
            }
            return new Response<>(exists);
        } catch (Exception e) {
            logger.error("Error on get user by enrollment", e);
            return new Response<>(unexpected);
        }
    }

    @Override
    public Response<Pagination<User>> getPagination(int page, int count) {
        try {
            return new Response<>(
                    this.userRepository.getPagination(page, count)
            );
        } catch (Exception e) {
            logger.error("Error on get user pagination", e);
            return new Response<>(unexpected);
        }
    }

    @Override
    public Response<String> login(Login login) {
        if (login.getEnrollment() == 0) {
            return new Response<>(enrollmentIsEmpty);
        }
        if (login.getPassword().isEmpty()) {
            return new Response<>(passwordIsEmpty);
        }

        final var username = new UsernamePasswordAuthenticationToken(
                String.valueOf(login.getEnrollment()),
                login.getPassword()
        );

        final var auth = authenticationManager.authenticate(username);
        final var user = (SystemUser) auth.getPrincipal();

        final var token = tokenService.generate(user.getUserData());

        return new Response<>(token);
    }
}
