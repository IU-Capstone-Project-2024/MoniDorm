package com.java.api;

import com.java.api.model.PostProcessReportRequest;
import com.java.api.model.PostProcessReportResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Log4j2
public class AdminPanelController implements AdminPanelAPI {
    @Override
    public ResponseEntity<Object> confirmReport(long reportId) {
        return null;
    }

    @Override
    public ResponseEntity<PostProcessReportResponse> createReport(PostProcessReportRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<Object> deleteReport(long reportId) {
        return null;
    }
}

