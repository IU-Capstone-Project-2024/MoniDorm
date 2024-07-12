package com.java.api;

import com.java.api.model.PostProcessReportRequest;
import com.java.api.model.PostProcessReportResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Validated
@RequestMapping(("/api/admin"))
public interface AdminPanelAPI {
    @Operation(summary = "Confirm report with certain id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "Report successfully confirmed",
                     content = @Content()
        ),

        @ApiResponse(responseCode = "400",
                     description = "Request was malformed",
                     content = @Content()),

        @ApiResponse(responseCode = "403",
                     description = "Incorrect access token provided",
                     content = @Content()),

        @ApiResponse(responseCode = "404",
                     description = "The entity is not found",
                     content = @Content())
    })
    @GetMapping(
        headers = "Token"
    )
    ResponseEntity<Void> confirmReport(@RequestParam(value = "report_id") long reportId);

    @Operation(summary = "Resolve report with certain id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "Report successfully resolved",
                     content = @Content()
        ),

        @ApiResponse(responseCode = "400",
                     description = "Request was malformed",
                     content = @Content()),

        @ApiResponse(responseCode = "403",
                     description = "Incorrect access token provided",
                     content = @Content()),

        @ApiResponse(responseCode = "404",
                     description = "The entity is not found",
                     content = @Content())
    })
    @GetMapping(
        path = "/resolve",
        headers = "Token"
    )
    ResponseEntity<Void> resolveReportForcefully(@RequestParam(value = "report_id") long reportId);

    @Operation(summary = "Resolve report with certain id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "Report successfully resolved",
                     content = @Content()
        ),

        @ApiResponse(responseCode = "400",
                     description = "Request was malformed",
                     content = @Content()),

        @ApiResponse(responseCode = "403",
                     description = "Incorrect access token provided",
                     content = @Content())
    })
    @GetMapping(
        path = "/resolveByMeta",
        headers = "Token"
    )
    ResponseEntity<Void> resolveReportForcefullyByMeta(
        @RequestParam @NotBlank String placement,
        @RequestParam @NotBlank String category
    );

    @Operation(summary = "Create confirmed report instantly")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "Report successfully created",
                     content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        schema = @Schema(implementation = PostProcessReportResponse.class))
        ),

        @ApiResponse(responseCode = "400",
                     description = "Request was malformed",
                     content = @Content()),

        @ApiResponse(responseCode = "403",
                     description = "Incorrect access token provided",
                     content = @Content())
    })
    @PostMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        headers = "Token"
    )
    ResponseEntity<PostProcessReportResponse> createReport(
        @RequestBody @NotNull @Valid PostProcessReportRequest request
    );

    @Operation(summary = "Delete report with certain id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "Report successfully deleted",
                     content = @Content()),

        @ApiResponse(responseCode = "400",
                     description = "Request was malformed",
                     content = @Content()),

        @ApiResponse(responseCode = "403",
                     description = "Incorrect access token provided",
                     content = @Content()),

        @ApiResponse(responseCode = "404",
                     description = "The entity is not found",
                     content = @Content())
    })
    @PatchMapping(
        headers = "Token",
        path = "/report"
    )
    ResponseEntity<Void> deleteReport(@RequestParam(value = "report_id") long reportId);

    @Operation(summary = "Delete failure with certain id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "Failure successfully deleted",
                     content = @Content()),

        @ApiResponse(responseCode = "400",
                     description = "Request was malformed",
                     content = @Content()),

        @ApiResponse(responseCode = "403",
                     description = "Incorrect access token provided",
                     content = @Content()),

        @ApiResponse(responseCode = "404",
                     description = "The entity is not found",
                     content = @Content())
    })
    @PatchMapping(
        headers = "Token",
        path = "/failure"
    )
    ResponseEntity<Void> deleteFailure(@RequestParam(value = "failure_id") long failureId);
}
