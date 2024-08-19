CREATE TABLE audit (
    created_at TIMESTAMP,
    updated_at TIMESTAMP
    -- ,created_by BIGINT NOT NULL,
    -- updated_by BIGINT,
    -- CONSTRAINT fk_created_by FOREIGN KEY (created_by) REFERENCES users(id),
    -- CONSTRAINT fk_updated_by FOREIGN KEY (updated_by) REFERENCES users(id)
);
-- after app has populated fields make it non null
ALTER TABLE audit
ALTER COLUMN created_at SET NOT NULL,
ALTER COLUMN updated_at SET NOT NULL;

drop table audit;