package com.java.api.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.java.domain.model.Failure;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GetFailureResponse(
    @Min(0) long id,
    @NotBlank String category,
    @NotBlank String placement,
    @NotNull OffsetDateTime failureDate,
    @Min(0) long reportCount,
    @NotBlank String summarization
) {
    public GetFailureResponse(@NotNull Failure failure) {
        this(failure.getId(), failure.getCategory(), failure.getPlacement(), failure.getFailureDate(),
            failure.getReportCount(), failure.getSummarization()
        );
    }
}
