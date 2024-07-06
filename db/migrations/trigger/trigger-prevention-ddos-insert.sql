--liquibase formatted sql

-- changeset catorleader:2
CREATE OR REPLACE FUNCTION prevent_duplicate_reports() RETURNS TRIGGER AS '
DECLARE
    N_minutes INTERVAL := ''15 minutes'';
    existing_report_count INTEGER;
BEGIN
    SELECT COUNT(*)
    INTO existing_report_count
    FROM report
    WHERE owner_email = NEW.owner_email
      AND placement = NEW.placement
      AND category = NEW.category
      AND failure_date >= (NEW.failure_date - N_minutes);

    IF existing_report_count > 0 THEN
        RAISE NOTICE ''Duplicate report detected. Insertion prevented.'';
        RETURN NULL;
    ELSE
        RETURN NEW;
    END IF;
END;
' LANGUAGE plpgsql;

CREATE TRIGGER check_duplicate_report
BEFORE INSERT ON report
FOR EACH ROW
EXECUTE FUNCTION prevent_duplicate_reports();
-- rollback DROP TRIGGER IF EXISTS check_duplicate_report
