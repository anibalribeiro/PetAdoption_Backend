package com.ribeiroanibal.adopt.service;

import com.ribeiroanibal.adopt.exception.EntityNotFoundException;
import com.ribeiroanibal.adopt.model.Pet;
import com.ribeiroanibal.adopt.repository.PetRepository;
import com.ribeiroanibal.adopt.rest.data.PageWrapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PetService {

    private final PetRepository petRepository;

    public PetService(final PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public PageWrapper<Pet> getEntities(final Pageable pageable) {
        return new PageWrapper<>(petRepository.findByActiveTrue(pageable), 20);
    }

    public Pet getEntity(final Long id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Pet.class.getSimpleName(), id));
    }

    public Pet addEntity(final Pet pet) {
        return petRepository.save(pet);
    }

    public Pet updateEntity(final Pet pet) {
        return petRepository.save(pet);
    }

    public void deleteEntity(final Long id) {
        petRepository.delete(getEntity(id));
    }

}
