--liquibase formatted sql

-- changeset catorleader:1
CREATE TABLE IF NOT EXISTS report(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    category varchar(128) NOT NULL,
    placement varchar(128) NOT NULL,
    failure_date TIMESTAMP WITH TIME ZONE,
    proceeded_date TIMESTAMP WITH TIME ZONE,
    owner_email varchar(128),
    is_confirmed_by_analysis BOOLEAN NOT NULL,
    is_confirmed_by_admin BOOLEAN NOT NULL,
    is_resolved_by_user BOOLEAN NOT NULL,
    is_resolved_by_admin BOOLEAN NOT NULL,
    description varchar(256)
);
-- rollback DROP TABLE IF EXISTS report;
