package com.it355.movie_management.services;

import com.it355.movie_management.common.config.AppConfig;
import com.it355.movie_management.dtos.UserPayload;
import com.it355.movie_management.dtos.comment.CommentResponseDto;
import com.it355.movie_management.dtos.movie.MovieCreateRequestDto;
import com.it355.movie_management.dtos.movie.MovieDto;
import com.it355.movie_management.dtos.movie.MovieWatchedDto;
import com.it355.movie_management.exceptions.*;
import com.it355.movie_management.models.Comment;
import com.it355.movie_management.models.Movie;
import com.it355.movie_management.repositories.CommentRepository;
import com.it355.movie_management.repositories.MovieRepository;
import com.it355.movie_management.repositories.UserRepository;
import com.it355.movie_management.repositories.WatchlistRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final WatchlistRepository watchlistRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AppConfig config;

    public MovieService(MovieRepository movieRepository, WatchlistRepository watchlistRepository, CommentRepository commentRepository, UserRepository userRepository, AppConfig config) {
        this.movieRepository = movieRepository;
        this.watchlistRepository = watchlistRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.config = config;
    }

    public MovieDto createMovie(MovieCreateRequestDto model) {
        Optional<Movie> existing = movieRepository.findByImdbId(model.getImdbId());

        if (existing.isPresent()) {
            throw new ConflictException("Movie already exists.");
        }

        Movie movie = new Movie();
        movie.setTitle(model.getTitle());
        movie.setImg(model.getImg());
        movie.setImdbId(model.getImdbId());
        movie.setType(model.getType());
        movie.setReleased(model.getReleased() != null ? model.getReleased() : null);
        movie.setImdbRating(model.getImdbRating());
        movie.setPlot(model.getPlot());
        movie.setActors(model.getActors());
        movie.setGenre(model.getGenre());

        Movie savedMovie = movieRepository.save(movie);

        return new MovieDto(savedMovie);
    }

    public MovieWatchedDto getMovie(Long movieId, UserPayload currentUser) {
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new NotFoundException("Movie not found."));

        MovieWatchedDto movieWatchedDto = new MovieWatchedDto(movie);
        movieWatchedDto.setWatched(watchlistRepository.existsByUserIdAndMovieIdAndWatchedTrue(currentUser.id(), movieId));

        return movieWatchedDto;
    }

    public MovieDto getOrCreateMovieByImdbId(String imdbId) {
        Optional<Movie> movie = movieRepository.findByImdbId(imdbId);

        if (movie.isPresent()) {
            return new MovieDto(movie.get());
        }

        String url = "http://www.omdbapi.com/?i=" + imdbId + "&apikey=" + config.getApiKey();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new BadRequestException("Failed to fetch from OMDB.");
        }

        Map<String, Object> content = response.getBody();
        if ("False".equals(content.get("Response"))) {
            throw new BadRequestException((String) content.get("Error"));
        }

        String releasedStr = (String) content.get("Released");
        LocalDate released = null;
        if (releasedStr != null && !releasedStr.equalsIgnoreCase("N/A")) {
            try {
                released = LocalDate.parse(releasedStr, DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH));
            } catch (DateTimeParseException e) {
                throw new BadRequestException("Invalid date format for released date.");
            }
        }

        Movie newMovie = new Movie();
        newMovie.setTitle((String) content.get("Title"));
        newMovie.setImg((String) content.get("Poster"));
        newMovie.setImdbId(imdbId);
        newMovie.setType((String) content.get("Type"));
        newMovie.setReleased(released);
        String imdbRatingStr = (String) content.get("imdbRating");
        if (imdbRatingStr != null && !imdbRatingStr.equalsIgnoreCase("N/A")) {
            newMovie.setImdbRating(new BigDecimal(imdbRatingStr));
        } else {
            newMovie.setImdbRating(null);
        }
        newMovie.setPlot((String) content.get("Plot"));
        newMovie.setActors((String) content.get("Actors"));
        newMovie.setGenre((String) content.get("Genre"));

        Movie savedMovie = movieRepository.save(newMovie);

        return new MovieDto(savedMovie);
    }

    public void addComment(Long userId, Long movieId, String commentText) {
        if (!movieRepository.existsById(movieId)) {
            throw new NotFoundException("Movie not found.");
        }

        Comment comment = new Comment();
        comment.setMovie(movieRepository.getReferenceById(movieId));
        comment.setUser(userRepository.getReferenceById(userId));
        comment.setComment(commentText);

        commentRepository.save(comment);
    }

    public List<CommentResponseDto> getCommentsByMovie(Long movieId) {
        if (!movieRepository.existsById(movieId)) {
            throw new NotFoundException("Movie not found.");
        }

        List<Comment> comments = commentRepository.findByMovieIdOrderByCreatedAtDesc(movieId);

        return comments.stream()
                .map(c -> new CommentResponseDto(
                        c.getId(),
                        c.getComment(),
                        c.getCreatedAt(),
                        c.getUser().getId(),
                        c.getUser().getUsername()
                ))
                .toList();
    }

    public void deleteComment(Long movieId, Long commentId, Long currentUserId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found."));

        if (!comment.getUser().getId().equals(currentUserId)) {
            throw new ForbiddenException("You are not authorized to delete this comment.");
        }

        commentRepository.delete(comment);
    }
}