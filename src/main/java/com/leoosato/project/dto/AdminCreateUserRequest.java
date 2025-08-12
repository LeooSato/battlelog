package com.leoosato.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AdminCreateUserRequest(
        @NotBlank @Size(min=3,max=50) String username,
        @NotBlank @Size(min=6,max=100) String password,
        @NotBlank String role // "USER" ou "ADMIN"
) {}
