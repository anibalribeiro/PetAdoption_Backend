package com.ribeiroanibal.adopt.rest.controller;

import com.ribeiroanibal.adopt.AdoptApplication;
import com.ribeiroanibal.adopt.model.Pet;
import com.ribeiroanibal.adopt.rest.data.PageWrapper;
import com.ribeiroanibal.adopt.rest.dto.PetPostDto;
import com.ribeiroanibal.adopt.rest.dto.PetPutDto;
import com.ribeiroanibal.adopt.service.PetService;
import com.ribeiroanibal.adopt.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = AdoptApplication.API_PETS, produces = MediaType.APPLICATION_JSON_VALUE)
public class PetController {

    private final PetService petService;
    private final UserService userService;

    public PetController(final PetService petService, final UserService userService) {
        this.petService = petService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<PageWrapper<Pet>> getEntities(Pageable pageable) {
        return new ResponseEntity<>(petService.getEntities(pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pet> getEntity(@PathVariable Long id) {
        return new ResponseEntity<>(petService.getEntity(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Pet> addEntity(@RequestBody @Valid PetPostDto petPostDto) {
        final Pet pet = petPostDto.toPet();
        pet.setUser(userService.getEntity(petPostDto.getUserId()));
        return new ResponseEntity<>(petService.addEntity(pet), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pet> updateEntity(@PathVariable("id") final Long id,
                                            @RequestBody @Valid PetPutDto petPutDto) {
        final Pet petToUpdate = petService.getEntity(id);
        petToUpdate.setName(petPutDto.getName());
        petToUpdate.setAge(petPutDto.getAge());
        petToUpdate.setDescription(petPutDto.getDescription());
        petToUpdate.setPhoto(petPutDto.getPhoto());
        petToUpdate.setActive(petPutDto.getActive());
        petToUpdate.setVersion(petPutDto.getVersion());
        return new ResponseEntity<>(petService.updateEntity(petToUpdate), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEntity(@PathVariable("id") final Long id) {
        petService.deleteEntity(id);
        return ResponseEntity.ok().build();
    }
}
