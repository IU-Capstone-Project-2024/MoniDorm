package com.java.domain.service.jpa;

import com.java.IntegrationEnvironment;
import com.java.api.exception.NotFoundException;
import com.java.api.model.PostProcessReportRequest;
import com.java.domain.service.ReportService;
import jakarta.transaction.Transactional;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JpaReportServiceTest extends IntegrationEnvironment {
    private static final OffsetDateTime FAILURE_DATE =
        OffsetDateTime.of(2024, 6, 10,
            2, 0, 0, 0, ZoneOffset.ofHours(3)
        );
    private static long REPORT_ID_1, REPORT_ID_2;

    @Autowired
    private ReportService reportService;

    @Test
    @Transactional
    @Rollback(false)
    @Order(1)
    void givenEmptyDB_whenTryingToFetchAny_thenNothing() {
        assertThat(reportService.getReportById(1L)).isEmpty();
        assertThat(reportService.getAllReports().get()).asList().isEmpty();
        assertThat(reportService.getAllReportsByPlacement("placement").get()).asList().isEmpty();
        assertThat(reportService.getAllReportsByCategory("category").get()).asList().isEmpty();
        assertThat(reportService.getAllReportsByDateWindows(null, null).get()).asList().isEmpty();
        assertThat(reportService.getAllReportsByOwnerEmail("email").get()).asList().isEmpty();
    }

    @Test
    @Transactional
    @Rollback(false)
    @Order(2)
    void givenEmptyDB_whenAddReport_thenCorrectlyAdded() {
        PostProcessReportRequest request = new PostProcessReportRequest(
            "category", "placement", FAILURE_DATE,
            "email", "description"
        );

        var response = reportService.processReport(request).orElseThrow();
        REPORT_ID_1 = response.id();

        assertThat(reportService.getReportById(REPORT_ID_1)).isPresent();
    }

    @Test
    @Transactional
    @Rollback(false)
    @Order(3)
    void givenSingleReportInDB_whenAccessingThroughDifferentMethods_thenFound() {
        var report = reportService.getReportById(REPORT_ID_1).orElseThrow();

        assertThat(reportService.getAllReports().get()).asList().containsOnly(report);
        assertThat(reportService.getAllReportsByPlacement("placement").get()).asList().containsOnly(report);
        assertThat(reportService.getAllReportsByCategory("category").get()).asList().containsOnly(report);
        assertThat(reportService.getAllReportsByDateWindows(null, null).get()).asList().containsOnly(report);

        assertThat(reportService.getAllReportsByDateWindows(FAILURE_DATE.minusHours(1), null).get()).asList()
            .containsOnly(report);
        assertThat(reportService.getAllReportsByDateWindows(null, FAILURE_DATE.plusHours(1)).get()).asList()
            .containsOnly(report);
        assertThat(reportService.getAllReportsByDateWindows(FAILURE_DATE.minusHours(1), FAILURE_DATE.plusHours(1))
            .get()).asList().containsOnly(report);
        assertThat(reportService.getAllReportsByDateWindows(FAILURE_DATE.plusHours(1), null).get()).asList().isEmpty();

        assertThat(reportService.getAllReportsByOwnerEmail("email").get()).asList().containsOnly(report);
    }

    @Test
    @Transactional
    @Rollback(false)
    @Order(4)
    void givenReport_turnSomeFields_thenCorrectlyTurned() {
        var entity = reportService.getReportById(REPORT_ID_1).orElseThrow();
        boolean resolvedBefore = entity.isResolvedByUser();
        boolean confirmedBefore = entity.isConfirmedByAnalysis();

        boolean isUpdatedResolveField = reportService.resolveReportByUser(REPORT_ID_1);
        boolean isUpdatedConfirmedBefore = reportService.confirmReportByAnalysis(REPORT_ID_1);
        var updatedEntity = reportService.getReportById(REPORT_ID_1).orElseThrow();

        assertThat(isUpdatedResolveField && isUpdatedConfirmedBefore).isTrue();
        assertThat(updatedEntity.isResolvedByUser()).isNotEqualTo(resolvedBefore);
        assertThat(updatedEntity.isConfirmedByAnalysis()).isNotEqualTo(confirmedBefore);
    }

    @Test
    @Transactional
    @Rollback(false)
    @Order(5)
    void givenDBWithOneReport_whenAddingForcefullyReport_thenCorrectlyAdded() {
        PostProcessReportRequest request = new PostProcessReportRequest(
            "category_1", "placement_1", FAILURE_DATE,
            "email_1", "description_1"
        );

        var response = reportService.processReportForcefully(request).orElseThrow();
        REPORT_ID_2 = response.id();

        assertThat(reportService.getReportById(REPORT_ID_2)).isPresent();
    }

    @Test
    @Transactional
    @Rollback(false)
    @Order(6)
    void givenTwoDistinctReportsInDB_whenAccessingThroughDifferentMethods_thenFound() {
        var report_1 = reportService.getReportById(REPORT_ID_1).orElseThrow();
        var report_2 = reportService.getReportById(REPORT_ID_2).orElseThrow();

        assertThat(reportService.getAllReports().get()).asList().containsOnly(report_1, report_2);
        assertThat(reportService.getAllReportsByPlacement("plac").get()).asList().containsOnly(report_1, report_2);
        assertThat(reportService.getAllReportsByDateWindows(null, null).get()).asList().containsOnly(report_1, report_2);
        assertThat(reportService.getAllReportsByCategory("category").get()).asList().containsOnly(report_1);

        assertThat(reportService.getAllReportsByDateWindows(FAILURE_DATE.minusHours(1), null).get()).asList()
            .containsOnly(report_1, report_2);
        assertThat(reportService.getAllReportsByDateWindows(null, FAILURE_DATE.plusHours(1)).get()).asList()
            .containsOnly(report_1, report_2);
        assertThat(reportService.getAllReportsByDateWindows(FAILURE_DATE.minusHours(1), FAILURE_DATE.plusHours(1))
            .get()).asList().containsOnly(report_1, report_2);
        assertThat(reportService.getAllReportsByDateWindows(FAILURE_DATE.plusHours(1), null).get()).asList().isEmpty();

        assertThat(reportService.getAllReportsByOwnerEmail("email").get()).asList().containsOnly(report_1);
    }

    @Test
    @Transactional
    @Rollback(false)
    @Order(7)
    void givenAdminReport_turnSomeFields_thenCorrectlyTurned() {
        var entity = reportService.getReportById(REPORT_ID_2).orElseThrow();
        boolean resolvedBefore = entity.isResolvedByAdmin();
        boolean confirmedBefore = entity.isConfirmedByAdmin();

        boolean isUpdatedResolveField = reportService.resolveReportByUser(REPORT_ID_2);
        boolean isUpdatedConfirmedBefore = reportService.confirmReportByAnalysis(REPORT_ID_2);
        var updatedEntity = reportService.getReportById(REPORT_ID_2).orElseThrow();

        assertThat(isUpdatedResolveField && isUpdatedConfirmedBefore).isTrue();
        assertThat(updatedEntity.isResolvedByUser()).isNotEqualTo(resolvedBefore);
        assertThat(updatedEntity.isConfirmedByAnalysis()).isEqualTo(confirmedBefore);
    }

    @Test
    @Transactional
    @Rollback(false)
    @Order(8)
    void givenDBWithTwoReports_whenDeleteBoth_thenOk() {
        assertThat(reportService.deleteReport(REPORT_ID_1)).isTrue();
        assertThat(reportService.deleteReport(REPORT_ID_2)).isTrue();

        assertThatExceptionOfType(NotFoundException.class)
            .isThrownBy(() -> reportService.deleteReport(REPORT_ID_1));
        assertThatExceptionOfType(NotFoundException.class)
            .isThrownBy(() -> reportService.deleteReport(REPORT_ID_2));
    }

    @Test
    @Transactional
    @Rollback(false)
    @Order(9)
    void givenEmptyDBAfterDeletion_whenTryingToFetchAny_thenNothing() {
        assertThat(reportService.getReportById(1L)).isEmpty();
        assertThat(reportService.getAllReports().get()).asList().isEmpty();
        assertThat(reportService.getAllReportsByPlacement("placement").get()).asList().isEmpty();
        assertThat(reportService.getAllReportsByCategory("category").get()).asList().isEmpty();
        assertThat(reportService.getAllReportsByDateWindows(null, null).get()).asList().isEmpty();
        assertThat(reportService.getAllReportsByOwnerEmail("email").get()).asList().isEmpty();
    }
}
