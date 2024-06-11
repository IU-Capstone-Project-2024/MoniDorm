package com.java.domain.service.jpa;

import com.java.api.exception.NotFoundException;
import com.java.api.model.PostProcessReportRequest;
import com.java.api.model.PostProcessReportResponse;
import com.java.domain.model.Report;
import com.java.domain.repository.ReportRepository;
import com.java.domain.service.ReportService;
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
    public Optional<List<Report>> getAllReportsByDateWindows(OffsetDateTime startWindow, OffsetDateTime endWindow) {
        return reportRepository.findAllByProceededDateBeforeAndProceededDateAfter(startWindow, endWindow);
    }

    @Override
    public Optional<PostProcessReportResponse> processReport(PostProcessReportRequest request) {
        var entity = reportRepository.save(new Report(
            request.category(), request.placement(), request.dateTime(), OffsetDateTime.now(), false, false
        ));

        return Optional.of(new PostProcessReportResponse(
            entity.getId(), entity.getProceededDate()
        ));
    }

    @Override
    public boolean confirmReport(long id) {
        var entity = reportRepository.findById(id).orElseThrow(() -> new NotFoundException(EXCEPTION_MESSAGE));
        entity.setResolved(true);
        reportRepository.flush();

        return true;
    }

    @Override
    public Optional<PostProcessReportResponse> processReportForcefully(PostProcessReportRequest request) {
        var entity = reportRepository.save(new Report(
            request.category(), request.placement(), request.dateTime(), OffsetDateTime.now(), true, false
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
