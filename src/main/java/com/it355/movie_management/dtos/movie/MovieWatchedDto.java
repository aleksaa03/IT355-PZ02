package com.it355.movie_management.dtos.movie;

import com.it355.movie_management.models.Movie;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MovieWatchedDto extends MovieDto {
    private boolean watched;

    public MovieWatchedDto() {}

    public MovieWatchedDto(Long id,
                    String title,
                    String img,
                    String imdbId,
                    String type,
                    LocalDate released,
                    BigDecimal imdbRating,
                    String plot,
                    String actors,
                    String genre) {
        super(id, title, img, imdbId, type, released, imdbRating, plot, actors, genre);
    }

    public MovieWatchedDto(Movie movie) {
        super(movie);
    }

    public boolean isWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }
}