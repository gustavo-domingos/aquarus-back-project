package com.gustavoalves.complementary_activities.infra.user.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepositoryJPA extends JpaRepository<UserJPA, UUID> {
    UserJPA findByEnrollment(long enrollment);
    Page<UserJPA> findAll(Pageable pageable);
}
