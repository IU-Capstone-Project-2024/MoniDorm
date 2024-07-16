package com.java.domain.repository;

import com.java.domain.model.Failure;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FailureRepository extends JpaRepository<Failure, Long> {
    @Query("from Failure f where f.placement like concat(:place, '%')")
    Optional<List<Failure>> findAllByPlacementLike(@Param("place") @NotBlank String placement);

    Optional<List<Failure>> findAllByCategory(@NotBlank String category);

    Optional<List<Failure>> findAllByFailureDateAfterAndFailureDateBefore(
        @NotNull OffsetDateTime startWindow,
        @NotNull OffsetDateTime endWindow
    );

    Optional<List<Failure>> findAllByFailureDateAfter(@NotNull OffsetDateTime startWindow);

    Optional<List<Failure>> findAllByFailureDateBefore(@NotNull OffsetDateTime endWindow);
}
