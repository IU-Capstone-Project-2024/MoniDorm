package com.java.api;

import com.java.api.model.GetFailureListResponse;
import com.java.api.model.PostFetchByDateRequest;
import com.java.domain.model.Failure;
import com.java.domain.service.FailureService;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(controllers = FailureController.class)
@AutoConfigureWebTestClient()
public class FailureControllerTest {
    private static final String COMMON_AUTHORIZATION_HEADER_NAME = "Token";
    private static final String COMMON_HEADER_VALUE = "token";
    private static final OffsetDateTime FAILURE_DATE =
        OffsetDateTime.of(2024, 6, 10,
            2, 0, 0, 0, ZoneOffset.ofHours(3)
        );
    private static final Failure FAILURE = new Failure(
        1L, "category", "placement", FAILURE_DATE, 1, "messages", "summarization"
    );

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private FailureService failureService;

    @Test
    void givenFailures_whenPullAll_thenSuccess() {
        Mockito.when(failureService.getAllFailures()).thenReturn(Optional.of(List.of(FAILURE)));

        webTestClient.get()
            .uri("/api/failure/all")
            .header(COMMON_AUTHORIZATION_HEADER_NAME, COMMON_HEADER_VALUE)
            .exchange()
            .expectBody(GetFailureListResponse.class)
            .value(GetFailureListResponse::size, Matchers.equalTo(1L));
    }

    @Test
    void givenCorrectWindow_thenWhenPullFailuresInWindow_correctlyFetched() {
        var startTime = FAILURE.getFailureDate().minusHours(1);
        var endTime = FAILURE.getFailureDate().plusHours(1);
        var request = new PostFetchByDateRequest(startTime, endTime);
        Mockito.when(failureService.getAllFailuresByDateWindows(Mockito.any(), Mockito.any()))
            .thenReturn(Optional.of(List.of(FAILURE)));

        webTestClient.post()
            .uri("/api/failure/allByDates")
            .header(COMMON_AUTHORIZATION_HEADER_NAME, COMMON_HEADER_VALUE)
            .bodyValue(request)
            .exchange()
            .expectBody(GetFailureListResponse.class)
            .value(GetFailureListResponse::size, Matchers.equalTo(1L));
    }

    @Test
    void givenCorrectPlacement_thenWhenPullFailuresWithSuchPlacement_correctlyFetched() {
        final String placement = "placement";
        Mockito.when(failureService.getAllFailuresByPlacement(placement))
            .thenReturn(Optional.of(List.of(FAILURE)));

        webTestClient.get()
            .uri(uriBuilder -> uriBuilder.queryParam("placement", placement).path("/api/failure/allByPlacement")
                .build())
            .header(COMMON_AUTHORIZATION_HEADER_NAME, COMMON_HEADER_VALUE)
            .exchange()
            .expectBody(GetFailureListResponse.class)
            .value(GetFailureListResponse::size, Matchers.equalTo(1L));
    }

    @Test
    void givenCorrectCategory_thenWhenPullReportsWithSuchPlacement_correctlyFetched() {
        final String category = "category";
        Mockito.when(failureService.getAllFailuresByCategory(category))
            .thenReturn(Optional.of(List.of(FAILURE)));

        webTestClient.get()
            .uri(uriBuilder -> uriBuilder.queryParam("category", category).path("/api/failure/allByCategory").build())
            .header(COMMON_AUTHORIZATION_HEADER_NAME, COMMON_HEADER_VALUE)
            .exchange()
            .expectBody(GetFailureListResponse.class)
            .value(GetFailureListResponse::size, Matchers.equalTo(1L));
    }
}
