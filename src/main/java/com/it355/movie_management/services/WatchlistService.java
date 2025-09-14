package com.it355.movie_management.services;

import com.it355.movie_management.common.config.AppConfig;
import com.it355.movie_management.dtos.movie.MovieWatchedDto;
import com.it355.movie_management.exceptions.BadRequestException;
import com.it355.movie_management.exceptions.NotFoundException;
import com.it355.movie_management.models.Movie;
import com.it355.movie_management.models.Watchlist;
import com.it355.movie_management.repositories.MovieRepository;
import com.it355.movie_management.repositories.UserRepository;
import com.it355.movie_management.repositories.WatchlistRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class WatchlistService {
    private final MovieRepository movieRepository;
    private final WatchlistRepository  watchlistRepository;
    private final UserRepository userRepository;
    private final AppConfig config;

    public WatchlistService(MovieRepository movieRepository, WatchlistRepository watchlistRepository, UserRepository userRepository, AppConfig config) {
        this.movieRepository = movieRepository;
        this.watchlistRepository = watchlistRepository;
        this.userRepository = userRepository;
        this.config = config;
    }

    public void addToWatchlist(String imdbId, Long userId) {
        Movie movie = movieRepository.findByImdbId(imdbId).orElse(null);

        if (movie == null) {
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://www.omdbapi.com/?i=" + imdbId + "&apikey=" + config.getApiKey();
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            Map<String, Object> content = response.getBody();
            if (content == null || "False".equals(content.get("Response"))) {
                throw new BadRequestException("Movie not found: " + imdbId);
            }

            String releasedStr = (String) content.get("Released");
            Date released = null;
            if (releasedStr != null && !releasedStr.equalsIgnoreCase("N/A")) {
                try {
                    released = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).parse(releasedStr);
                } catch (ParseException e) {
                    throw new BadRequestException("Invalid date format for released date.");
                }
            }

            String imdbRatingStr = (String) content.get("imdbRating");
            BigDecimal imdbRating = null;
            try {
                imdbRating = new BigDecimal(imdbRatingStr);
            } catch (Exception ignored) {}

            movie = new Movie();
            movie.setTitle((String) content.get("Title"));
            movie.setImg((String) content.get("Poster"));
            movie.setImdbId(imdbId);
            movie.setType((String) content.get("Type"));
            movie.setReleased(LocalDate.ofInstant(released.toInstant(), ZoneId.systemDefault()));
            movie.setImdbRating(imdbRating);
            movie.setPlot((String) content.get("Plot"));
            movie.setActors((String) content.get("Actors"));
            movie.setGenre((String) content.get("Genre"));

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
        return watchlist.stream().map(item -> new MovieWatchedDto(item.getMovie())).toList();
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
