package com.java.api;

import com.java.api.model.PostFailureConfirmationByAnalyticSystemRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Validated
@RequestMapping(("/api/analyst"))
public interface AnalystSystemAPI {
    @Operation(summary = "Resolve report with certain id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "Analytic successfully received",
                     content = @Content()),

        @ApiResponse(responseCode = "400",
                     description = "Request was malformed",
                     content = @Content()),

        @ApiResponse(responseCode = "403",
                     description = "Incorrect access token provided",
                     content = @Content())
    })
    @PostMapping(
        headers = "Token"
    )
    ResponseEntity<Void> receiveFailureConfirmationByAnalyticSystem(
        @NotNull PostFailureConfirmationByAnalyticSystemRequest request
    );
}
