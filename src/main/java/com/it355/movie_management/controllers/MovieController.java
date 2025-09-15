package com.it355.movie_management.controllers;

import com.it355.movie_management.common.config.AppConfig;
import com.it355.movie_management.common.enums.UserLogType;
import com.it355.movie_management.dtos.comment.CommentResponseDto;
import com.it355.movie_management.dtos.movie.MovieCreateRequestDto;
import com.it355.movie_management.dtos.movie.MovieDto;
import com.it355.movie_management.dtos.movie.MovieWatchedDto;
import com.it355.movie_management.exceptions.BadRequestException;
import com.it355.movie_management.services.MovieService;
import com.it355.movie_management.utils.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/movies")
public class MovieController extends PortalController {
    private final MovieService movieService;
    private final AppConfig config;

    public MovieController(MovieService movieService, AppConfig config) {
        this.movieService = movieService;
        this.config = config;
    }

    @PostMapping()
    public ResponseEntity<?> createMovie(@RequestBody MovieCreateRequestDto model, HttpServletRequest request) {
        MovieDto movie =  movieService.createMovie(model);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("movie", movie));
    }

    @GetMapping()
    public ResponseEntity<?> searchMovies(@RequestParam(name = "s") String search,
                                          @RequestParam(name = "p", required = false, defaultValue = "1") int page,
                                          @RequestParam(name = "type", required = false) String type
    ) {
        if (search.length() < 3) {
            throw new BadRequestException("Search query must be at least 3 char long.");
        }

        String query = String.format("apikey=%s&s=%s&page=%d", config.getApiKey(), search, page);
        if (type != null && !type.isEmpty()) {
            query += "&type=" + type;
        }

        String url = "http://www.omdbapi.com/?" + query;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new BadRequestException("Error fetching data from OMDB API.");
        }

        Map<String, Object> content = response.getBody();
        if ("True".equals(content.get("Response"))) {
            Map<String, Object> result = Map.of(
                    "movies", content.get("Search"),
                    "totalResults", Integer.parseInt(content.get("totalResults").toString())
            );
            return ResponseEntity.ok(result);
        } else if ("False".equals(content.get("Response"))) {
            throw new BadRequestException(content.get("Error").toString());
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Internal server error"));
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<?> getMovie(@PathVariable Long movieId) {
        if (movieId == null) {
            throw new BadRequestException("Movie id must be number.");
        }

        MovieWatchedDto movie = movieService.getMovie(movieId, this.currentUser());

        return ResponseEntity.ok().body(Map.of("movie", movie));
    }

    @GetMapping("/imdb/{imdbId}")
    public ResponseEntity<?> getMovieByImdb(@PathVariable String imdbId) {
        if (StringUtil.isNullOrEmpty(imdbId)) {
            throw new BadRequestException("IMDB ID cannot be empty.");
        }

        MovieDto movie = movieService.getOrCreateMovieByImdbId(imdbId);

        return ResponseEntity.ok(Map.of("movieId", movie.getId()));
    }

    @PostMapping("/{movieId}/comments")
    public ResponseEntity<?> addComment(@PathVariable Long movieId, @RequestBody Map<String, String> body) {
        if (movieId == null) {
            throw new BadRequestException("Movie id must be number.");
        }

        String comment = body.get("comment");

        if (StringUtil.isNullOrEmpty(comment)) {
            throw new BadRequestException("Comment cannot be empty.");
        }

        movieService.addComment(this.currentUser().id(), movieId, comment);

        this.addUserLog(UserLogType.Add,
                String.format("Added comment to movie with ID %s", movieId),
                String.format("Comment: %s", comment));

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Comment added successfully."));
    }

    @GetMapping("/{movieId}/comments")
    public ResponseEntity<?> getMovieComments(@PathVariable Long movieId) {
        if (movieId == null) {
            throw new BadRequestException("Movie id must be number.");
        }

        List<CommentResponseDto> comments = movieService.getCommentsByMovie(movieId);

        return ResponseEntity.ok().body(Map.of("comments", comments));
    }

    @DeleteMapping("/{movieId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long movieId, @PathVariable Long commentId) {
        if (movieId == null || commentId == null) {
            throw new BadRequestException("Movie id and comment id must be numbers.");
        }

        movieService.deleteComment(movieId, commentId, this.currentUser().id());

        this.addUserLog(UserLogType.Delete, String.format("Deleted comment from movie with ID %s", movieId));

        return ResponseEntity.ok().body(Map.of("message", "Comment was removed."));
    }
}