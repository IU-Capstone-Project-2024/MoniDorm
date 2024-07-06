package com.java.api;

import com.java.api.model.PostProcessReportRequest;
import com.java.api.model.PostProcessReportResponse;
import com.java.domain.model.Report;
import com.java.domain.service.ReportService;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(controllers = AdminPanelController.class)
@AutoConfigureWebTestClient
class AdminPanelControllerTest {
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
    void givenReportId_whenReportNotExists_thenNotFoundException() {
        final long id = 1L;
        Mockito.when(reportService.confirmReportByAdmin(id)).thenReturn(false);

        webTestClient.get()
            .uri(uriBuilder -> uriBuilder.queryParam("report_id", id).path("/api/admin").build())
            .header(COMMON_AUTHORIZATION_HEADER_NAME, COMMON_HEADER_VALUE)
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    void givenReportId_whenReportExists_thenNotFoundException() {
        final long id = 1L;
        Mockito.when(reportService.confirmReportByAdmin(id)).thenReturn(true);

        webTestClient.get()
            .uri(uriBuilder -> uriBuilder.queryParam("report_id", id).path("/api/admin").build())
            .header(COMMON_AUTHORIZATION_HEADER_NAME, COMMON_HEADER_VALUE)
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void givenReportId_whenReportNotExistsAndTryResolve_thenNotFoundException() {
        final long id = 1L;
        Mockito.when(reportService.resolveReportByAdmin(id)).thenReturn(false);

        webTestClient.get()
            .uri(uriBuilder -> uriBuilder.queryParam("report_id", id).path("/api/admin/resolve").build())
            .header(COMMON_AUTHORIZATION_HEADER_NAME, COMMON_HEADER_VALUE)
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    void givenReportId_whenReportExistsAndTryResolve_thenNotFoundException() {
        final long id = 1L;
        Mockito.when(reportService.resolveReportByAdmin(id)).thenReturn(true);

        webTestClient.get()
            .uri(uriBuilder -> uriBuilder.queryParam("report_id", id).path("/api/admin/resolve").build())
            .header(COMMON_AUTHORIZATION_HEADER_NAME, COMMON_HEADER_VALUE)
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void givenNoReport_whenResolveByMeta_thenNotFoundException() {
        final String placement = "placement";
        final String category = "category";
        Mockito.when(reportService.getAllReportsByPlacement(placement))
            .thenReturn(Optional.empty());

        webTestClient.get()
            .uri(uriBuilder -> uriBuilder.queryParam(
                "placement",
                placement
            ).queryParam(
                "category",
                category
            ).path("/api/admin/resolveByMeta").build())
            .header(COMMON_AUTHORIZATION_HEADER_NAME, COMMON_HEADER_VALUE)
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    void givenSingleRepoWithIncorrectPlacement_whenResolveByMeta_thenNotFoundException() {
        final String placement = "placement";
        final String category = "dasda";
        REPORT.setId(1L);
        Mockito.when(reportService.getAllReportsByPlacement(placement))
            .thenReturn(Optional.of(List.of(REPORT)));
        Mockito.when(reportService.resolveReportByAdmin(1L)).thenReturn(true);

        webTestClient.get()
            .uri(uriBuilder -> uriBuilder.queryParam(
                "placement",
                placement
            ).queryParam(
                "category",
                category
            ).path("/api/admin/resolveByMeta").build())
            .header(COMMON_AUTHORIZATION_HEADER_NAME, COMMON_HEADER_VALUE)
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    void givenSingleRepoWithCorrectPlacement_whenResolveByMeta_thenNotFoundException() {
        final String placement = "placement";
        final String category = "category";
        REPORT.setId(1L);
        Mockito.when(reportService.getAllReportsByPlacement(placement))
            .thenReturn(Optional.of(List.of(REPORT)));
        Mockito.when(reportService.resolveReportByAdmin(1L)).thenReturn(true);

        webTestClient.get()
            .uri(uriBuilder -> uriBuilder.queryParam(
                "placement",
                placement
            ).queryParam(
                "category",
                category
            ).path("/api/admin/resolveByMeta").build())
            .header(COMMON_AUTHORIZATION_HEADER_NAME, COMMON_HEADER_VALUE)
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void givenRequest_whenCreateReport_thenSuccessfullyCreated() {
        var request = new PostProcessReportRequest("category", "placement", OffsetDateTime.now(), "email", "a");
        var response = new PostProcessReportResponse(REPORT.getId(), REPORT.getProceededDate());
        Mockito.when(reportService.processReportForcefully(Mockito.any())).thenReturn(Optional.of(response));

        webTestClient.post()
            .uri("/api/admin")
            .header(COMMON_AUTHORIZATION_HEADER_NAME, COMMON_HEADER_VALUE)
            .bodyValue(request)
            .exchange()
            .expectBody(PostProcessReportResponse.class)
            .value(PostProcessReportResponse::id, Matchers.equalTo(REPORT.getId()));
    }

    @Test
    void givenReportId_whenReportNotExists_thenBadRequest() {
        final long id = 1L;
        Mockito.when(reportService.deleteReport(id)).thenReturn(false);

        webTestClient.delete()
            .uri("/api/admin", Map.of("report_id", id))
            .header(COMMON_AUTHORIZATION_HEADER_NAME, COMMON_HEADER_VALUE)
            .exchange()
            .expectStatus()
            .isBadRequest();
    }

    @Test
    void givenReportId_whenReportExists_thenOk() {
        final long id = 1L;
        Mockito.when(reportService.deleteReport(id)).thenReturn(true);

        webTestClient.delete()
            .uri(uriBuilder -> uriBuilder.queryParam("report_id", id).path("/api/admin").build())
            .header(COMMON_AUTHORIZATION_HEADER_NAME, COMMON_HEADER_VALUE)
            .exchange()
            .expectStatus()
            .isOk();
    }
}
