package com.it355.movie_management.services;

import com.it355.movie_management.dtos.movie.MovieDto;
import com.it355.movie_management.dtos.movie.MovieWatchedDto;
import com.it355.movie_management.exceptions.BadRequestException;
import com.it355.movie_management.exceptions.NotFoundException;
import com.it355.movie_management.models.Movie;
import com.it355.movie_management.models.Watchlist;
import com.it355.movie_management.repositories.MovieRepository;
import com.it355.movie_management.repositories.UserRepository;
import com.it355.movie_management.repositories.WatchlistRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WatchlistService {
    private final MovieRepository movieRepository;
    private final WatchlistRepository  watchlistRepository;
    private final UserRepository userRepository;
    private final OmdbApiService omdbApiService;

    public WatchlistService(MovieRepository movieRepository, WatchlistRepository watchlistRepository, UserRepository userRepository, OmdbApiService omdbApiService) {
        this.movieRepository = movieRepository;
        this.watchlistRepository = watchlistRepository;
        this.userRepository = userRepository;
        this.omdbApiService = omdbApiService;
    }

    public void addToWatchlist(String imdbId, Long userId) {
        Movie movie = movieRepository.findByImdbId(imdbId).orElse(null);

        if (movie == null) {
            MovieDto result = omdbApiService.getMovieByImdbId(imdbId);

            movie = new Movie();
            movie.setTitle(result.getTitle());
            movie.setImg(result.getImg());
            movie.setImdbId(imdbId);
            movie.setType(result.getType());
            movie.setReleased(result.getReleased());
            movie.setImdbRating(result.getImdbRating());
            movie.setPlot(result.getPlot());
            movie.setActors(result.getActors());
            movie.setGenre(result.getGenre());

            movieRepository.save(movie);
        }

        boolean exists = watchlistRepository.existsByUserIdAndMovieId(userId, movie.getId());
        if (exists) {
            throw new BadRequestException("Movie is already in your watchlist.");
        }

        Watchlist watchlist = new Watchlist();
        watchlist.setUser(userRepository.getReferenceById(userId));
        watchlist.setMovie(movie);

        watchlistRepository.save(watchlist);
    }

    public List<MovieWatchedDto> getWatchlist(Long userId) {
        List<Watchlist> watchlist = watchlistRepository.findAllByUserIdWithMovie(userId);
        return watchlist.stream().map(item -> new MovieWatchedDto(item.getMovie(), item.isWatched())).toList();
    }

    public void updateWatchStatus(Long movieId, Long userId, Boolean watched) {
        Watchlist watchlistItem = watchlistRepository.findByUserIdAndMovieId(userId, movieId).orElseThrow(() -> new NotFoundException("Movie not found in your watchlist."));

        watchlistItem.setWatched(watched);
        watchlistRepository.save(watchlistItem);
    }

    public void removeFromWatchlist(Long movieId, Long userId) {
        Watchlist watchlistItem = watchlistRepository.findByUserIdAndMovieId(userId, movieId).orElseThrow(() -> new NotFoundException("Movie not found in your watchlist."));

        watchlistRepository.delete(watchlistItem);
    }
}
