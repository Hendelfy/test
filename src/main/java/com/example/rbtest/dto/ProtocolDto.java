package com.example.rbtest.dto;

import com.example.rbtest.domain.ProtocolState;
import lombok.Builder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ProtocolDto(
        @NotNull LocalDateTime createdAt,
        @NotNull String createdBy,
        @NotNull ProtocolState state,
        @NotEmpty List<Integer> documentIds
) {
}
