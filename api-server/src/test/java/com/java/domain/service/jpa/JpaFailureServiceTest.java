package com.java.domain.service.jpa;

import com.java.IntegrationEnvironment;
import com.java.domain.model.Failure;
import com.java.domain.repository.FailureRepository;
import com.java.domain.service.FailureService;
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

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JpaFailureServiceTest extends IntegrationEnvironment {
    private static final OffsetDateTime FAILURE_DATE =
        OffsetDateTime.of(2024, 6, 10,
            2, 0, 0, 0, ZoneOffset.ofHours(3)
        );

    @Autowired
    private FailureRepository failureRepository;

    @Autowired
    private FailureService failureService;

    @Test
    @Transactional
    @Rollback(false)
    @Order(1)
    void givenEmptyDB_whenTryingToFetchAny_thenNothing() {
        assertThat(failureService.getAllFailures().get()).asList().isEmpty();
        assertThat(failureService.getAllFailuresByPlacement("placement").get()).asList().isEmpty();
        assertThat(failureService.getAllFailuresByCategory("category").get()).asList().isEmpty();
        assertThat(failureService.getAllFailuresByDateWindows(null, null).get()).asList().isEmpty();
    }

    @Test
    @Transactional
    @Rollback(false)
    @Order(2)
    void givenSingleReportInDB_whenAccessingThroughDifferentMethods_thenFound() {
        var failure = new Failure();
        failure.setPlacement("placement");
        failure.setCategory("category");
        failure.setFailureDate(FAILURE_DATE);
        failure.setReportCount(1);
        failure.setAggregatedReportMessages("messages");
        failure.setSummarization("summarization");
        var entity = failureRepository.save(failure);
        failureRepository.flush();

        assertThat(failureService.getAllFailures().get()).asList().containsOnly(entity);
        assertThat(failureService.getAllFailuresByPlacement("placement").get()).asList().containsOnly(entity);
        assertThat(failureService.getAllFailuresByCategory("category").get()).asList().containsOnly(entity);
        assertThat(failureService.getAllFailuresByDateWindows(null, null).get()).asList().containsOnly(entity);

        assertThat(failureService.getAllFailuresByDateWindows(FAILURE_DATE.minusHours(1), null).get()).asList()
            .containsOnly(entity);
        assertThat(failureService.getAllFailuresByDateWindows(null, FAILURE_DATE.plusHours(1)).get()).asList()
            .containsOnly(entity);
        assertThat(failureService.getAllFailuresByDateWindows(FAILURE_DATE.minusHours(1), FAILURE_DATE.plusHours(1))
            .get()).asList().containsOnly(entity);
        assertThat(failureService.getAllFailuresByDateWindows(FAILURE_DATE.plusHours(1), null).get()).asList()
            .isEmpty();
    }

    @Test
    @Transactional
    @Rollback(false)
    @Order(3)
    void givenTwoDistinctReportsInDB_whenAccessingThroughDifferentMethods_thenFound() {
        var failure_1 = failureService.getAllFailures().orElseThrow().getFirst();
        var failure = new Failure();
        failure.setPlacement("placement_1");
        failure.setCategory("category_1");
        failure.setFailureDate(FAILURE_DATE);
        failure.setReportCount(2);
        failure.setAggregatedReportMessages("messages_1");
        failure.setSummarization("summarization_1");
        var failure_2 = failureRepository.save(failure);
        failureRepository.flush();

        assertThat(failureService.getAllFailures().get()).asList().containsOnly(failure_1, failure_2);
        assertThat(failureService.getAllFailuresByPlacement("plac").get()).asList().containsOnly(failure_1, failure_2);
        assertThat(failureService.getAllFailuresByDateWindows(null, null).get()).asList()
            .containsOnly(failure_1, failure_2);
        assertThat(failureService.getAllFailuresByCategory("category").get()).asList().containsOnly(failure_1);

        assertThat(failureService.getAllFailuresByDateWindows(FAILURE_DATE.minusHours(1), null).get()).asList()
            .containsOnly(failure_1, failure_2);
        assertThat(failureService.getAllFailuresByDateWindows(null, FAILURE_DATE.plusHours(1)).get()).asList()
            .containsOnly(failure_1, failure_2);
        assertThat(failureService.getAllFailuresByDateWindows(FAILURE_DATE.minusHours(1), FAILURE_DATE.plusHours(1))
            .get()).asList().containsOnly(failure_1, failure_2);
        assertThat(failureService.getAllFailuresByDateWindows(FAILURE_DATE.plusHours(1), null).get()).asList()
            .isEmpty();
    }

    @Test
    @Transactional
    @Rollback(false)
    @Order(4)
    void givenFullDB_whenDeleteAllReportOneByOne_thenEmptyDB() {
        var failures = failureService.getAllFailures().orElseThrow();

        var isDeletedList = failures.stream()
            .map(failure -> failureService.deleteFailure(failure.getId()))
            .toList();

        assertThat(isDeletedList).asList().containsOnly(true);
        assertThat(failureService.getAllFailures().get()).asList().isEmpty();
        assertThat(failureService.getAllFailuresByPlacement("placement").get()).asList().isEmpty();
        assertThat(failureService.getAllFailuresByCategory("category").get()).asList().isEmpty();
        assertThat(failureService.getAllFailuresByDateWindows(null, null).get()).asList().isEmpty();
    }
}
