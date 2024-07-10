package com.java.api;

import com.java.api.model.GetReportListResponse;
import com.java.api.model.GetReportResponse;
import com.java.api.model.PostFetchReportsByDateRequest;
import com.java.api.model.PostProcessReportRequest;
import com.java.api.model.PostProcessReportResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
@RequestMapping(("/api/report"))
public interface ReportAPI {
    @Operation(summary = "Get report with certain id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "Report successfully returned",
                     content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        schema = @Schema(implementation = GetReportResponse.class))
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
    ResponseEntity<GetReportResponse> getReport(@RequestParam(value = "report_id") @Min(0) long reportId);

    @Operation(summary = "Update report comment with certain id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "Report successfully returned",
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
    @PatchMapping(
        path = "/update/comment",
        headers = "Token"
    )
    ResponseEntity<Void> updateReportComment(
        @RequestParam(value = "report_id") @Min(0) long reportId,
        @RequestParam String comment
    );

    @Operation(summary = "Get all reports")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "Reports successfully returned",
                     content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        schema = @Schema(implementation = GetReportListResponse.class))
        ),

        @ApiResponse(responseCode = "400",
                     description = "Request was malformed",
                     content = @Content()),

        @ApiResponse(responseCode = "403",
                     description = "Incorrect access token provided",
                     content = @Content())
    })
    @GetMapping(
        path = "/all",
        headers = "Token"
    )
    ResponseEntity<GetReportListResponse> getAllReports();

    @Operation(summary = "Get all the reports by the date window (exclusive)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "Reports successfully returned",
                     content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        schema = @Schema(implementation = GetReportListResponse.class))
        ),

        @ApiResponse(responseCode = "400",
                     description = "Request was malformed",
                     content = @Content()),

        @ApiResponse(responseCode = "403",
                     description = "Incorrect access token provided",
                     content = @Content())
    })
    @PostMapping(
        path = "/allByDates",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        headers = "Token"
    )
    ResponseEntity<GetReportListResponse> getReportsByDateWindow(
        @RequestBody @NotNull @Valid PostFetchReportsByDateRequest request
    );

    @Operation(summary = "Get all the reports by the placement")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "Reports successfully returned",
                     content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        schema = @Schema(implementation = GetReportListResponse.class))
        ),

        @ApiResponse(responseCode = "400",
                     description = "Request was malformed",
                     content = @Content()),

        @ApiResponse(responseCode = "403",
                     description = "Incorrect access token provided",
                     content = @Content())
    })
    @GetMapping(
        path = "/allByPlacement",
        headers = "Token"
    )
    ResponseEntity<GetReportListResponse> getReportsByPlacement(@RequestParam @NotBlank String placement);

    @Operation(summary = "Get all the reports by the category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "Reports successfully returned",
                     content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        schema = @Schema(implementation = GetReportListResponse.class))
        ),

        @ApiResponse(responseCode = "400",
                     description = "Request was malformed",
                     content = @Content()),

        @ApiResponse(responseCode = "403",
                     description = "Incorrect access token provided",
                     content = @Content())
    })

    @GetMapping(
        path = "/allByCategory",
        headers = "Token"
    )
    ResponseEntity<GetReportListResponse> getReportsByCategory(@RequestParam @NotBlank String category);

    @Operation(summary = "Get all the reports by the owner email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "Reports successfully returned",
                     content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        schema = @Schema(implementation = GetReportListResponse.class))
        ),

        @ApiResponse(responseCode = "400",
                     description = "Request was malformed",
                     content = @Content()),

        @ApiResponse(responseCode = "403",
                     description = "Incorrect access token provided",
                     content = @Content())
    })
    @GetMapping(
        path = "/allByEmail",
        headers = "Token"
    )
    ResponseEntity<GetReportListResponse> getReportsByEmail(@RequestParam @NotBlank String ownerEmail);

    @Operation(summary = "Process report from the user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "Report proceeded successfully",
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
    ResponseEntity<PostProcessReportResponse> processReport(
        @RequestBody @NotNull @Valid PostProcessReportRequest request
    );

    @Operation(summary = "Resolve report from the user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "Report resolved successfully",
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
    @PatchMapping(
        headers = "Token"
    )
    ResponseEntity<Void> resolveReport(@RequestParam(value = "report_id") @Min(0) long reportId);
}
