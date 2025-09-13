package com.it355.movie_management.models;

import com.it355.movie_management.models.abstraction.Base;
import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "comments")
public class Comment extends Base {
    @Column(name = "comment", columnDefinition = "text", nullable = false)
    private String comment;

    @Column(name = "created_at", columnDefinition = "timestamptz", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_comment_user"))
    private User user;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false, foreignKey = @ForeignKey(name = "fk_comment_movie"))
    private Movie movie;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
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