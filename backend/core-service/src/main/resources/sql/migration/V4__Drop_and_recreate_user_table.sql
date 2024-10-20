-- V4__Drop_and_recreate_user_table.sql

-- Drop the existing "user" table if it exists
DROP TABLE IF EXISTS "user" CASCADE;

-- Recreate the "user" table with the new columns
CREATE TABLE "user" (
                        id UUID DEFAULT gen_random_uuid() PRIMARY KEY,              -- Auto-incrementing ID column
                        first_name VARCHAR(50) NOT NULL,    -- First name column
                        last_name VARCHAR(50) NOT NULL,     -- Last name column
                        email VARCHAR(100) UNIQUE NOT NULL, -- Email column, must be unique
                        is_registered BOOLEAN DEFAULT false, -- isRegistered column, defaults to false
                        enabled BOOLEAN DEFAULT true        -- enabled column, defaults to true
);

-- Insert new "admin" and "user" records into the newly created table
INSERT INTO "user" (first_name, last_name, email, is_registered, enabled) VALUES
                       ('admin', 'admin', 'admin@email.com', true, true),
                       ('user', 'user', 'user@email.com', true, true);
