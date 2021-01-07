package com.ribeiroanibal.adopt.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ribeiroanibal.adopt.model.User;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public final class UserPostDto {
    @NotBlank
    private final String username;

    @NotBlank
    private final String password;

    @NotBlank
    private final String phone;

    @JsonCreator
    public UserPostDto(@JsonProperty("username") final String username,
                       @JsonProperty("password") final String password,
                       @JsonProperty("phone") final String phone) {
        this.username = username;
        this.password = password;
        this.phone = phone;
    }

    public User toUser() {
        return new User(username, password, phone);
    }
}
