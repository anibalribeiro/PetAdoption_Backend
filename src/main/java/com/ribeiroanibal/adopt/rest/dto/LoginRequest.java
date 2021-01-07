package com.ribeiroanibal.adopt.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public final class LoginRequest {
    @NotBlank
    private final String username;

    @NotBlank
    private final String password;

    @JsonCreator
    public LoginRequest(@JsonProperty("username") final String username,
                        @JsonProperty("password") final String password) {
        this.username = username;
        this.password = password;
    }
}
