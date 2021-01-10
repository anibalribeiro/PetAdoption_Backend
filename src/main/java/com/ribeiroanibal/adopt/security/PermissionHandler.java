package com.ribeiroanibal.adopt.security;

import com.ribeiroanibal.adopt.model.User;
import com.ribeiroanibal.adopt.service.UserService;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class PermissionHandler {
    private final UserService userService;

    public PermissionHandler(final UserService userService) {
        this.userService = userService;
    }

    public boolean canEditOrRemove(final Long userId) {
        return Objects.equals(userId, getLoggedInUser().getId());
    }

    public User getLoggedInUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final String username = Optional.ofNullable(auth)
                .map(Authentication::getPrincipal)
                .filter(authObj -> authObj instanceof UserDetailsImpl)
                .map(user -> ((UserDetailsImpl) user).getUsername())
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("Authentication Credentials not " +
                        "found"));
        return userService.getEntityByUsername(username);
    }
}
