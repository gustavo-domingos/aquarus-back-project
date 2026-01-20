package com.gustavoalves.complementary_activities.domain.user;

import com.gustavoalves.complementary_activities.utils.entities.Pagination;

import java.util.UUID;

public interface UserRepository {
    User create(User user);
    User update(User user);
    User getByUUID(UUID uuid);
    User getByEnrollment(long enrollment);
    Pagination<User> getPagination(int page, int count);
}
