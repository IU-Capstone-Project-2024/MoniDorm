package com.java.domain.service;

import com.java.domain.model.Failure;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public interface FailureService {
    @NotNull Optional<List<Failure>> getAllFailures();

    @NotNull Optional<List<Failure>> getAllFailuresByPlacement(@NotBlank String placement);

    @NotNull Optional<List<Failure>> getAllFailuresByCategory(@NotBlank String category);

    @NotNull Optional<List<Failure>> getAllFailuresByDateWindows(OffsetDateTime startWindow, OffsetDateTime endWindow);

    boolean deleteFailure(@Min(0) long id);
}
