package com.gustavoalves.complementary_activities.domain.user;

import com.gustavoalves.complementary_activities.utils.entities.Pagination;
import com.gustavoalves.complementary_activities.utils.entities.Response;

import java.util.UUID;

public interface UserUseCase {
    Response<User> create(User user);
    Response<User> update(User user);
    Response<User> getByUUID(UUID uuid);
    Response<User> getByEnrollment(long enrollment);
    Response<Pagination<User>> getPagination(int page, int count);
    Response<String> login(Login login);
}
