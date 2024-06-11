package com.java.domain.model;

import com.java.domain.model.converter.DateTimeConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "report")
@NoArgsConstructor
@Getter
@Setter
public class Report {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String placement;

    @Convert(converter = DateTimeConverter.class)
    private OffsetDateTime failureDate;

    @Convert(converter = DateTimeConverter.class)
    private OffsetDateTime proceededDate;

    @Column(nullable = false)
    private boolean isConfirmed;

    @Column(nullable = false)
    private boolean isResolved;

    public Report(
        String category,
        String placement,
        OffsetDateTime failureDate,
        OffsetDateTime proceededDate,
        boolean isConfirmed,
        boolean isResolved
    ) {
        this.category = category;
        this.placement = placement;
        this.failureDate = failureDate;
        this.proceededDate = proceededDate;
        this.isConfirmed = isConfirmed;
        this.isResolved = isResolved;
    }
}
