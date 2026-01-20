package com.gustavoalves.complementary_activities.infra.user;

import com.gustavoalves.complementary_activities.domain.user.User;
import com.gustavoalves.complementary_activities.domain.user.UserRepository;
import com.gustavoalves.complementary_activities.infra.user.jpa.UserJPA;
import com.gustavoalves.complementary_activities.infra.user.jpa.UserRepositoryJPA;
import com.gustavoalves.complementary_activities.utils.entities.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserRepositoryImpl implements UserRepository {
    @Autowired
    private UserRepositoryJPA userRepositoryJPA;

    @Override
    public User create(User user) {
        final var userJPA = convertUserToJPA(user);

        var result = userRepositoryJPA.save(userJPA);

        return convertJPAToUser(result);
    }

    @Override
    public User update(User user) {
        final var userJPA = convertUserToJPA(user);
        userJPA.setUuid(user.getUuid());

        var result = userRepositoryJPA.save(userJPA);

        return convertJPAToUser(result);
    }

    @Override
    public User getByUUID(UUID uuid) {
        var result = userRepositoryJPA.getReferenceById(uuid);

        return convertJPAToUser(result);
    }

    @Override
    public User getByEnrollment(long enrollment) {
        var result = userRepositoryJPA.findByEnrollment(enrollment);

        if (result == null) {
            return null;
        }

        final var user = convertJPAToUser(result);

        user.setPassword(result.getPassword());

        return user;
    }

    @Override
    public Pagination<User> getPagination(int page, int count) {
        Pageable pageable = PageRequest.of(page, count, Sort.by("name").ascending());

        Page<UserJPA> resultPage = userRepositoryJPA.findAll(pageable);

        List<User> users = resultPage.getContent()
                .stream()
                .map(this::convertJPAToUser)
                .collect(Collectors.toList());

        return new Pagination<>(
                users,
                (int) resultPage.getTotalElements(),
                page
        );
    }

    private UserJPA convertUserToJPA(User user) {
        final UserJPA newUser = new UserJPA();

        newUser.setDocument(user.getDocument());
        newUser.setEmail(user.getEmail());
        newUser.setName(user.getName());
        newUser.setPassword(user.getPassword());
        newUser.setEnrollment(user.getEnrollment());
        newUser.setPhone(user.getPhone());
        newUser.setType(user.getType());

        return newUser;
    }

    private User convertJPAToUser(UserJPA user) {
        final User newUser = new User();

        newUser.setUuid(user.getUuid());
        newUser.setDocument(user.getDocument());
        newUser.setEmail(user.getEmail());
        newUser.setName(user.getName());
        newUser.setEnrollment(user.getEnrollment());
        newUser.setPhone(user.getPhone());
        newUser.setType(user.getType());

        return newUser;
    }
}
