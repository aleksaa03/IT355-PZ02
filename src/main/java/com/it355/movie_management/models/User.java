package com.it355.movie_management.models;

import com.it355.movie_management.common.enums.UserRole;
import com.it355.movie_management.models.abstraction.Base;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users")
public class User extends Base {
    @Column(name = "username", nullable = false, length = 100, unique = true)
    private String username;

    @Column(name = "password", nullable = false, length = 256)
    private String password;

    @Column(name = "role_id", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private UserRole roleId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Watchlist> watchlist;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserLog> userLogs;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRoleId() {
        return roleId;
    }

    public void setRoleId(UserRole roleId) {
        this.roleId = roleId;
    }

    public List<Watchlist> getWatchlist() {
        return watchlist;
    }

    public void setWatchlist(List<Watchlist> watchlist) {
        this.watchlist = watchlist;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<UserLog> getUserLogs() {
        return userLogs;
    }

    public void setUserLogs(List<UserLog> userLogs) {
        this.userLogs = userLogs;
    }
}