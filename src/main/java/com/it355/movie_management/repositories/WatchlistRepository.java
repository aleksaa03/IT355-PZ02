package com.it355.movie_management.repositories;

import com.it355.movie_management.models.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {
    boolean existsByUserIdAndMovieIdAndWatchedTrue(Long userId, Long movieId);
    boolean existsByUserIdAndMovieId(Long userId, Long movieId);
    @Query("SELECT w FROM Watchlist w JOIN FETCH w.movie WHERE w.user.id = :userId")
    List<Watchlist> findAllByUserIdWithMovie(@Param("userId") Long userId);
    Optional<Watchlist> findByUserIdAndMovieId(Long userId, Long movieId);
}