package com.java.api;

import com.java.api.exception.NotFoundException;
import com.java.api.model.GetReportListResponse;
import com.java.api.model.GetReportResponse;
import com.java.api.model.PostFetchReportsByDateRequest;
import com.java.api.model.PostProcessReportRequest;
import com.java.api.model.PostProcessReportResponse;
import com.java.domain.model.Report;
import com.java.domain.service.ReportService;
import java.util.Comparator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@Log4j2
public class ReportController implements ReportAPI {
    private static final String NOT_FOUND_REPORT = "Report with such %s is not found";
    private final ReportService reportService;

    @Override
    public ResponseEntity<GetReportResponse> getReport(long reportId) {
        var maybeEntity = reportService.getReportById(reportId);

        if (maybeEntity.isEmpty()) {
            throw new NotFoundException(String.format(NOT_FOUND_REPORT, reportId));
        }

        return ResponseEntity.ok(maybeEntity.map(GetReportResponse::new).get());
    }

    @Override
    public ResponseEntity<Void> updateReportComment(long reportId, String description) {
        boolean isUpdated = reportService.updateReportComment(reportId, description);

        if (!isUpdated) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<GetReportListResponse> getAllReports() {
        return ResponseEntity.ok(
            new GetReportListResponse(
                reportService.getAllReports().orElseThrow().stream()
                    .map(GetReportResponse::new)
                    .toList()
            )
        );
    }

    @Override
    public ResponseEntity<GetReportListResponse> getReportsByDateWindow(PostFetchReportsByDateRequest request) {
        return ResponseEntity.ok(
            new GetReportListResponse(
                reportService.getAllReportsByDateWindows(request.startDate(), request.endDate()).orElseThrow().stream()
                    .map(GetReportResponse::new)
                    .toList()
            )
        );
    }

    @Override
    public ResponseEntity<GetReportListResponse> getReportsByPlacement(String placement) {
        return ResponseEntity.ok(
            new GetReportListResponse(
                reportService.getAllReportsByPlacement(placement).orElseThrow().stream()
                    .map(GetReportResponse::new)
                    .toList()
            )
        );
    }

    @Override
    public ResponseEntity<GetReportListResponse> getReportsByCategory(String category) {
        return ResponseEntity.ok(
            new GetReportListResponse(
                reportService.getAllReportsByCategory(category).orElseThrow().stream()
                    .map(GetReportResponse::new)
                    .toList()
            )
        );
    }

    @Override
    public ResponseEntity<GetReportListResponse> getReportsByEmail(String ownerEmail) {
        return ResponseEntity.ok(
            new GetReportListResponse(
                reportService.getAllReportsByOwnerEmail(ownerEmail).orElseThrow().stream()
                    .map(GetReportResponse::new)
                    .toList()
            )
        );
    }

    @Override
    public ResponseEntity<PostProcessReportResponse> processReport(PostProcessReportRequest request) {
        PostProcessReportResponse createdEntity;
        try {
            createdEntity = reportService.processReport(request).orElseThrow();
        } catch (JpaSystemException e) {
            createdEntity = reportService.getAllReportsByPlacement(request.placement())
                .orElseThrow().stream()
                .filter(report -> report.getCategory().equals(request.category())
                                  && report.getOwnerEmail().equals(request.ownerEmail()))
                .max(Comparator.comparing(Report::getFailureDate))
                .map(report -> new PostProcessReportResponse(report.getId(), report.getProceededDate()))
                .orElseThrow();
        }

        return ResponseEntity.ok(new PostProcessReportResponse(
            createdEntity.id(), createdEntity.lastSuccessfullyProceededDate()
        ));
    }

    @Override
    public ResponseEntity<Void> resolveReport(long reportId) {
        boolean isResolved = reportService.resolveReportByUser(reportId);

        if (!isResolved) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }
}
