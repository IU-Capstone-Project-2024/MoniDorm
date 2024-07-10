package com.java.api.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.Nullable;
import java.time.OffsetDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PostFetchByDateRequest(
    @Nullable OffsetDateTime startDate,
    @Nullable OffsetDateTime endDate
) {
}
