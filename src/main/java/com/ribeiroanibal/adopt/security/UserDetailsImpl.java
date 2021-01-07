package com.ribeiroanibal.adopt.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ribeiroanibal.adopt.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class UserDetailsImpl implements UserDetails {

    private final Long id;

    private final String username;

    @JsonIgnore
    private final String password;

    public UserDetailsImpl(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public static UserDetailsImpl build(User user) {
        return new UserDetailsImpl(user.getId(), user.getUsername(), user.getPassword());
    }

    public Long getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetailsImpl that = (UserDetailsImpl) o;
        return Objects.equals(id, that.id) && Objects.equals(username, that.username) && Objects.equals(password,
                that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password);
    }
}
