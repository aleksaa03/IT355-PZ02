package com.it355.movie_management.models;

import com.it355.movie_management.common.enums.UserLogType;
import com.it355.movie_management.models.abstraction.Base;
import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "user_logs")
public class UserLog extends Base {
    @Column(name = "action", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private UserLogType action;

    @Column(name = "description")
    private String description;

    @Column(name = "details")
    private String details;

    @Column(name = "event_time", columnDefinition = "timestamptz", nullable = false)
    private OffsetDateTime eventTime = OffsetDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_userlog_user"))
    private User user;

    public UserLogType getAction() {
        return action;
    }

    public void setAction(UserLogType action) {
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public OffsetDateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(OffsetDateTime eventTime) {
        this.eventTime = eventTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}