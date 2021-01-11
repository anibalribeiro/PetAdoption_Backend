package com.ribeiroanibal.adopt.rest.dto;

import lombok.Data;

@Data
public final class UserResponseDto {
    private final Long id;
    private final String username;
    private final String phone;

    public UserResponseDto(final Long id,
                           final String username,
                           final String phone) {
        this.id = id;
        this.username = username;
        this.phone = phone;
    }
}
