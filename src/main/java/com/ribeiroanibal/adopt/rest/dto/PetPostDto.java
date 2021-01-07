package com.ribeiroanibal.adopt.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ribeiroanibal.adopt.model.Pet;
import com.ribeiroanibal.adopt.model.enums.PetCategoryEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public final class PetPostDto {

    @NotBlank
    private final String name;

    private final Float age;

    private final PetCategoryEnum category;

    @NotBlank
    private final String description;

    @NotNull
    private final Long userId;

    @NotNull
    private final String photo;

    @JsonCreator
    public PetPostDto(@JsonProperty("name") final String name,
                      @JsonProperty("age") final Float age,
                      @JsonProperty("category") final PetCategoryEnum category,
                      @JsonProperty("description") final String description,
                      @JsonProperty("userId") final Long userId,
                      @JsonProperty("photo") final String photo) {
        this.name = name;
        this.age = age;
        this.category = category;
        this.description = description;
        this.userId = userId;
        this.photo = photo;
    }

    public Pet toPet() {
        return new Pet(name, age, category, description, null, photo, true);
    }
}
