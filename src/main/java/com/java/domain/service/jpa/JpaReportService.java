package com.java.domain.service.jpa;

import com.java.api.exception.NotFoundException;
import com.java.api.model.PostProcessReportRequest;
import com.java.api.model.PostProcessReportResponse;
import com.java.domain.model.Report;
import com.java.domain.repository.ReportRepository;
import com.java.domain.service.ReportService;
import jakarta.annotation.Nullable;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JpaReportService implements ReportService {
    private static final String EXCEPTION_MESSAGE = "Report is not found";
    private final ReportRepository reportRepository;

    @Override
    public Optional<Report> getReportById(long id) {
        return reportRepository.findById(id);
    }

    @Override
    public Optional<List<Report>> getAllReports() {
        return Optional.of(reportRepository.findAll());
    }

    @Override
    public Optional<List<Report>> getAllReportsByPlacement(String placement) {
        return reportRepository.findAllByPlacementLike(placement);
    }

    @Override
    public Optional<List<Report>> getAllReportsByCategory(String category) {
        return reportRepository.findAllByCategory(category);
    }

    @Override
    public Optional<List<Report>> getAllReportsByDateWindows(
        @Nullable OffsetDateTime startWindow,
        @Nullable OffsetDateTime endWindow
    ) {
        if (startWindow == null && endWindow == null) {
            return Optional.of(reportRepository.findAll());
        }

        if (startWindow != null && endWindow != null) {
            return reportRepository.findAllByFailureDateAfterAndFailureDateBefore(startWindow, endWindow);
        }

        if (startWindow != null) {
            return reportRepository.findAllByFailureDateAfter(startWindow);
        }

        return reportRepository.findAllByFailureDateBefore(endWindow);

    }

    @Override
    public Optional<List<Report>> getAllReportsByOwnerEmail(String ownerEmail) {
        return reportRepository.findAllByOwnerEmail(ownerEmail);
    }

    @Override
    public Optional<PostProcessReportResponse> processReport(PostProcessReportRequest request) {
        var entity = reportRepository.save(new Report(
            request.category(), request.placement(), request.dateTime(),
            request.ownerEmail(), OffsetDateTime.now(), false,
            false, false, false
        ));

        return Optional.of(new PostProcessReportResponse(
            entity.getId(), entity.getProceededDate()
        ));
    }

    @Override
    public boolean confirmReportByAnalysis(long id) {
        var entity = reportRepository.findById(id).orElseThrow(() -> new NotFoundException(EXCEPTION_MESSAGE));
        entity.setConfirmedByAnalysis(true);
        reportRepository.flush();

        return true;
    }

    @Override
    public boolean confirmReportByAdmin(long id) {
        var entity = reportRepository.findById(id).orElseThrow(() -> new NotFoundException(EXCEPTION_MESSAGE));
        entity.setConfirmedByAdmin(true);
        reportRepository.flush();

        return true;
    }

    @Override
    public boolean resolveReportByUser(long id) {
        var entity = reportRepository.findById(id).orElseThrow(() -> new NotFoundException(EXCEPTION_MESSAGE));
        entity.setResolvedByUser(true);
        reportRepository.flush();

        return true;
    }

    @Override
    public boolean resolveReportByAdmin(long id) {
        var entity = reportRepository.findById(id).orElseThrow(() -> new NotFoundException(EXCEPTION_MESSAGE));
        entity.setResolvedByAdmin(true);
        reportRepository.flush();

        return true;
    }

    @Override
    public Optional<PostProcessReportResponse> processReportForcefully(PostProcessReportRequest request) {
        var entity = reportRepository.save(new Report(
            request.category(), request.placement(), request.dateTime(),
            request.ownerEmail(), OffsetDateTime.now(), false,
            true, false, false
        ));

        return Optional.of(new PostProcessReportResponse(
            entity.getId(), entity.getProceededDate()
        ));
    }

    @Override
    public boolean deleteReport(long id) {
        var entity = reportRepository.findById(id).orElseThrow(() -> new NotFoundException(EXCEPTION_MESSAGE));
        reportRepository.delete(entity);
        reportRepository.flush();

        return true;
    }
}
