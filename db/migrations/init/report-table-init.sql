--liquibase formatted sql

-- changeset catorleader:1
CREATE TABLE IF NOT EXISTS report(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    category varchar(128) NOT NULL,
    placement varchar(128) NOT NULL,
    failure_date TIMESTAMP WITH TIME ZONE,
    proceeded_date TIMESTAMP WITH TIME ZONE,
    is_confirmed BOOLEAN NOT NULL,
    is_resolved BOOLEAN NOT NULL
);
-- rollback DROP TABLE IF EXISTS report;
