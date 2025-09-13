package com.it355.movie_management.models;

import com.it355.movie_management.models.abstraction.Base;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "movies")
public class Movie extends Base {
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "img")
    private String img;

    @Column(name = "imdb_id", length = 20, unique = true, nullable = false)
    private String imdbId;

    @Column(name = "type")
    private String type;

    @Column(name = "released")
    private LocalDate released;

    @Column(name = "imdb_rating", precision = 3, scale = 1)
    private BigDecimal imdbRating;

    @Column(name = "plot")
    private String plot;

    @Column(name = "actors")
    private String actors;

    @Column(name = "genre")
    private String genre;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Watchlist> watchlistedBy;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
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

    public List<Watchlist> getWatchlistedBy() {
        return watchlistedBy;
    }

    public void setWatchlistedBy(List<Watchlist> watchlistedBy) {
        this.watchlistedBy = watchlistedBy;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}