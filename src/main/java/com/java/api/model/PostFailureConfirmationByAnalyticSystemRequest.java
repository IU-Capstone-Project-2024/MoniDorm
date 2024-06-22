package com.java.api.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PostFailureConfirmationByAnalyticSystemRequest(
    @NotBlank String placement,
    @NotBlank String category,
    @NotNull OffsetDateTime firstProceededFailureDatetime
) {
}
