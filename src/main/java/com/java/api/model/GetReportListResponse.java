package com.java.api.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GetReportListResponse(
    @NotNull List<GetReportResponse> responses,
    @Min(0) long size
) {
    public GetReportListResponse(List<GetReportResponse> responses) {
        this(responses, responses.size());
    }
}
