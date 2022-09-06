package com.example.rbtest.dto;

import com.example.rbtest.domain.ProtocolState;
import lombok.Builder;

import javax.validation.constraints.NotNull;

@Builder
public record ProtocolStateDto(@NotNull ProtocolState state) {
}
