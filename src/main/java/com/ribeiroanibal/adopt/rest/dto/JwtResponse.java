package com.ribeiroanibal.adopt.rest.dto;

import lombok.Data;

@Data
public final class JwtResponse {
    private final String token;

    private final String type = "Bearer";

    private final Long id;

    private final String username;
}