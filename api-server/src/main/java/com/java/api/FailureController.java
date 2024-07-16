package com.java.api;

import com.java.api.model.GetFailureListResponse;
import com.java.api.model.GetFailureResponse;
import com.java.api.model.PostFetchByDateRequest;
import com.java.domain.service.FailureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
public class FailureController implements FailureAPI {
    private final FailureService failureService;

    @Override
    public ResponseEntity<GetFailureListResponse> getAllFailures() {
        return ResponseEntity.ok(
            new GetFailureListResponse(
                failureService.getAllFailures().orElseThrow().stream()
                    .map(GetFailureResponse::new)
                    .toList()
            )
        );
    }

    @Override
    public ResponseEntity<GetFailureListResponse> getFailuresByDateWindow(PostFetchByDateRequest request) {
        return ResponseEntity.ok(
            new GetFailureListResponse(
                failureService.getAllFailuresByDateWindows(request.startDate(), request.endDate()).orElseThrow()
                    .stream()
                    .map(GetFailureResponse::new)
                    .toList()
            )
        );
    }

    @Override
    public ResponseEntity<GetFailureListResponse> getReportsByPlacement(String placement) {
        return ResponseEntity.ok(
            new GetFailureListResponse(
                failureService.getAllFailuresByPlacement(placement).orElseThrow().stream()
                    .map(GetFailureResponse::new)
                    .toList()
            )
        );
    }

    @Override
    public ResponseEntity<GetFailureListResponse> getReportsByCategory(String category) {
        return ResponseEntity.ok(
            new GetFailureListResponse(
                failureService.getAllFailuresByCategory(category).orElseThrow().stream()
                    .map(GetFailureResponse::new)
                    .toList()
            )
        );
    }
}
