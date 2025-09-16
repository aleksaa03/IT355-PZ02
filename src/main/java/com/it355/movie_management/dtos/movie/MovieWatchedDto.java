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
                    String genre,
                    boolean watched) {
        super(id, title, img, imdbId, type, released, imdbRating, plot, actors, genre);
        this.watched = watched;
    }

    public MovieWatchedDto(Movie movie) {
        super(movie);
    }

    public MovieWatchedDto(Movie movie, boolean watched) {
        super(movie);
        this.watched = watched;
    }

    public boolean isWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }
}