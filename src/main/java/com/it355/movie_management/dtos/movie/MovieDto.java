package com.it355.movie_management.dtos.movie;

import com.it355.movie_management.models.Movie;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MovieDto {
    private Long id;
    private String title;
    private String img;
    private String imdbId;
    private String type;
    private LocalDate released;
    private BigDecimal imdbRating;
    private String plot;
    private String actors;
    private String genre;

    public MovieDto() {}

    public MovieDto(Long id,
                    String title,
                    String img,
                    String imdbId,
                    String type,
                    LocalDate released,
                    BigDecimal imdbRating,
                    String plot,
                    String actors,
                    String genre) {
        this.id = id;
        this.setTitle(title);
        this.setImg(img);
        this.setImdbId(imdbId);
        this.setType(type);
        this.setReleased(released);
        this.setImdbRating(imdbRating);
        this.setPlot(plot);
        this.setActors(actors);
        this.setGenre(genre);
    }

    public MovieDto(Movie movie) {
        this.id = movie.getId();
        this.setTitle(movie.getTitle());
        this.setImg(movie.getImg());
        this.setImdbId(movie.getImdbId());
        this.setType(movie.getType());
        this.setReleased(movie.getReleased());
        this.setImdbRating(movie.getImdbRating());
        this.setPlot(movie.getPlot());
        this.setActors(movie.getActors());
        this.setGenre(movie.getGenre());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getReleased() {
        return released;
    }

    public void setReleased(LocalDate released) {
        this.released = released;
    }

    public BigDecimal getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(BigDecimal imdbRating) {
        this.imdbRating = imdbRating;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}