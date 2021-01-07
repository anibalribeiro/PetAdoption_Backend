package com.ribeiroanibal.adopt.repository;

import com.ribeiroanibal.adopt.model.User;
import com.ribeiroanibal.adopt.repository.base.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
