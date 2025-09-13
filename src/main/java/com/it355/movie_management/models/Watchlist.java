package com.it355.movie_management.models;

import com.it355.movie_management.models.abstraction.Base;
import jakarta.persistence.*;

@Entity
@Table(name = "watchlist")
public class Watchlist extends Base {
    @Column(name = "watched", nullable = false)
    private boolean watched = false;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_watchlist_user"))
    private User user;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false, foreignKey = @ForeignKey(name = "fk_watchlist_movie"))
    private Movie movie;

    public boolean isWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}