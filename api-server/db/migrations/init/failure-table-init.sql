--liquibase formatted sql

-- changeset catorleader:2
CREATE TABLE IF NOT EXISTS failure(
 id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
 category varchar(128) NOT NULL,
 placement varchar(128) NOT NULL,
 failure_date TIMESTAMP WITH TIME ZONE,
 report_count int,
 aggregated_report_messages text,
 summarization varchar(1024)
);
-- rollback DROP TABLE IF EXISTS failure;
