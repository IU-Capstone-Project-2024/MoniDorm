package com.java.api;

import com.java.api.model.PostFailureConfirmationByAnalyticSystemRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
public class AnalystSystemController implements AnalystSystemAPI {
    @Override
    public ResponseEntity<Void> receiveFailureConfirmationByAnalyticSystem(
        PostFailureConfirmationByAnalyticSystemRequest request
    ) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
