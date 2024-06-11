package com.java.domain.repository;

import com.java.domain.model.Report;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReportRepository extends JpaRepository<Report, Long> {
    @Query("from Report r where r.placement like concat(:place, '%')")
    Optional<List<Report>> findAllByPlacementLike(@Param("place") @NotBlank String placement);

    Optional<List<Report>> findAllByCategory(@NotBlank String category);

    Optional<List<Report>> findAllByProceededDateBeforeAndProceededDateAfter(
        @NotNull OffsetDateTime startWindow,
        @NotNull OffsetDateTime endWindow
    );
}
