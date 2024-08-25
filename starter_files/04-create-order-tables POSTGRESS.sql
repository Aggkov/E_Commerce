-- No equivalent for USE in PostgreSQL; instead, connect to the database using \c full-stack-ecommerce or include the database name in your connection string.

--
-- Prep work
--
-- PostgreSQL does not support FOREIGN_KEY_CHECKS
DROP TABLE IF EXISTS order_item;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS address;

--
-- Table structure for table `address`
--
CREATE TABLE address (
  id SERIAL PRIMARY KEY,
  city VARCHAR(255),
  state VARCHAR(255),
  street VARCHAR(255),
  zip_code VARCHAR(255),
  state_id BIGINT,
	CONSTRAINT "FK_state_id" FOREIGN KEY ("state_id") REFERENCES "state" ("id"),

);

--
-- Table structure for table `customer`
--
CREATE TABLE customer (
  id SERIAL PRIMARY KEY,
  first_name VARCHAR(255),
  last_name VARCHAR(255),
  email VARCHAR(255),
  shipping_address_id BIGINT,
  billing_address_id BIGINT,
  UNIQUE (billing_address_id),
  UNIQUE (shipping_address_id),
  CONSTRAINT FK_billing_address_id FOREIGN KEY (billing_address_id) REFERENCES address (id),
  CONSTRAINT FK_shipping_address_id FOREIGN KEY (shipping_address_id) REFERENCES address (id)
);

--
-- Table structure for table `orders`
--
CREATE TABLE orders (
  id SERIAL PRIMARY KEY,
  order_tracking_number VARCHAR(255),
  total_price DECIMAL(19,2),
  total_quantity INT,
  customer_id BIGINT,
  status VARCHAR(128),
  created_at TIMESTAMP,
  updated_at TIMESTAMP,
  CONSTRAINT FK_customer_id FOREIGN KEY (customer_id) REFERENCES customer (id)
);

--
-- Table structure for table `order_items`
--
CREATE TABLE order_item (
  id SERIAL PRIMARY KEY,
  quantity INT,
  order_id BIGINT,
  product_id BIGINT,
  CONSTRAINT FK_order_id FOREIGN KEY (order_id) REFERENCES orders (id),
  CONSTRAINT FK_product_id FOREIGN KEY (product_id) REFERENCES product (id)
);

-- Note: No need to specify engine or charset, as PostgreSQL handles these differently from MySQL.
