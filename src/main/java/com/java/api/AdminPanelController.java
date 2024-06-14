package com.java.api;

import com.java.api.exception.NotFoundException;
import com.java.api.model.PostProcessReportRequest;
import com.java.api.model.PostProcessReportResponse;
import com.java.domain.service.ReportService;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Log4j2
public class AdminPanelController implements AdminPanelAPI {
    private final ReportService reportService;

    @Override
    public ResponseEntity<Void> confirmReport(long reportId) {
        boolean isConfirmed = reportService.confirmReportByAdmin(reportId);

        if (!isConfirmed) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> resolveReportForcefully(long reportId) {
        boolean isResolved = reportService.resolveReportByAdmin(reportId);

        if (!isResolved) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> resolveReportForcefullyByMeta(String placement, String category) {
        final String errorMsg = "List for the meta is empty";

        AtomicInteger dataProceeded = new AtomicInteger();
        reportService.getAllReportsByPlacement(placement).ifPresentOrElse(
            reports -> reports.forEach(report -> {
                if (report.getCategory().equals(category)) {
                    reportService.resolveReportByAdmin(report.getId());
                    dataProceeded.getAndIncrement();
                }
            }), () -> {
                throw new NotFoundException(errorMsg);
            }
        );

        if (dataProceeded.get() == 0) {
            throw new NotFoundException(errorMsg);
        }

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<PostProcessReportResponse> createReport(PostProcessReportRequest request) {
        var createdEntity = reportService.processReportForcefully(request).orElseThrow();

        return ResponseEntity.ok(new PostProcessReportResponse(
            createdEntity.id(), createdEntity.lastSuccessfullyProceededDate()
        ));
    }

    @Override
    public ResponseEntity<Void> deleteReport(long reportId) {
        boolean isDeleted = reportService.deleteReport(reportId);

        if (!isDeleted) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }
}

