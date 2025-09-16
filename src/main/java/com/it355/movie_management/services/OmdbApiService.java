package com.it355.movie_management.services;

import com.it355.movie_management.dtos.movie.MovieDto;
import com.it355.movie_management.infra.OmdbApiProvider;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OmdbApiService {
    private final OmdbApiProvider omdbApiProvider;

    public OmdbApiService(OmdbApiProvider omdbApiProvider) {
        this.omdbApiProvider = omdbApiProvider;
    }

    public Map<String, Object> searchMovies(String search, String type, int page) {
        return omdbApiProvider.searchMovies(search, type, page);
    }

    public MovieDto getMovieByImdbId(String imdbId) {
        return omdbApiProvider.getMovieByImdbId(imdbId);
    }
}