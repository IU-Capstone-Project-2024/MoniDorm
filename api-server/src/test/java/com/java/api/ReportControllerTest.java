package com.java.api;

import com.java.api.model.GetReportListResponse;
import com.java.api.model.GetReportResponse;
import com.java.api.model.PostFetchByDateRequest;
import com.java.api.model.PostProcessReportRequest;
import com.java.api.model.PostProcessReportResponse;
import com.java.domain.model.Report;
import com.java.domain.service.ReportService;
import java.time.OffsetDateTime;
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

@WebFluxTest(controllers = ReportController.class)
@AutoConfigureWebTestClient()
class ReportControllerTest {
    private static final String COMMON_AUTHORIZATION_HEADER_NAME = "Token";
    private static final String COMMON_HEADER_VALUE = "token";

    private static final Report REPORT = new Report(
        "category", "placement", OffsetDateTime.now(), "email", OffsetDateTime.now(),
        false, false, false, false, null
    );

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ReportService reportService;

    @Test
    void givenId_whenGetNotExistingReport_thenNotFound() {
        final long id = 1L;
        Mockito.when(reportService.getReportById(id)).thenReturn(Optional.empty());

        webTestClient.get()
            .uri(builder -> builder.queryParam("report_id", id).path("/api/report").build())
            .header(COMMON_AUTHORIZATION_HEADER_NAME, COMMON_HEADER_VALUE)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void givenId_whenGetExistingReport_thenNotFound() {
        final long id = 1L;
        REPORT.setId(id);
        Mockito.when(reportService.getReportById(id)).thenReturn(Optional.of(REPORT));

        webTestClient.get()
            .uri(builder -> builder.queryParam("report_id", id).path("/api/report").build())
            .header(COMMON_AUTHORIZATION_HEADER_NAME, COMMON_HEADER_VALUE)
            .exchange()
            .expectBody(GetReportResponse.class)
            .value(GetReportResponse::id, Matchers.equalTo(REPORT.getId()));
    }

    @Test
    void givenIdWithComment_whenUpdateNotExistingReport_thenBadRequest() {
        final long id = 1L;
        final String description = "description";
        Mockito.when(reportService.updateReportComment(id, description)).thenReturn(false);

        webTestClient.patch()
            .uri(uriBuilder -> uriBuilder.queryParam("report_id", id).queryParam("description", description)
                .path("/api/report/update/comment").build())
            .header(COMMON_AUTHORIZATION_HEADER_NAME, COMMON_HEADER_VALUE)
            .exchange()
            .expectStatus()
            .isBadRequest();
    }

    @Test
    void givenIdWithComment_whenUpdateExistingReport_thenBadRequest() {
        final long id = 1L;
        final String description = "description";
        Mockito.when(reportService.updateReportComment(id, description)).thenReturn(true);

        webTestClient.patch()
            .uri(uriBuilder -> uriBuilder.queryParam("report_id", id).queryParam("description", description)
                .path("/api/report/update/comment").build())
            .header(COMMON_AUTHORIZATION_HEADER_NAME, COMMON_HEADER_VALUE)
            .exchange()
            .expectStatus()
            .isOk();
    }

    @Test
    void givenReports_whenPullAll_thenSuccess() {
        Mockito.when(reportService.getAllReports()).thenReturn(Optional.of(List.of(REPORT)));

        webTestClient.get()
            .uri("/api/report/all")
            .header(COMMON_AUTHORIZATION_HEADER_NAME, COMMON_HEADER_VALUE)
            .exchange()
            .expectBody(GetReportListResponse.class)
            .value(GetReportListResponse::size, Matchers.equalTo(1L));
    }

    @Test
    void givenCorrectWindow_thenWhenPullReportsInWindow_correctlyFetched() {
        var startTime = REPORT.getFailureDate().minusHours(1);
        var endTime = REPORT.getFailureDate().plusHours(1);
        var request = new PostFetchByDateRequest(startTime, endTime);
        Mockito.when(reportService.getAllReportsByDateWindows(Mockito.any(), Mockito.any()))
            .thenReturn(Optional.of(List.of(REPORT)));

        webTestClient.post()
            .uri("/api/report/allByDates")
            .header(COMMON_AUTHORIZATION_HEADER_NAME, COMMON_HEADER_VALUE)
            .bodyValue(request)
            .exchange()
            .expectBody(GetReportListResponse.class)
            .value(GetReportListResponse::size, Matchers.equalTo(1L));
    }

    @Test
    void givenCorrectPlacement_thenWhenPullReportsWithSuchPlacement_correctlyFetched() {
        final String placement = "placement";
        Mockito.when(reportService.getAllReportsByPlacement(placement))
            .thenReturn(Optional.of(List.of(REPORT)));

        webTestClient.get()
            .uri(uriBuilder -> uriBuilder.queryParam("placement", placement).path("/api/report/allByPlacement").build())
            .header(COMMON_AUTHORIZATION_HEADER_NAME, COMMON_HEADER_VALUE)
            .exchange()
            .expectBody(GetReportListResponse.class)
            .value(GetReportListResponse::size, Matchers.equalTo(1L));
    }

    @Test
    void givenCorrectCategory_thenWhenPullReportsWithSuchPlacement_correctlyFetched() {
        final String category = "category";
        Mockito.when(reportService.getAllReportsByCategory(category))
            .thenReturn(Optional.of(List.of(REPORT)));

        webTestClient.get()
            .uri(uriBuilder -> uriBuilder.queryParam("category", category).path("/api/report/allByCategory").build())
            .header(COMMON_AUTHORIZATION_HEADER_NAME, COMMON_HEADER_VALUE)
            .exchange()
            .expectBody(GetReportListResponse.class)
            .value(GetReportListResponse::size, Matchers.equalTo(1L));
    }

    @Test
    void givenCorrectEmail_thenWhenPullReportsWithSuchPlacement_correctlyFetched() {
        final String email = "email";
        Mockito.when(reportService.getAllReportsByOwnerEmail(email))
            .thenReturn(Optional.of(List.of(REPORT)));

        webTestClient.get()
            .uri(uriBuilder -> uriBuilder.queryParam("ownerEmail", email).path("/api/report/allByEmail").build())
            .header(COMMON_AUTHORIZATION_HEADER_NAME, COMMON_HEADER_VALUE)
            .exchange()
            .expectBody(GetReportListResponse.class)
            .value(GetReportListResponse::size, Matchers.equalTo(1L));
    }

    @Test
    void givenRequestWithReport_whenProcessing_thenSuccess() {
        var request = new PostProcessReportRequest(
            "category", "placement", OffsetDateTime.now(), "email", "description"
        );
        var response = new PostProcessReportResponse(REPORT.getId(), REPORT.getProceededDate());
        Mockito.when(reportService.processReport(Mockito.any())).thenReturn(Optional.of(response));

        webTestClient.post()
            .uri("/api/report")
            .header(COMMON_AUTHORIZATION_HEADER_NAME, COMMON_HEADER_VALUE)
            .bodyValue(request)
            .exchange()
            .expectBody(PostProcessReportResponse.class)
            .value(PostProcessReportResponse::id, Matchers.equalTo(REPORT.getId()));
    }

    @Test
    void givenId_whenReportNotExists_thenBadRequest() {
        final long id = 1L;
        Mockito.when(reportService.resolveReportByUser(id)).thenReturn(false);

        webTestClient.patch()
            .uri(uriBuilder -> uriBuilder.queryParam("report_id", id).path("/api/report").build())
            .header(COMMON_AUTHORIZATION_HEADER_NAME, COMMON_HEADER_VALUE)
            .exchange()
            .expectStatus()
            .isBadRequest();
    }

    @Test
    void givenId_whenReportExists_thenOk() {
        final long id = 1L;
        Mockito.when(reportService.resolveReportByUser(id)).thenReturn(true);

        webTestClient.patch()
            .uri(uriBuilder -> uriBuilder.queryParam("report_id", id).path("/api/report").build())
            .header(COMMON_AUTHORIZATION_HEADER_NAME, COMMON_HEADER_VALUE)
            .exchange()
            .expectStatus()
            .isOk();
    }
}
