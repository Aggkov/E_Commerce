-- Add shipping and billing address to orders table
ALTER TABLE orders
    ADD COLUMN shipping_address_id UUID,
    ADD COLUMN billing_address_id UUID;

-- Add foreign key constraints
ALTER TABLE orders
    ADD CONSTRAINT FK_shipping_address_id_orders FOREIGN KEY (shipping_address_id) REFERENCES shipping_address (id),
    ADD CONSTRAINT FK_billing_address_id_orders FOREIGN KEY (billing_address_id) REFERENCES billing_address (id);
