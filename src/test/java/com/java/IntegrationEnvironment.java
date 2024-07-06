package com.java;

import jakarta.validation.constraints.NotNull;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.DirectoryResourceAccessor;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class IntegrationEnvironment {
    public static final PostgreSQLContainer<?> POSTGRES;

    static {
        POSTGRES = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("monidorm")
            .withUsername("postgres")
            .withPassword("postgres");
        POSTGRES.start();

        runMigrations(POSTGRES);
    }

    private static void runMigrations(@NotNull JdbcDatabaseContainer<?> c) {
        try (Connection connection = DriverManager.getConnection(c.getJdbcUrl(), c.getUsername(), c.getPassword())) {
            Database database =
                DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));

            var dir = new File("").toPath().toAbsolutePath().resolve(Path.of("db"));
            Liquibase liquibase = new Liquibase(
                Path.of("migrations", "db.master-changelog.xml").toString(),
                new DirectoryResourceAccessor(dir),
                database
            );

            liquibase.update(new Contexts(), new LabelExpression());
        } catch (SQLException | LiquibaseException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @DynamicPropertySource
    private static void dynamicPropertyConfiguration(DynamicPropertyRegistry registry) {
        registry.add("spring.liquibase.enabled", () -> "false");

        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }
}
