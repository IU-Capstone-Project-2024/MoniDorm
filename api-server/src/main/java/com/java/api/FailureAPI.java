package com.java.api;

import com.java.api.model.GetFailureListResponse;
import com.java.api.model.PostFetchByDateRequest;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Validated
@RequestMapping(("/api/failure"))
public interface FailureAPI {
    @Operation(summary = "Get all failures")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "Failures successfully returned",
                     content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        schema = @Schema(implementation = GetFailureListResponse.class))
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
    ResponseEntity<GetFailureListResponse> getAllFailures();

    @Operation(summary = "Get all the failures by the date window (exclusive)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "Failures successfully returned",
                     content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        schema = @Schema(implementation = GetFailureListResponse.class))
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
    ResponseEntity<GetFailureListResponse> getFailuresByDateWindow(
        @RequestBody @NotNull @Valid PostFetchByDateRequest request
    );

    @Operation(summary = "Get all the failures by the placement")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "Failures successfully returned",
                     content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        schema = @Schema(implementation = GetFailureListResponse.class))
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
    ResponseEntity<GetFailureListResponse> getReportsByPlacement(@RequestParam @NotBlank String placement);

    @Operation(summary = "Get all the failures by the category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "Failures successfully returned",
                     content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        schema = @Schema(implementation = GetFailureListResponse.class))
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
    ResponseEntity<GetFailureListResponse> getReportsByCategory(@RequestParam @NotBlank String category);
}
