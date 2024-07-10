package com.java.configuration;

import com.java.domain.repository.ReportRepository;
import com.java.domain.service.ReportService;
import com.java.domain.service.jpa.JpaReportService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JpaDatabaseAccessConfiguration {
    @Bean
    public ReportService reportService(@NotNull ReportRepository reportRepository) {
        return new JpaReportService(reportRepository);
    }
}
