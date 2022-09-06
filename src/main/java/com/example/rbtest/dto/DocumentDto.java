package com.example.rbtest.dto;

import com.example.rbtest.domain.DocumentType;
import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
public record DocumentDto(
        @NotNull LocalDateTime createdAt,
        @NotNull String createdBy,

        @NotNull String name,
        @NotNull DocumentType type
) {
}
