package com.java.domain.service;

import com.java.api.model.PostProcessReportRequest;
import com.java.api.model.PostProcessReportResponse;
import com.java.domain.model.Report;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public interface ReportService {
    @NotNull Optional<Report> getReportById(@Min(0) long id);

    @NotNull Optional<List<Report>> getAllReports();

    @NotNull Optional<List<Report>> getAllReportsByPlacement(@NotBlank String placement);

    @NotNull Optional<List<Report>> getAllReportsByCategory(@NotBlank String category);

    @NotNull Optional<List<Report>> getAllReportsByDateWindows(OffsetDateTime startWindow, OffsetDateTime endWindow);

    @NotNull Optional<List<Report>> getAllReportsByOwnerEmail(@NotBlank String ownerEmail);

    @NotNull Optional<PostProcessReportResponse> processReport(@NotNull PostProcessReportRequest request);

    boolean confirmReportByAnalysis(@Min(0) long id);

    boolean confirmReportByAdmin(@Min(0) long id);

    boolean resolveReportByUser(@Min(0) long id);

    boolean resolveReportByAdmin(@Min(0) long id);

    @NotNull Optional<PostProcessReportResponse> processReportForcefully(@NotNull PostProcessReportRequest request);

    boolean deleteReport(@Min(0) long id);
}
