package com.example.dto;

public record JwtResponseDto(
        String token,
        String type
) {
    public JwtResponseDto(String token) {
        this(token, "Bearer");
    }
}