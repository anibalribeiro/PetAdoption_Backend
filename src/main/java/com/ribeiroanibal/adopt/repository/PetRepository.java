package com.ribeiroanibal.adopt.repository;

import com.ribeiroanibal.adopt.model.Pet;
import com.ribeiroanibal.adopt.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface PetRepository extends BaseRepository<Pet, Long> {
    Page<Pet> findByActiveTrue(Pageable pageable);
}
