package com.it355.movie_management.controllers;

import com.it355.movie_management.dtos.UserPayload;
import com.it355.movie_management.dtos.movie.MovieWatchedDto;
import com.it355.movie_management.exceptions.BadRequestException;
import com.it355.movie_management.services.WatchlistService;
import com.it355.movie_management.utils.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/watch-list")
public class WatchlistController {
    private final WatchlistService watchlistService;

    public WatchlistController(WatchlistService watchlistService) {
        this.watchlistService = watchlistService;
    }

    @PostMapping()
    public ResponseEntity<?> addToWatchlist(@RequestBody Map<String, String> body, HttpServletRequest request) {
        String imdbId = body.get("imdbId");

        if (StringUtil.isNullOrEmpty(imdbId)) {
            throw new BadRequestException("IMDB ID cannot be empty.");
        }

        UserPayload currentUser = (UserPayload) request.getAttribute("currentUser");

        watchlistService.addToWatchlist(imdbId, currentUser.id());

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Movie added to your watchlist."));
    }

    @GetMapping()
    public ResponseEntity<?> getWatchlist(HttpServletRequest request) {
        UserPayload currentUser = (UserPayload) request.getAttribute("currentUser");
        List<MovieWatchedDto> watchlist = watchlistService.getWatchlist(currentUser.id());

        return ResponseEntity.ok().body(Map.of("movies", watchlist));
    }

    @PatchMapping("/{movieId}")
    public ResponseEntity<?> updateWatchStatus(@PathVariable Long movieId,
                                               @RequestBody Map<String, Object> body,
                                               HttpServletRequest request) {
        if (movieId == null) {
            throw new BadRequestException("Movie id must be number.");
        }

        Boolean watched = (Boolean) body.get("watched");

        if (watched == null) {
            throw new BadRequestException("Watched status is required.");
        }

        UserPayload currentUser = (UserPayload) request.getAttribute("currentUser");

        watchlistService.updateWatchStatus(movieId, currentUser.id(), watched);

        return ResponseEntity.ok().body(Map.of("message", "Watch status updated successfully."));
    }

    @DeleteMapping("/{movieId}")
    public ResponseEntity<?> removeFromWatchlist(@PathVariable Long movieId, HttpServletRequest request) {
        if (movieId == null) {
            throw new BadRequestException("Movie id must be number.");
        }

        UserPayload currentUser = (UserPayload) request.getAttribute("currentUser");

        watchlistService.removeFromWatchlist(movieId, currentUser.id());

        return ResponseEntity.ok(Map.of("message", "Movie removed from your watchlist."));
    }
}