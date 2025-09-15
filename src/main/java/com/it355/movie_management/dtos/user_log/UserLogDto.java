package com.it355.movie_management.dtos.user_log;

import com.it355.movie_management.common.enums.UserLogType;
import com.it355.movie_management.dtos.user.UserDto;

import java.time.OffsetDateTime;

public class UserLogDto {
    private Long id;
    private UserLogType action;
    private String description;
    private String details;
    private OffsetDateTime eventTime;
    private UserDto user;

    public UserLogDto() {}

    public UserLogDto(Long id, UserLogType action, String description, String details, OffsetDateTime eventTime, UserDto user) {
        this.id = id;
        this.action = action;
        this.description = description;
        this.details = details;
        this.eventTime = eventTime;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }
}