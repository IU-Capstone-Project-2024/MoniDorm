package com.java.domain.service.jpa;

import com.java.api.exception.NotFoundException;
import com.java.domain.model.Failure;
import com.java.domain.repository.FailureRepository;
import com.java.domain.service.FailureService;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JpaFailureService implements FailureService {
    private static final String EXCEPTION_MESSAGE = "Report is not found";
    private final FailureRepository failureRepository;

    @Override
    public Optional<List<Failure>> getAllFailures() {
        return Optional.of(failureRepository.findAll());
    }

    @Override
    public Optional<List<Failure>> getAllFailuresByPlacement(String placement) {
        return failureRepository.findAllByPlacementLike(placement);
    }

    @Override
    public Optional<List<Failure>> getAllFailuresByCategory(String category) {
        return failureRepository.findAllByCategory(category);
    }

    @Override
    public Optional<List<Failure>> getAllFailuresByDateWindows(OffsetDateTime startWindow, OffsetDateTime endWindow) {
        if (startWindow != null || endWindow != null) {
            if (startWindow != null && endWindow != null) {
                return failureRepository.findAllByFailureDateAfterAndFailureDateBefore(startWindow, endWindow);
            }

            if (startWindow != null) {
                return failureRepository.findAllByFailureDateAfter(startWindow);
            }

            return failureRepository.findAllByFailureDateBefore(endWindow);
        } else {
            return Optional.of(failureRepository.findAll());
        }
    }

    @Override
    public boolean deleteFailure(long id) {
        var entity = failureRepository.findById(id).orElseThrow(() -> new NotFoundException(EXCEPTION_MESSAGE));
        failureRepository.delete(entity);
        failureRepository.flush();

        return true;
    }
}
