package com.java.api.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GetReportResponse(
    @Min(0) long id,
    @NotBlank String category,
    @NotBlank String placement,
    String ownerEmail,
    @NotNull OffsetDateTime failureDate,
    @NotNull OffsetDateTime lastSuccessfullyProceededDate,
    boolean isResolved,
    boolean isConfirmed,
    String description
) {
}
