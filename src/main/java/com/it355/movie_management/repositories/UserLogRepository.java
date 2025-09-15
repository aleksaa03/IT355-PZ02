package com.it355.movie_management.repositories;

import com.it355.movie_management.models.UserLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLogRepository extends JpaRepository<UserLog, Long> {
    @EntityGraph(attributePaths = "user")
    Page<UserLog> findAll(Pageable pageable);
}