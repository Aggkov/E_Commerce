-- Add the isRegistered and enabled columns to the "user" table
ALTER TABLE "user"
    ADD COLUMN isRegistered BOOLEAN DEFAULT false,
ADD COLUMN enabled BOOLEAN DEFAULT true;

-- Insert new records with values for the new columns
INSERT INTO "user" (first_name, last_name, email, isRegistered, enabled) VALUES
                   ('admin', 'admin', 'admin@example.com', true, true),
                   ('user', 'user', 'user@email.com', true, true);
