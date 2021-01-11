package com.ribeiroanibal.adopt.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ribeiroanibal.adopt.model.enums.PetCategoryEnum;
import com.ribeiroanibal.adopt.rest.dto.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "pets")
public class Pet extends BaseEntity {
    @NotBlank
    private String name;

    private Float age;

    private PetCategoryEnum category;

    @NotBlank
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    @NotNull
    private String photo;

    private Boolean active;

    @JsonProperty("user")
    public UserResponseDto getUser() {
        return new UserResponseDto(
                Optional.ofNullable(user).map(BaseEntity::getId).orElse(null),
                Optional.ofNullable(user).map(User::getUsername).orElse(null),
                Optional.ofNullable(user).map(User::getPhone).orElse(null)
        );
    }
}
