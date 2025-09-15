package com.it355.movie_management.controllers;

import com.it355.movie_management.common.enums.UserLogType;
import com.it355.movie_management.dtos.movie.MovieWatchedDto;
import com.it355.movie_management.exceptions.BadRequestException;
import com.it355.movie_management.services.WatchlistService;
import com.it355.movie_management.utils.StringUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/watch-list")
public class WatchlistController extends PortalController {
    private final WatchlistService watchlistService;

    public WatchlistController(WatchlistService watchlistService) {
        this.watchlistService = watchlistService;
    }

    @PostMapping()
    public ResponseEntity<?> addToWatchlist(@RequestBody Map<String, String> body) {
        String imdbId = body.get("imdbId");

        if (StringUtil.isNullOrEmpty(imdbId)) {
            throw new BadRequestException("IMDB ID cannot be empty.");
        }

        watchlistService.addToWatchlist(imdbId, this.currentUser().id());

        this.addUserLog(UserLogType.Add, String.format("Added movie to watchlist with IMDB ID %s", imdbId));

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Movie added to your watchlist."));
    }

    @GetMapping()
    public ResponseEntity<?> getWatchlist() {
        List<MovieWatchedDto> watchlist = watchlistService.getWatchlist(this.currentUser().id());
        return ResponseEntity.ok().body(Map.of("movies", watchlist));
    }

    @PatchMapping("/{movieId}")
    public ResponseEntity<?> updateWatchStatus(@PathVariable Long movieId, @RequestBody Map<String, Object> body) {
        if (movieId == null) {
            throw new BadRequestException("Movie id must be number.");
        }

        Boolean watched = (Boolean) body.get("watched");

        if (watched == null) {
            throw new BadRequestException("Watched status is required.");
        }

        watchlistService.updateWatchStatus(movieId, this.currentUser().id(), watched);

        this.addUserLog(UserLogType.Update, String.format("Changed watch status for movie with ID %s", movieId));

        return ResponseEntity.ok().body(Map.of("message", "Watch status updated successfully."));
    }

    @DeleteMapping("/{movieId}")
    public ResponseEntity<?> removeFromWatchlist(@PathVariable Long movieId) {
        if (movieId == null) {
            throw new BadRequestException("Movie id must be number.");
        }

        watchlistService.removeFromWatchlist(movieId, this.currentUser().id());

        this.addUserLog(UserLogType.Delete,
                String.format("Removed movie from watchlist with ID %s", movieId),
                String.format("Movie ID: %s", movieId));

        return ResponseEntity.ok(Map.of("message", "Movie removed from your watchlist."));
    }
}