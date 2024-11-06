-- Enable the pgcrypto extension for UUID generation
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE payment (
    payment_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    paypal_order_id VARCHAR(255) NOT NULL,
    order_tracking_number VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    currency_code VARCHAR(10) NOT NULL,
    amount DECIMAL(18, 2) NOT NULL,
    create_time TIMESTAMP DEFAULT clock_timestamp(),
    update_time TIMESTAMP DEFAULT NULL
);