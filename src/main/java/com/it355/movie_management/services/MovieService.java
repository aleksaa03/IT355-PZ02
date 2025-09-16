package com.it355.movie_management.services;

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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final WatchlistRepository watchlistRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final OmdbApiService omdbApiService;

    public MovieService(MovieRepository movieRepository, WatchlistRepository watchlistRepository, CommentRepository commentRepository, UserRepository userRepository, OmdbApiService omdbApiService) {
        this.movieRepository = movieRepository;
        this.watchlistRepository = watchlistRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.omdbApiService = omdbApiService;
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

        MovieDto result = omdbApiService.getMovieByImdbId(imdbId);

        Movie newMovie = new Movie();
        newMovie.setTitle(result.getTitle());
        newMovie.setImg(result.getImg());
        newMovie.setImdbId(imdbId);
        newMovie.setType(result.getType());
        newMovie.setReleased(result.getReleased());
        newMovie.setImdbRating(result.getImdbRating());
        newMovie.setPlot(result.getPlot());
        newMovie.setActors(result.getActors());
        newMovie.setGenre(result.getGenre());

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