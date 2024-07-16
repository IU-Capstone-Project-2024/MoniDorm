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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "report")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id", "category", "placement"})
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

    private String ownerEmail;

    @Column(nullable = false)
    private boolean isConfirmedByAnalysis;

    @Column(nullable = false)
    private boolean isConfirmedByAdmin;

    @Column(nullable = false)
    private boolean isResolvedByUser;

    @Column(nullable = false)
    private boolean isResolvedByAdmin;

    public String description;

    @SuppressWarnings("checkstyle:ParameterNumber") public Report(
        String category,
        String placement,
        OffsetDateTime failureDate,
        String ownerEmail,
        OffsetDateTime proceededDate,
        boolean isConfirmedByAnalysis,
        boolean isConfirmedByAdmin,
        boolean isResolvedByUser,
        boolean isResolvedByAdmin,
        String description
    ) {
        this.category = category;
        this.placement = placement;
        this.failureDate = failureDate;
        this.ownerEmail = ownerEmail;
        this.proceededDate = proceededDate;
        this.isConfirmedByAnalysis = isConfirmedByAnalysis;
        this.isConfirmedByAdmin = isConfirmedByAdmin;
        this.isResolvedByUser = isResolvedByUser;
        this.isResolvedByAdmin = isResolvedByAdmin;
        this.description = description;
    }
}
