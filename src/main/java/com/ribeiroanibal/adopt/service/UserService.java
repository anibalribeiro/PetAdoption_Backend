package com.ribeiroanibal.adopt.service;

import com.ribeiroanibal.adopt.exception.EntityNotFoundException;
import com.ribeiroanibal.adopt.model.User;
import com.ribeiroanibal.adopt.repository.UserRepository;
import com.ribeiroanibal.adopt.rest.data.PageWrapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Page<User> getEntities(final Pageable pageable) {
        return new PageWrapper<>(userRepository.findAll(pageable), 20);
    }

    public User getEntity(final Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class.getSimpleName(), id));
    }

    public User getEntityByUsername(final String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(User.class.getSimpleName(), username));
    }

    public Optional<User> getOptionalEntityByUsername(final String username) {
        return userRepository.findByUsername(username);
    }

    public User addEntity(final User user) {
        return userRepository.save(user);
    }
}
