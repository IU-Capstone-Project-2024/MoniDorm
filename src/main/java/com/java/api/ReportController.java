package com.java.api;

import com.java.api.model.GetReportListResponse;
import com.java.api.model.GetReportResponse;
import com.java.api.model.PostFetchReportsByDateRequest;
import com.java.api.model.PostProcessReportRequest;
import com.java.api.model.PostProcessReportResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
public class ReportController implements ReportAPI {
    @Override
    public ResponseEntity<GetReportResponse> getReport(long reportId) {
        return null;
    }

    @Override
    public ResponseEntity<GetReportListResponse> getAllReports() {
        return null;
    }

    @Override
    public ResponseEntity<GetReportListResponse> getReportsByDateWindow(PostFetchReportsByDateRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<GetReportListResponse> getReportsByPlacement(String placement) {
        return null;
    }

    @Override
    public ResponseEntity<GetReportListResponse> getReportsByCategory(String category) {
        return null;
    }

    @Override
    public ResponseEntity<PostProcessReportResponse> processReport(PostProcessReportRequest request) {
        return null;
    }
}
