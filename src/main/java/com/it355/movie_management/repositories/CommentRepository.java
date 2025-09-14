package com.it355.movie_management.repositories;

import com.it355.movie_management.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByMovieIdOrderByCreatedAtDesc(Long movieId);
}