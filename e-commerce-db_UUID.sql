DROP TABLE IF EXISTS product CASCADE;
DROP TABLE IF EXISTS product_category CASCADE;
DROP TABLE IF EXISTS country CASCADE;
DROP TABLE IF EXISTS state CASCADE;
DROP TABLE IF EXISTS order_item CASCADE;
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS "user" CASCADE;
DROP TABLE IF EXISTS shipping_address CASCADE;
DROP TABLE IF EXISTS billing_address CASCADE;
DROP TABLE IF EXISTS user_shipping_address CASCADE;
DROP TABLE IF EXISTS user_billing_address CASCADE;



-- Enable the pgcrypto extension for UUID generation
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-----------------------------------------------------
-- Table e-commerce.product_category
-----------------------------------------------------
CREATE TABLE IF NOT EXISTS product_category (
  "id" UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  "category_name" VARCHAR(255) DEFAULT NULL
);
ALTER TABLE product_category OWNER TO ecommerce_user;

-- -----------------------------------------------------
-- Table e-commerce.product
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS product (
  "id" UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  "sku" VARCHAR(255) DEFAULT NULL,
  "name" VARCHAR(255) DEFAULT NULL,
  "description" VARCHAR(255) DEFAULT NULL,
  "unit_price" DECIMAL(13, 2) DEFAULT NULL,
  "image_url" VARCHAR(255) DEFAULT NULL,
  "active" BOOLEAN DEFAULT TRUE,
  "units_in_stock" INTEGER DEFAULT NULL,
  "units_sold" INTEGER DEFAULT NULL,
  "category_id" UUID NOT NULL,
  "created_at" TIMESTAMP DEFAULT clock_timestamp(),  -- New audit field
  "updated_at" TIMESTAMP DEFAULT NULL,  -- New audit field
  CONSTRAINT FK_product_category_id FOREIGN KEY (category_id) REFERENCES product_category (id)
); 
ALTER TABLE product OWNER TO ecommerce_user;

CREATE TABLE "country" (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  code varchar(2) DEFAULT NULL,
  name varchar(255) DEFAULT NULL
);
ALTER TABLE country OWNER TO ecommerce_user;

-- Table structure for table `state`
CREATE TABLE "state" (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  name VARCHAR(255),
  country_id UUID,
  CONSTRAINT FK_country_id FOREIGN KEY (country_id) REFERENCES country (id)
);
ALTER TABLE state OWNER TO ecommerce_user;

CREATE TABLE shipping_address (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  city VARCHAR(255),
  street VARCHAR(255),
  zip_code VARCHAR(255),
  state_id UUID,
  CONSTRAINT FK_state_id FOREIGN KEY (state_id) REFERENCES state (id)
);
ALTER TABLE shipping_address OWNER TO ecommerce_user;

CREATE TABLE billing_address (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  city VARCHAR(255),
  street VARCHAR(255),
  zip_code VARCHAR(255),
  state_id UUID,
  CONSTRAINT FK_state_id FOREIGN KEY (state_id) REFERENCES state (id)
);
ALTER TABLE billing_address OWNER TO ecommerce_user;

-- Step 2: Create the `customer` table
CREATE TABLE "user" (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  first_name VARCHAR(255),
  last_name VARCHAR(255),
  email VARCHAR(255)
);
ALTER TABLE "user" OWNER TO ecommerce_user;

-- Step 3: Create the `customer_shipping_address` junction table
CREATE TABLE user_shipping_address (
  user_id UUID,
  shipping_address_id UUID,
  PRIMARY KEY (user_id, shipping_address_id),
  CONSTRAINT FK_user_id_shipping FOREIGN KEY (user_id) REFERENCES "user" (id),
  CONSTRAINT FK_shipping_address_id_shipping FOREIGN KEY (shipping_address_id) REFERENCES shipping_address (id)
);
ALTER TABLE user_shipping_address OWNER TO ecommerce_user;

-- Step 4: Create the `customer_billing_address` junction table
CREATE TABLE user_billing_address (
  user_id UUID,
  billing_address_id UUID,
  PRIMARY KEY (user_id, billing_address_id),
  CONSTRAINT FK_user_id_billing FOREIGN KEY (user_id) REFERENCES "user" (id),
  CONSTRAINT FK_billing_address_id_billing FOREIGN KEY (billing_address_id) REFERENCES billing_address (id)
);
ALTER TABLE user_billing_address OWNER TO ecommerce_user;

-- Table structure for table `orders`
CREATE TABLE orders (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  order_tracking_number VARCHAR(255),
  total_price DECIMAL(19,2),
  total_quantity INT,
  user_id UUID,
  status VARCHAR(128),
  created_at TIMESTAMP,
  updated_at TIMESTAMP,
  CONSTRAINT orders FOREIGN KEY (user_id) REFERENCES "user" (id)
);
ALTER TABLE orders OWNER TO ecommerce_user;

-- Table structure for table `order_items`
CREATE TABLE order_item (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  quantity INT,
  order_id UUID,
  product_id UUID,
  CONSTRAINT FK_order_id FOREIGN KEY (order_id) REFERENCES orders (id),
  CONSTRAINT FK_product_id FOREIGN KEY (product_id) REFERENCES product (id)
);
ALTER TABLE order_item OWNER TO ecommerce_user;

-- -----------------------------------------------------
-- Categories
-- -----------------------------------------------------
INSERT INTO product_category(id, category_name) VALUES ('e302d1b4-8609-417d-9f5a-ceb1fb1a9331', 'Books');
INSERT INTO product_category(id, category_name) VALUES ('e74d708e-b0c7-42b1-bb7d-b42f7b2b0b32','Coffee Mugs');
INSERT INTO product_category(id, category_name) VALUES ('b26be512-510f-4064-bf9c-32d525fe76aa', 'Mouse Pads');
INSERT INTO product_category(id, category_name) VALUES ('8e9f6773-c946-462d-afc5-69ffac311b10', 'Luggage Tags');


INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('BOOK-TECH-1000', 'Crash Course in Python', 'Learn Python at your own pace. The author explains how the technology works in easy-to-understand language. This book includes working examples that you can apply to your own projects. Purchase the book and get started today!', 'uploads/images/books/book-1000.png', TRUE, 100, 0, 14.99, 'e302d1b4-8609-417d-9f5a-ceb1fb1a9331');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('BOOK-TECH-1001', 'Become a Guru in JavaScript', 'Learn JavaScript at your own pace. The author explains how the technology works in easy-to-understand language. This book includes working examples that you can apply to your own projects. Purchase the book and get started today!', 'uploads/images/books/book-1001.png', TRUE, 100, 0, 20.99, 'e302d1b4-8609-417d-9f5a-ceb1fb1a9331');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('BOOK-TECH-1002', 'Exploring Vue.js', 'Learn Vue.js at your own pace. The author explains how the technology works in easy-to-understand language. This book includes working examples that you can apply to your own projects. Purchase the book and get started today!', 'uploads/images/books/book-1002.png', TRUE, 100, 0, 14.99, 'e302d1b4-8609-417d-9f5a-ceb1fb1a9331');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('BOOK-TECH-1003', 'Advanced Techniques in Big Data', 'Learn Big Data at your own pace. The author explains how the technology works in easy-to-understand language. This book includes working examples that you can apply to your own projects. Purchase the book and get started today!', 'uploads/images/books/book-1003.png', TRUE, 100, 0, 13.99, 'e302d1b4-8609-417d-9f5a-ceb1fb1a9331');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('BOOK-TECH-1004', 'Crash Course in Big Data', 'Learn Big Data at your own pace. The author explains how the technology works in easy-to-understand language. This book includes working examples that you can apply to your own projects. Purchase the book and get started today!', 'uploads/images/books/book-1004.png', TRUE, 100, 0, 18.99, 'e302d1b4-8609-417d-9f5a-ceb1fb1a9331');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('BOOK-TECH-1005', 'JavaScript Cookbook', 'Learn JavaScript at your own pace. The author explains how the technology works in easy-to-understand language. This book includes working examples that you can apply to your own projects. Purchase the book and get started today!', 'uploads/images/books/book-1005.png', TRUE, 100, 0, 23.99, 'e302d1b4-8609-417d-9f5a-ceb1fb1a9331');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('BOOK-TECH-1006', 'Beginners Guide to SQL', 'Learn SQL at your own pace. The author explains how the technology works in easy-to-understand language. This book includes working examples that you can apply to your own projects. Purchase the book and get started today!', 'uploads/images/books/book-1006.png', TRUE, 100, 0, 14.99, 'e302d1b4-8609-417d-9f5a-ceb1fb1a9331');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('BOOK-TECH-1007', 'Advanced Techniques in JavaScript', 'Learn JavaScript at your own pace. The author explains how the technology works in easy-to-understand language. This book includes working examples that you can apply to your own projects. Purchase the book and get started today!', 'uploads/images/books/book-1007.png', TRUE, 100, 0, 16.99, 'e302d1b4-8609-417d-9f5a-ceb1fb1a9331');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('BOOK-TECH-1008', 'Introduction to Spring Boot', 'Learn Spring Boot at your own pace. The author explains how the technology works in easy-to-understand language. This book includes working examples that you can apply to your own projects. Purchase the book and get started today!', 'uploads/images/books/book-1008.png', TRUE, 100, 0, 25.99, 'e302d1b4-8609-417d-9f5a-ceb1fb1a9331');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('BOOK-TECH-1009', 'Become a Guru in React.js', 'Learn React.js at your own pace. The author explains how the technology works in easy-to-understand language. This book includes working examples that you can apply to your own projects. Purchase the book and get started today!', 'uploads/images/books/book-1009.png', TRUE, 100, 0, 23.99, 'e302d1b4-8609-417d-9f5a-ceb1fb1a9331');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('BOOK-TECH-1010', 'Beginners Guide to Data Science', 'Learn Data Science at your own pace. The author explains how the technology works in easy-to-understand language. This book includes working examples that you can apply to your own projects. Purchase the book and get started today!', 'uploads/images/books/book-1010.png', TRUE, 100, 0, 24.99, 'e302d1b4-8609-417d-9f5a-ceb1fb1a9331');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('BOOK-TECH-1011', 'Advanced Techniques in Java', 'Learn Java at your own pace. The author explains how the technology works in easy-to-understand language. This book includes working examples that you can apply to your own projects. Purchase the book and get started today!', 'uploads/images/books/book-1011.png', TRUE, 100, 0, 19.99, 'e302d1b4-8609-417d-9f5a-ceb1fb1a9331');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('BOOK-TECH-1012', 'Exploring DevOps', 'Learn DevOps at your own pace. The author explains how the technology works in easy-to-understand language. This book includes working examples that you can apply to your own projects. Purchase the book and get started today!', 'uploads/images/books/book-1012.png', TRUE, 100, 0, 24.99, 'e302d1b4-8609-417d-9f5a-ceb1fb1a9331');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('BOOK-TECH-1013', 'The Expert Guide to SQL', 'Learn SQL at your own pace. The author explains how the technology works in easy-to-understand language. This book includes working examples that you can apply to your own projects. Purchase the book and get started today!', 'uploads/images/books/book-1013.png', TRUE, 100, 0, 19.99, 'e302d1b4-8609-417d-9f5a-ceb1fb1a9331');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('BOOK-TECH-1014', 'Introduction to SQL', 'Learn SQL at your own pace. The author explains how the technology works in easy-to-understand language. This book includes working examples that you can apply to your own projects. Purchase the book and get started today!', 'uploads/images/books/book-1014.png', TRUE, 100, 0, 22.99, 'e302d1b4-8609-417d-9f5a-ceb1fb1a9331');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('BOOK-TECH-1015', 'The Expert Guide to JavaScript', 'Learn JavaScript at your own pace. The author explains how the technology works in easy-to-understand language. This book includes working examples that you can apply to your own projects. Purchase the book and get started today!', 'uploads/images/books/book-1015.png', TRUE, 100, 0, 22.99, 'e302d1b4-8609-417d-9f5a-ceb1fb1a9331');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('BOOK-TECH-1016', 'Exploring React.js', 'Learn React.js at your own pace. The author explains how the technology works in easy-to-understand language. This book includes working examples that you can apply to your own projects. Purchase the book and get started today!', 'uploads/images/books/book-1016.png', TRUE, 100, 0, 27.99, 'e302d1b4-8609-417d-9f5a-ceb1fb1a9331');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('BOOK-TECH-1017', 'Advanced Techniques in React.js', 'Learn React.js at your own pace. The author explains how the technology works in easy-to-understand language. This book includes working examples that you can apply to your own projects. Purchase the book and get started today!', 'uploads/images/books/book-1017.png', TRUE, 100, 0, 13.99, 'e302d1b4-8609-417d-9f5a-ceb1fb1a9331');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('BOOK-TECH-1018', 'Introduction to C#', 'Learn C# at your own pace. The author explains how the technology works in easy-to-understand language. This book includes working examples that you can apply to your own projects. Purchase the book and get started today!', 'uploads/images/books/book-1018.png', TRUE, 100, 0, 26.99, 'e302d1b4-8609-417d-9f5a-ceb1fb1a9331');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('BOOK-TECH-1019', 'Crash Course in JavaScript', 'Learn JavaScript at your own pace. The author explains how the technology works in easy-to-understand language. This book includes working examples that you can apply to your own projects. Purchase the book and get started today!', 'uploads/images/books/book-1019.png', TRUE, 100, 0, 13.99, 'e302d1b4-8609-417d-9f5a-ceb1fb1a9331');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('BOOK-TECH-1020', 'Introduction to Machine Learning', 'Learn Machine Learning at your own pace. The author explains how the technology works in easy-to-understand language. This book includes working examples that you can apply to your own projects. Purchase the book and get started today!', 'uploads/images/books/book-1020.png', TRUE, 100, 0, 19.99, 'e302d1b4-8609-417d-9f5a-ceb1fb1a9331');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('BOOK-TECH-1021', 'Become a Guru in Java', 'Learn Java at your own pace. The author explains how the technology works in easy-to-understand language. This book includes working examples that you can apply to your own projects. Purchase the book and get started today!', 'uploads/images/books/book-1021.png', TRUE, 100, 0, 18.99, 'e302d1b4-8609-417d-9f5a-ceb1fb1a9331');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('BOOK-TECH-1022', 'Introduction to Python', 'Learn Python at your own pace. The author explains how the technology works in easy-to-understand language. This book includes working examples that you can apply to your own projects. Purchase the book and get started today!', 'uploads/images/books/book-1022.png', TRUE, 100, 0, 26.99, 'e302d1b4-8609-417d-9f5a-ceb1fb1a9331');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('BOOK-TECH-1023', 'Advanced Techniques in C#', 'Learn C# at your own pace. The author explains how the technology works in easy-to-understand language. This book includes working examples that you can apply to your own projects. Purchase the book and get started today!', 'uploads/images/books/book-1023.png', TRUE, 100, 0, 22.99, 'e302d1b4-8609-417d-9f5a-ceb1fb1a9331');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('BOOK-TECH-1024', 'The Expert Guide to Machine Learning', 'Learn Machine Learning at your own pace. The author explains how the technology works in easy-to-understand language. This book includes working examples that you can apply to your own projects. Purchase the book and get started today!', 'uploads/images/books/book-1024.png', TRUE, 100, 0, 16.99, 'e302d1b4-8609-417d-9f5a-ceb1fb1a9331');

-- -----------------------------------------------------
-- Coffee Mugs
-- -----------------------------------------------------
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('COFFEEMUG-1000', 'Coffee Mug - Express', 'Do you love mathematics? If so, then you need this elegant coffee mug with an amazing fractal design. You don’’t have to worry about boring coffee mugs anymore. This coffee mug will be the topic of conversation in the office, guaranteed! Buy it now!', 'uploads/images/coffeemugs/coffeemug-1000.png', TRUE, 100, 0, 18.99, 'e74d708e-b0c7-42b1-bb7d-b42f7b2b0b32');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('COFFEEMUG-1001', 'Coffee Mug - Cherokee', 'Do you love mathematics? If so, then you need this elegant coffee mug with an amazing fractal design. You don’’t have to worry about boring coffee mugs anymore. This coffee mug will be the topic of conversation in the office, guaranteed! Buy it now!', 'uploads/images/coffeemugs/coffeemug-1001.png', TRUE, 100, 0, 18.99, 'e74d708e-b0c7-42b1-bb7d-b42f7b2b0b32');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('COFFEEMUG-1002', 'Coffee Mug - Sweeper', 'Do you love mathematics? If so, then you need this elegant coffee mug with an amazing fractal design. You don’’t have to worry about boring coffee mugs anymore. This coffee mug will be the topic of conversation in the office, guaranteed! Buy it now!', 'uploads/images/coffeemugs/coffeemug-1002.png', TRUE, 100, 0, 18.99, 'e74d708e-b0c7-42b1-bb7d-b42f7b2b0b32');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('COFFEEMUG-1003', 'Coffee Mug - Aspire', 'Do you love mathematics? If so, then you need this elegant coffee mug with an amazing fractal design. You don’’t have to worry about boring coffee mugs anymore. This coffee mug will be the topic of conversation in the office, guaranteed! Buy it now!', 'uploads/images/coffeemugs/coffeemug-1003.png', TRUE, 100, 0, 18.99, 'e74d708e-b0c7-42b1-bb7d-b42f7b2b0b32');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('COFFEEMUG-1004', 'Coffee Mug - Dorian', 'Do you love mathematics? If so, then you need this elegant coffee mug with an amazing fractal design. You don’’t have to worry about boring coffee mugs anymore. This coffee mug will be the topic of conversation in the office, guaranteed! Buy it now!', 'uploads/images/coffeemugs/coffeemug-1004.png', TRUE, 100, 0, 18.99, 'e74d708e-b0c7-42b1-bb7d-b42f7b2b0b32');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('COFFEEMUG-1005', 'Coffee Mug - Columbia', 'Do you love mathematics? If so, then you need this elegant coffee mug with an amazing fractal design. You don’’t have to worry about boring coffee mugs anymore. This coffee mug will be the topic of conversation in the office, guaranteed! Buy it now!', 'uploads/images/coffeemugs/coffeemug-1005.png', TRUE, 100, 0, 18.99, 'e74d708e-b0c7-42b1-bb7d-b42f7b2b0b32');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('COFFEEMUG-1006', 'Coffee Mug - Worthing', 'Do you love mathematics? If so, then you need this elegant coffee mug with an amazing fractal design. You don’’t have to worry about boring coffee mugs anymore. This coffee mug will be the topic of conversation in the office, guaranteed! Buy it now!', 'uploads/images/coffeemugs/coffeemug-1006.png', TRUE, 100, 0, 18.99, 'e74d708e-b0c7-42b1-bb7d-b42f7b2b0b32');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('COFFEEMUG-1007', 'Coffee Mug - Oak Cliff', 'Do you love mathematics? If so, then you need this elegant coffee mug with an amazing fractal design. You don’’t have to worry about boring coffee mugs anymore. This coffee mug will be the topic of conversation in the office, guaranteed! Buy it now!', 'uploads/images/coffeemugs/coffeemug-1007.png', TRUE, 100, 0, 18.99, 'e74d708e-b0c7-42b1-bb7d-b42f7b2b0b32');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('COFFEEMUG-1008', 'Coffee Mug - Tachyon', 'Do you love mathematics? If so, then you need this elegant coffee mug with an amazing fractal design. You don’’t have to worry about boring coffee mugs anymore. This coffee mug will be the topic of conversation in the office, guaranteed! Buy it now!', 'uploads/images/coffeemugs/coffeemug-1008.png', TRUE, 100, 0, 18.99, 'e74d708e-b0c7-42b1-bb7d-b42f7b2b0b32');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('COFFEEMUG-1009', 'Coffee Mug - Pan', 'Do you love mathematics? If so, then you need this elegant coffee mug with an amazing fractal design. You don’’t have to worry about boring coffee mugs anymore. This coffee mug will be the topic of conversation in the office, guaranteed! Buy it now!', 'uploads/images/coffeemugs/coffeemug-1009.png', TRUE, 100, 0, 18.99, 'e74d708e-b0c7-42b1-bb7d-b42f7b2b0b32');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('COFFEEMUG-1010', 'Coffee Mug - Phase', 'Do you love mathematics? If so, then you need this elegant coffee mug with an amazing fractal design. You don’’t have to worry about boring coffee mugs anymore. This coffee mug will be the topic of conversation in the office, guaranteed! Buy it now!', 'uploads/images/coffeemugs/coffeemug-1010.png', TRUE, 100, 0, 18.99, 'e74d708e-b0c7-42b1-bb7d-b42f7b2b0b32');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('COFFEEMUG-1011', 'Coffee Mug - Falling', 'Do you love mathematics? If so, then you need this elegant coffee mug with an amazing fractal design. You don’’t have to worry about boring coffee mugs anymore. This coffee mug will be the topic of conversation in the office, guaranteed! Buy it now!', 'uploads/images/coffeemugs/coffeemug-1011.png', TRUE, 100, 0, 18.99, 'e74d708e-b0c7-42b1-bb7d-b42f7b2b0b32');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('COFFEEMUG-1012', 'Coffee Mug - Wispy', 'Do you love mathematics? If so, then you need this elegant coffee mug with an amazing fractal design. You don’’t have to worry about boring coffee mugs anymore. This coffee mug will be the topic of conversation in the office, guaranteed! Buy it now!', 'uploads/images/coffeemugs/coffeemug-1012.png', TRUE, 100, 0, 18.99, 'e74d708e-b0c7-42b1-bb7d-b42f7b2b0b32');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('COFFEEMUG-1013', 'Coffee Mug - Arlington', 'Do you love mathematics? If so, then you need this elegant coffee mug with an amazing fractal design. You don’’t have to worry about boring coffee mugs anymore. This coffee mug will be the topic of conversation in the office, guaranteed! Buy it now!', 'uploads/images/coffeemugs/coffeemug-1013.png', TRUE, 100, 0, 18.99, 'e74d708e-b0c7-42b1-bb7d-b42f7b2b0b32');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('COFFEEMUG-1014', 'Coffee Mug - Gazing', 'Do you love mathematics? If so, then you need this elegant coffee mug with an amazing fractal design. You don’’t have to worry about boring coffee mugs anymore. This coffee mug will be the topic of conversation in the office, guaranteed! Buy it now!', 'uploads/images/coffeemugs/coffeemug-1014.png', TRUE, 100, 0, 18.99, 'e74d708e-b0c7-42b1-bb7d-b42f7b2b0b32');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('COFFEEMUG-1015', 'Coffee Mug - Azura', 'Do you love mathematics? If so, then you need this elegant coffee mug with an amazing fractal design. You don’’t have to worry about boring coffee mugs anymore. This coffee mug will be the topic of conversation in the office, guaranteed! Buy it now!', 'uploads/images/coffeemugs/coffeemug-1015.png', TRUE, 100, 0, 18.99, 'e74d708e-b0c7-42b1-bb7d-b42f7b2b0b32');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('COFFEEMUG-1016', 'Coffee Mug - Quantum Leap', 'Do you love mathematics? If so, then you need this elegant coffee mug with an amazing fractal design. You don’’t have to worry about boring coffee mugs anymore. This coffee mug will be the topic of conversation in the office, guaranteed! Buy it now!', 'uploads/images/coffeemugs/coffeemug-1016.png', TRUE, 100, 0, 18.99, 'e74d708e-b0c7-42b1-bb7d-b42f7b2b0b32');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('COFFEEMUG-1017', 'Coffee Mug - Light Years', 'Do you love mathematics? If so, then you need this elegant coffee mug with an amazing fractal design. You don’’t have to worry about boring coffee mugs anymore. This coffee mug will be the topic of conversation in the office, guaranteed! Buy it now!', 'uploads/images/coffeemugs/coffeemug-1017.png', TRUE, 100, 0, 18.99, 'e74d708e-b0c7-42b1-bb7d-b42f7b2b0b32');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('COFFEEMUG-1018', 'Coffee Mug - Taylor', 'Do you love mathematics? If so, then you need this elegant coffee mug with an amazing fractal design. You don’’t have to worry about boring coffee mugs anymore. This coffee mug will be the topic of conversation in the office, guaranteed! Buy it now!', 'uploads/images/coffeemugs/coffeemug-1018.png', TRUE, 100, 0, 18.99, 'e74d708e-b0c7-42b1-bb7d-b42f7b2b0b32');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('COFFEEMUG-1019', 'Coffee Mug - Gracia', 'Do you love mathematics? If so, then you need this elegant coffee mug with an amazing fractal design. You don’’t have to worry about boring coffee mugs anymore. This coffee mug will be the topic of conversation in the office, guaranteed! Buy it now!', 'uploads/images/coffeemugs/coffeemug-1019.png', TRUE, 100, 0, 18.99, 'e74d708e-b0c7-42b1-bb7d-b42f7b2b0b32');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('COFFEEMUG-1020', 'Coffee Mug - Relax', 'Do you love mathematics? If so, then you need this elegant coffee mug with an amazing fractal design. You don’’t have to worry about boring coffee mugs anymore. This coffee mug will be the topic of conversation in the office, guaranteed! Buy it now!', 'uploads/images/coffeemugs/coffeemug-1020.png', TRUE, 100, 0, 18.99, 'e74d708e-b0c7-42b1-bb7d-b42f7b2b0b32');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('COFFEEMUG-1021', 'Coffee Mug - Windermere', 'Do you love mathematics? If so, then you need this elegant coffee mug with an amazing fractal design. You don’’t have to worry about boring coffee mugs anymore. This coffee mug will be the topic of conversation in the office, guaranteed! Buy it now!', 'uploads/images/coffeemugs/coffeemug-1021.png', TRUE, 100, 0, 18.99, 'e74d708e-b0c7-42b1-bb7d-b42f7b2b0b32');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('COFFEEMUG-1022', 'Coffee Mug - Prancer', 'Do you love mathematics? If so, then you need this elegant coffee mug with an amazing fractal design. You don’’t have to worry about boring coffee mugs anymore. This coffee mug will be the topic of conversation in the office, guaranteed! Buy it now!', 'uploads/images/coffeemugs/coffeemug-1022.png', TRUE, 100, 0, 18.99, 'e74d708e-b0c7-42b1-bb7d-b42f7b2b0b32');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('COFFEEMUG-1023', 'Coffee Mug - Recursion', 'Do you love mathematics? If so, then you need this elegant coffee mug with an amazing fractal design. You don’’t have to worry about boring coffee mugs anymore. This coffee mug will be the topic of conversation in the office, guaranteed! Buy it now!', 'uploads/images/coffeemugs/coffeemug-1023.png',TRUE, 100, 0, 18.99, 'e74d708e-b0c7-42b1-bb7d-b42f7b2b0b32');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('COFFEEMUG-1024', 'Coffee Mug - Treasure', 'Do you love mathematics? If so, then you need this elegant coffee mug with an amazing fractal design. You don’’t have to worry about boring coffee mugs anymore. This coffee mug will be the topic of conversation in the office, guaranteed! Buy it now!', 'uploads/images/coffeemugs/coffeemug-1024.png', TRUE, 100, 0, 18.99, 'e74d708e-b0c7-42b1-bb7d-b42f7b2b0b32');

-- -----------------------------------------------------

-- -----------------------------------------------------
-- Mouse Pads
-- -----------------------------------------------------
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('MOUSEPAD-1000', 'Mouse Pad - Express', 'Fractal images are amazing! You can now own a mouse pad with a unique and amazing fractal. The mouse pad is made of a durable and smooth material. Your mouse will easily glide across the mouse pad. This mouse pad will brighten your workspace. Buy it now!', 'uploads/images/mousepads/mousepad-1000.png', TRUE, 100, 0, 17.99, 'b26be512-510f-4064-bf9c-32d525fe76aa');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('MOUSEPAD-1001', 'Mouse Pad - Cherokee', 'Fractal images are amazing! You can now own a mouse pad with a unique and amazing fractal. The mouse pad is made of a durable and smooth material. Your mouse will easily glide across the mouse pad. This mouse pad will brighten your workspace. Buy it now!', 'uploads/images/mousepads/mousepad-1001.png', TRUE, 100, 0, 17.99, 'b26be512-510f-4064-bf9c-32d525fe76aa');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('MOUSEPAD-1002', 'Mouse Pad - Sweeper', 'Fractal images are amazing! You can now own a mouse pad with a unique and amazing fractal. The mouse pad is made of a durable and smooth material. Your mouse will easily glide across the mouse pad. This mouse pad will brighten your workspace. Buy it now!', 'uploads/images/mousepads/mousepad-1002.png', TRUE, 100, 0, 17.99, 'b26be512-510f-4064-bf9c-32d525fe76aa');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('MOUSEPAD-1003', 'Mouse Pad - Aspire', 'Fractal images are amazing! You can now own a mouse pad with a unique and amazing fractal. The mouse pad is made of a durable and smooth material. Your mouse will easily glide across the mouse pad. This mouse pad will brighten your workspace. Buy it now!', 'uploads/images/mousepads/mousepad-1003.png', TRUE, 100, 0, 17.99, 'b26be512-510f-4064-bf9c-32d525fe76aa');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('MOUSEPAD-1004', 'Mouse Pad - Dorian', 'Fractal images are amazing! You can now own a mouse pad with a unique and amazing fractal. The mouse pad is made of a durable and smooth material. Your mouse will easily glide across the mouse pad. This mouse pad will brighten your workspace. Buy it now!', 'uploads/images/mousepads/mousepad-1004.png', TRUE, 100, 0, 17.99, 'b26be512-510f-4064-bf9c-32d525fe76aa');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('MOUSEPAD-1005', 'Mouse Pad - Columbia', 'Fractal images are amazing! You can now own a mouse pad with a unique and amazing fractal. The mouse pad is made of a durable and smooth material. Your mouse will easily glide across the mouse pad. This mouse pad will brighten your workspace. Buy it now!', 'uploads/images/mousepads/mousepad-1005.png', TRUE, 100, 0, 17.99, 'b26be512-510f-4064-bf9c-32d525fe76aa');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('MOUSEPAD-1006', 'Mouse Pad - Worthing', 'Fractal images are amazing! You can now own a mouse pad with a unique and amazing fractal. The mouse pad is made of a durable and smooth material. Your mouse will easily glide across the mouse pad. This mouse pad will brighten your workspace. Buy it now!', 'uploads/images/mousepads/mousepad-1006.png', TRUE, 100, 0, 17.99, 'b26be512-510f-4064-bf9c-32d525fe76aa');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('MOUSEPAD-1007', 'Mouse Pad - Oak Cliff', 'Fractal images are amazing! You can now own a mouse pad with a unique and amazing fractal. The mouse pad is made of a durable and smooth material. Your mouse will easily glide across the mouse pad. This mouse pad will brighten your workspace. Buy it now!', 'uploads/images/mousepads/mousepad-1007.png', TRUE, 100, 0, 17.99, 'b26be512-510f-4064-bf9c-32d525fe76aa');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('MOUSEPAD-1008', 'Mouse Pad - Tachyon', 'Fractal images are amazing! You can now own a mouse pad with a unique and amazing fractal. The mouse pad is made of a durable and smooth material. Your mouse will easily glide across the mouse pad. This mouse pad will brighten your workspace. Buy it now!', 'uploads/images/mousepads/mousepad-1008.png', TRUE, 100, 0, 17.99, 'b26be512-510f-4064-bf9c-32d525fe76aa');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('MOUSEPAD-1009', 'Mouse Pad - Pan', 'Fractal images are amazing! You can now own a mouse pad with a unique and amazing fractal. The mouse pad is made of a durable and smooth material. Your mouse will easily glide across the mouse pad. This mouse pad will brighten your workspace. Buy it now!', 'uploads/images/mousepads/mousepad-1009.png', TRUE, 100, 0, 17.99, 'b26be512-510f-4064-bf9c-32d525fe76aa');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('MOUSEPAD-1010', 'Mouse Pad - Phase', 'Fractal images are amazing! You can now own a mouse pad with a unique and amazing fractal. The mouse pad is made of a durable and smooth material. Your mouse will easily glide across the mouse pad. This mouse pad will brighten your workspace. Buy it now!', 'uploads/images/mousepads/mousepad-1010.png', TRUE, 100, 0, 17.99, 'b26be512-510f-4064-bf9c-32d525fe76aa');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('MOUSEPAD-1011', 'Mouse Pad - Falling', 'Fractal images are amazing! You can now own a mouse pad with a unique and amazing fractal. The mouse pad is made of a durable and smooth material. Your mouse will easily glide across the mouse pad. This mouse pad will brighten your workspace. Buy it now!', 'uploads/images/mousepads/mousepad-1011.png', TRUE, 100, 0, 17.99, 'b26be512-510f-4064-bf9c-32d525fe76aa');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('MOUSEPAD-1012', 'Mouse Pad - Wispy', 'Fractal images are amazing! You can now own a mouse pad with a unique and amazing fractal. The mouse pad is made of a durable and smooth material. Your mouse will easily glide across the mouse pad. This mouse pad will brighten your workspace. Buy it now!', 'uploads/images/mousepads/mousepad-1012.png', TRUE, 100, 0, 17.99, 'b26be512-510f-4064-bf9c-32d525fe76aa');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('MOUSEPAD-1013', 'Mouse Pad - Arlington', 'Fractal images are amazing! You can now own a mouse pad with a unique and amazing fractal. The mouse pad is made of a durable and smooth material. Your mouse will easily glide across the mouse pad. This mouse pad will brighten your workspace. Buy it now!', 'uploads/images/mousepads/mousepad-1013.png', TRUE, 100, 0, 17.99, 'b26be512-510f-4064-bf9c-32d525fe76aa');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('MOUSEPAD-1014', 'Mouse Pad - Gazing', 'Fractal images are amazing! You can now own a mouse pad with a unique and amazing fractal. The mouse pad is made of a durable and smooth material. Your mouse will easily glide across the mouse pad. This mouse pad will brighten your workspace. Buy it now!', 'uploads/images/mousepads/mousepad-1014.png', TRUE, 100, 0, 17.99, 'b26be512-510f-4064-bf9c-32d525fe76aa');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('MOUSEPAD-1015', 'Mouse Pad - Azura', 'Fractal images are amazing! You can now own a mouse pad with a unique and amazing fractal. The mouse pad is made of a durable and smooth material. Your mouse will easily glide across the mouse pad. This mouse pad will brighten your workspace. Buy it now!', 'uploads/images/mousepads/mousepad-1015.png', TRUE, 100, 0, 17.99, 'b26be512-510f-4064-bf9c-32d525fe76aa');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('MOUSEPAD-1016', 'Mouse Pad - Quantum Leap', 'Fractal images are amazing! You can now own a mouse pad with a unique and amazing fractal. The mouse pad is made of a durable and smooth material. Your mouse will easily glide across the mouse pad. This mouse pad will brighten your workspace. Buy it now!', 'uploads/images/mousepads/mousepad-1016.png', TRUE, 100, 0, 17.99, 'b26be512-510f-4064-bf9c-32d525fe76aa');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('MOUSEPAD-1017', 'Mouse Pad - Light Years', 'Fractal images are amazing! You can now own a mouse pad with a unique and amazing fractal. The mouse pad is made of a durable and smooth material. Your mouse will easily glide across the mouse pad. This mouse pad will brighten your workspace. Buy it now!', 'uploads/images/mousepads/mousepad-1017.png', TRUE, 100, 0, 17.99, 'b26be512-510f-4064-bf9c-32d525fe76aa');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('MOUSEPAD-1018', 'Mouse Pad - Taylor', 'Fractal images are amazing! You can now own a mouse pad with a unique and amazing fractal. The mouse pad is made of a durable and smooth material. Your mouse will easily glide across the mouse pad. This mouse pad will brighten your workspace. Buy it now!', 'uploads/images/mousepads/mousepad-1018.png', TRUE, 100, 0, 17.99, 'b26be512-510f-4064-bf9c-32d525fe76aa');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('MOUSEPAD-1019', 'Mouse Pad - Gracia', 'Fractal images are amazing! You can now own a mouse pad with a unique and amazing fractal. The mouse pad is made of a durable and smooth material. Your mouse will easily glide across the mouse pad. This mouse pad will brighten your workspace. Buy it now!', 'uploads/images/mousepads/mousepad-1019.png', TRUE, 100, 0, 17.99, 'b26be512-510f-4064-bf9c-32d525fe76aa');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('MOUSEPAD-1020', 'Mouse Pad - Relax', 'Fractal images are amazing! You can now own a mouse pad with a unique and amazing fractal. The mouse pad is made of a durable and smooth material. Your mouse will easily glide across the mouse pad. This mouse pad will brighten your workspace. Buy it now!', 'uploads/images/mousepads/mousepad-1020.png', TRUE, 100, 0, 17.99, 'b26be512-510f-4064-bf9c-32d525fe76aa');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('MOUSEPAD-1021', 'Mouse Pad - Windermere', 'Fractal images are amazing! You can now own a mouse pad with a unique and amazing fractal. The mouse pad is made of a durable and smooth material. Your mouse will easily glide across the mouse pad. This mouse pad will brighten your workspace. Buy it now!', 'uploads/images/mousepads/mousepad-1021.png', TRUE, 100, 0, 17.99, 'b26be512-510f-4064-bf9c-32d525fe76aa');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('MOUSEPAD-1022', 'Mouse Pad - Prancer', 'Fractal images are amazing! You can now own a mouse pad with a unique and amazing fractal. The mouse pad is made of a durable and smooth material. Your mouse will easily glide across the mouse pad. This mouse pad will brighten your workspace. Buy it now!', 'uploads/images/mousepads/mousepad-1022.png', TRUE, 100, 0, 17.99, 'b26be512-510f-4064-bf9c-32d525fe76aa');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('MOUSEPAD-1023', 'Mouse Pad - Recursion', 'Fractal images are amazing! You can now own a mouse pad with a unique and amazing fractal. The mouse pad is made of a durable and smooth material. Your mouse will easily glide across the mouse pad. This mouse pad will brighten your workspace. Buy it now!', 'uploads/images/mousepads/mousepad-1023.png', TRUE, 100, 0, 17.99, 'b26be512-510f-4064-bf9c-32d525fe76aa');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('MOUSEPAD-1024', 'Mouse Pad - Treasure', 'Fractal images are amazing! You can now own a mouse pad with a unique and amazing fractal. The mouse pad is made of a durable and smooth material. Your mouse will easily glide across the mouse pad. This mouse pad will brighten your workspace. Buy it now!', 'uploads/images/mousepads/mousepad-1024.png', TRUE, 100, 0, 17.99, 'b26be512-510f-4064-bf9c-32d525fe76aa');

-- -----------------------------------------------------
-- Luggage Tags
-- -----------------------------------------------------
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('LUGGAGETAG-1000', 'Luggage Tag - Cherish', 'This luggage tag will help you identify your luggage. The luggage tag is very unique and it will stand out from the crowd. The luggage tag is created out of a rugged and durable plastic. Buy this luggage tag now to make it easy to identify your luggage!', 'uploads/images/luggagetags/luggagetag-1000.png', TRUE, 100, 0, 16.99, '8e9f6773-c946-462d-afc5-69ffac311b10');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('LUGGAGETAG-1001', 'Luggage Tag - Adventure', 'This luggage tag will help you identify your luggage. The luggage tag is very unique and it will stand out from the crowd. The luggage tag is created out of a rugged and durable plastic. Buy this luggage tag now to make it easy to identify your luggage!', 'uploads/images/luggagetags/luggagetag-1001.png', TRUE, 100, 0, 16.99, '8e9f6773-c946-462d-afc5-69ffac311b10');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('LUGGAGETAG-1002', 'Luggage Tag - Skyline', 'This luggage tag will help you identify your luggage. The luggage tag is very unique and it will stand out from the crowd. The luggage tag is created out of a rugged and durable plastic. Buy this luggage tag now to make it easy to identify your luggage!', 'uploads/images/luggagetags/luggagetag-1002.png', TRUE, 100, 0, 16.99, '8e9f6773-c946-462d-afc5-69ffac311b10');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('LUGGAGETAG-1003', 'Luggage Tag - River', 'This luggage tag will help you identify your luggage. The luggage tag is very unique and it will stand out from the crowd. The luggage tag is created out of a rugged and durable plastic. Buy this luggage tag now to make it easy to identify your luggage!', 'uploads/images/luggagetags/luggagetag-1003.png', TRUE, 100, 0, 16.99, '8e9f6773-c946-462d-afc5-69ffac311b10');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('LUGGAGETAG-1004', 'Luggage Tag - Trail Steps', 'This luggage tag will help you identify your luggage. The luggage tag is very unique and it will stand out from the crowd. The luggage tag is created out of a rugged and durable plastic. Buy this luggage tag now to make it easy to identify your luggage!', 'uploads/images/luggagetags/luggagetag-1004.png', TRUE, 100, 0, 16.99, '8e9f6773-c946-462d-afc5-69ffac311b10');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('LUGGAGETAG-1005', 'Luggage Tag - Blooming', 'This luggage tag will help you identify your luggage. The luggage tag is very unique and it will stand out from the crowd. The luggage tag is created out of a rugged and durable plastic. Buy this luggage tag now to make it easy to identify your luggage!', 'uploads/images/luggagetags/luggagetag-1005.png', TRUE, 100, 0, 16.99, '8e9f6773-c946-462d-afc5-69ffac311b10');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('LUGGAGETAG-1006', 'Luggage Tag - Park', 'This luggage tag will help you identify your luggage. The luggage tag is very unique and it will stand out from the crowd. The luggage tag is created out of a rugged and durable plastic. Buy this luggage tag now to make it easy to identify your luggage!', 'uploads/images/luggagetags/luggagetag-1006.png', TRUE, 100, 0, 16.99, '8e9f6773-c946-462d-afc5-69ffac311b10');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('LUGGAGETAG-1007', 'Luggage Tag - Beauty', 'This luggage tag will help you identify your luggage. The luggage tag is very unique and it will stand out from the crowd. The luggage tag is created out of a rugged and durable plastic. Buy this luggage tag now to make it easy to identify your luggage!', 'uploads/images/luggagetags/luggagetag-1007.png', TRUE, 100, 0, 16.99, '8e9f6773-c946-462d-afc5-69ffac311b10');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('LUGGAGETAG-1008', 'Luggage Tag - Water Fall', 'This luggage tag will help you identify your luggage. The luggage tag is very unique and it will stand out from the crowd. The luggage tag is created out of a rugged and durable plastic. Buy this luggage tag now to make it easy to identify your luggage!', 'uploads/images/luggagetags/luggagetag-1008.png', TRUE, 100, 0, 16.99, '8e9f6773-c946-462d-afc5-69ffac311b10');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('LUGGAGETAG-1009', 'Luggage Tag - Trail', 'This luggage tag will help you identify your luggage. The luggage tag is very unique and it will stand out from the crowd. The luggage tag is created out of a rugged and durable plastic. Buy this luggage tag now to make it easy to identify your luggage!', 'uploads/images/luggagetags/luggagetag-1009.png', TRUE, 100, 0, 16.99, '8e9f6773-c946-462d-afc5-69ffac311b10');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('LUGGAGETAG-1010', 'Luggage Tag - Skyscraper', 'This luggage tag will help you identify your luggage. The luggage tag is very unique and it will stand out from the crowd. The luggage tag is created out of a rugged and durable plastic. Buy this luggage tag now to make it easy to identify your luggage!', 'uploads/images/luggagetags/luggagetag-1010.png', TRUE, 100, 0, 16.99, '8e9f6773-c946-462d-afc5-69ffac311b10');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('LUGGAGETAG-1011', 'Luggage Tag - Leaf', 'This luggage tag will help you identify your luggage. The luggage tag is very unique and it will stand out from the crowd. The luggage tag is created out of a rugged and durable plastic. Buy this luggage tag now to make it easy to identify your luggage!', 'uploads/images/luggagetags/luggagetag-1011.png', TRUE, 100, 0, 16.99, '8e9f6773-c946-462d-afc5-69ffac311b10');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('LUGGAGETAG-1012', 'Luggage Tag - Jungle', 'This luggage tag will help you identify your luggage. The luggage tag is very unique and it will stand out from the crowd. The luggage tag is created out of a rugged and durable plastic. Buy this luggage tag now to make it easy to identify your luggage!', 'uploads/images/luggagetags/luggagetag-1012.png', TRUE, 100, 0, 16.99, '8e9f6773-c946-462d-afc5-69ffac311b10');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('LUGGAGETAG-1013', 'Luggage Tag - Shoreline', 'This luggage tag will help you identify your luggage. The luggage tag is very unique and it will stand out from the crowd. The luggage tag is created out of a rugged and durable plastic. Buy this luggage tag now to make it easy to identify your luggage!', 'uploads/images/luggagetags/luggagetag-1013.png', TRUE, 100, 0, 16.99, '8e9f6773-c946-462d-afc5-69ffac311b10');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('LUGGAGETAG-1014', 'Luggage Tag - Blossom', 'This luggage tag will help you identify your luggage. The luggage tag is very unique and it will stand out from the crowd. The luggage tag is created out of a rugged and durable plastic. Buy this luggage tag now to make it easy to identify your luggage!', 'uploads/images/luggagetags/luggagetag-1014.png', TRUE, 100, 0, 16.99, '8e9f6773-c946-462d-afc5-69ffac311b10');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('LUGGAGETAG-1015', 'Luggage Tag - Lock', 'This luggage tag will help you identify your luggage. The luggage tag is very unique and it will stand out from the crowd. The luggage tag is created out of a rugged and durable plastic. Buy this luggage tag now to make it easy to identify your luggage!', 'uploads/images/luggagetags/luggagetag-1015.png', TRUE, 100, 0, 16.99, '8e9f6773-c946-462d-afc5-69ffac311b10');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('LUGGAGETAG-1016', 'Luggage Tag - Cafe', 'This luggage tag will help you identify your luggage. The luggage tag is very unique and it will stand out from the crowd. The luggage tag is created out of a rugged and durable plastic. Buy this luggage tag now to make it easy to identify your luggage!', 'uploads/images/luggagetags/luggagetag-1016.png', TRUE, 100, 0, 16.99, '8e9f6773-c946-462d-afc5-69ffac311b10');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('LUGGAGETAG-1017', 'Luggage Tag - Darling', 'This luggage tag will help you identify your luggage. The luggage tag is very unique and it will stand out from the crowd. The luggage tag is created out of a rugged and durable plastic. Buy this luggage tag now to make it easy to identify your luggage!', 'uploads/images/luggagetags/luggagetag-1017.png', TRUE, 100, 0, 16.99, '8e9f6773-c946-462d-afc5-69ffac311b10');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('LUGGAGETAG-1018', 'Luggage Tag - Full Stack', 'This luggage tag will help you identify your luggage. The luggage tag is very unique and it will stand out from the crowd. The luggage tag is created out of a rugged and durable plastic. Buy this luggage tag now to make it easy to identify your luggage!', 'uploads/images/luggagetags/luggagetag-1018.png', TRUE, 100, 0, 16.99, '8e9f6773-c946-462d-afc5-69ffac311b10');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('LUGGAGETAG-1019', 'Luggage Tag - Courtyard', 'This luggage tag will help you identify your luggage. The luggage tag is very unique and it will stand out from the crowd. The luggage tag is created out of a rugged and durable plastic. Buy this luggage tag now to make it easy to identify your luggage!', 'uploads/images/luggagetags/luggagetag-1019.png', TRUE, 100, 0, 16.99, '8e9f6773-c946-462d-afc5-69ffac311b10');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('LUGGAGETAG-1020', 'Luggage Tag - Coaster', 'This luggage tag will help you identify your luggage. The luggage tag is very unique and it will stand out from the crowd. The luggage tag is created out of a rugged and durable plastic. Buy this luggage tag now to make it easy to identify your luggage!', 'uploads/images/luggagetags/luggagetag-1020.png', TRUE, 100, 0, 16.99, '8e9f6773-c946-462d-afc5-69ffac311b10');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('LUGGAGETAG-1021', 'Luggage Tag - Bridge', 'This luggage tag will help you identify your luggage. The luggage tag is very unique and it will stand out from the crowd. The luggage tag is created out of a rugged and durable plastic. Buy this luggage tag now to make it easy to identify your luggage!', 'uploads/images/luggagetags/luggagetag-1021.png', TRUE, 100, 0, 16.99, '8e9f6773-c946-462d-afc5-69ffac311b10');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('LUGGAGETAG-1022', 'Luggage Tag - Sunset', 'This luggage tag will help you identify your luggage. The luggage tag is very unique and it will stand out from the crowd. The luggage tag is created out of a rugged and durable plastic. Buy this luggage tag now to make it easy to identify your luggage!', 'uploads/images/luggagetags/luggagetag-1022.png', TRUE, 100, 0, 16.99, '8e9f6773-c946-462d-afc5-69ffac311b10');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('LUGGAGETAG-1023', 'Luggage Tag - Flames', 'This luggage tag will help you identify your luggage. The luggage tag is very unique and it will stand out from the crowd. The luggage tag is created out of a rugged and durable plastic. Buy this luggage tag now to make it easy to identify your luggage!', 'uploads/images/luggagetags/luggagetag-1023.png', TRUE, 100, 0, 16.99, '8e9f6773-c946-462d-afc5-69ffac311b10');
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, units_sold, unit_price, category_id) VALUES ('LUGGAGETAG-1024', 'Luggage Tag - Countryside', 'This luggage tag will help you identify your luggage. The luggage tag is very unique and it will stand out from the crowd. The luggage tag is created out of a rugged and durable plastic. Buy this luggage tag now to make it easy to identify your luggage!', 'uploads/images/luggagetags/luggagetag-1024.png', TRUE, 100, 0, 16.99, '8e9f6773-c946-462d-afc5-69ffac311b10');


-- Insert data into the "country" table
INSERT INTO country (id, code, name) VALUES 
('24a88516-2a94-42da-ac03-0a8043d37572', 'BR', 'Brazil'),
('53da6c4c-99c1-4ebd-8689-f33317702ff0', 'CA', 'Canada'),
('07b04c34-5471-4432-9c50-d18e5f2699a4', 'DE', 'Germany'),
('cf9fcf33-0eb8-4408-87ac-3693307822ea', 'IN', 'India'),
('e9909d92-9955-4eed-9314-68745cc34ab3', 'TR', 'Turkey'),
('d666418d-b248-4231-acf1-cf7d212ed993', 'US', 'United States');


INSERT INTO state (name,country_id)VALUES 
('Acre','24a88516-2a94-42da-ac03-0a8043d37572'),
('Alagoas','24a88516-2a94-42da-ac03-0a8043d37572'),
('Amapá','24a88516-2a94-42da-ac03-0a8043d37572'),
('Amazonas','24a88516-2a94-42da-ac03-0a8043d37572'),
('Bahia','24a88516-2a94-42da-ac03-0a8043d37572'),
('Ceará','24a88516-2a94-42da-ac03-0a8043d37572'),
('Distrito Federal','24a88516-2a94-42da-ac03-0a8043d37572'),
('Espírito Santo','24a88516-2a94-42da-ac03-0a8043d37572'),
('Goiás','24a88516-2a94-42da-ac03-0a8043d37572'),
('Maranhão','24a88516-2a94-42da-ac03-0a8043d37572'),
('Mato Grosso do Sul','24a88516-2a94-42da-ac03-0a8043d37572'),
('Mato Grosso','24a88516-2a94-42da-ac03-0a8043d37572'),
('Minas Gerais','24a88516-2a94-42da-ac03-0a8043d37572'),
('Paraná','24a88516-2a94-42da-ac03-0a8043d37572'),
('Paraíba','24a88516-2a94-42da-ac03-0a8043d37572'),
('Pará','24a88516-2a94-42da-ac03-0a8043d37572'),
('Pernambuco','24a88516-2a94-42da-ac03-0a8043d37572'),
('Piaui','24a88516-2a94-42da-ac03-0a8043d37572'),
('Rio de Janeiro','24a88516-2a94-42da-ac03-0a8043d37572'),
('Rio Grande do Norte','24a88516-2a94-42da-ac03-0a8043d37572'),
('Rio Grande do Sul','24a88516-2a94-42da-ac03-0a8043d37572'),
('Rondônia','24a88516-2a94-42da-ac03-0a8043d37572'),
('Roraima','24a88516-2a94-42da-ac03-0a8043d37572'),
('Santa Catarina','24a88516-2a94-42da-ac03-0a8043d37572'),
('Sergipe','24a88516-2a94-42da-ac03-0a8043d37572'),
('São Paulo','24a88516-2a94-42da-ac03-0a8043d37572'),
('Tocantins','24a88516-2a94-42da-ac03-0a8043d37572'),
('Alberta','53da6c4c-99c1-4ebd-8689-f33317702ff0'),
('British Columbia','53da6c4c-99c1-4ebd-8689-f33317702ff0'),
('Manitoba','53da6c4c-99c1-4ebd-8689-f33317702ff0'),
('New Brunswick','53da6c4c-99c1-4ebd-8689-f33317702ff0'),
('Newfoundland and Labrador','53da6c4c-99c1-4ebd-8689-f33317702ff0'),
('Northwest Territories','53da6c4c-99c1-4ebd-8689-f33317702ff0'),
('Nova Scotia','53da6c4c-99c1-4ebd-8689-f33317702ff0'),
('Nunavut','53da6c4c-99c1-4ebd-8689-f33317702ff0'),
('Ontario','53da6c4c-99c1-4ebd-8689-f33317702ff0'),
('Prince Edward Island','53da6c4c-99c1-4ebd-8689-f33317702ff0'),
('Quebec','53da6c4c-99c1-4ebd-8689-f33317702ff0'),
('Saskatchewan','53da6c4c-99c1-4ebd-8689-f33317702ff0'),
('Yukon','53da6c4c-99c1-4ebd-8689-f33317702ff0'),
('Baden-Württemberg','07b04c34-5471-4432-9c50-d18e5f2699a4'),
('Bavaria','07b04c34-5471-4432-9c50-d18e5f2699a4'),
('Berlin','07b04c34-5471-4432-9c50-d18e5f2699a4'),
('Brandenburg','07b04c34-5471-4432-9c50-d18e5f2699a4'),
('Bremen','07b04c34-5471-4432-9c50-d18e5f2699a4'),
('Hamburg','07b04c34-5471-4432-9c50-d18e5f2699a4'),
('Hesse','07b04c34-5471-4432-9c50-d18e5f2699a4'),
('Lower Saxony','07b04c34-5471-4432-9c50-d18e5f2699a4'),
('Mecklenburg-Vorpommern','07b04c34-5471-4432-9c50-d18e5f2699a4'),
('North Rhine-Westphalia','07b04c34-5471-4432-9c50-d18e5f2699a4'),
('Rhineland-Palatinate','07b04c34-5471-4432-9c50-d18e5f2699a4'),
('Saarland','07b04c34-5471-4432-9c50-d18e5f2699a4'),
('Saxony','07b04c34-5471-4432-9c50-d18e5f2699a4'),
('Saxony-Anhalt','07b04c34-5471-4432-9c50-d18e5f2699a4'),
('Schleswig-Holstein','07b04c34-5471-4432-9c50-d18e5f2699a4'),
('Thuringia','07b04c34-5471-4432-9c50-d18e5f2699a4'),
('Andhra Pradesh','cf9fcf33-0eb8-4408-87ac-3693307822ea'),
('Arunachal Pradesh','cf9fcf33-0eb8-4408-87ac-3693307822ea'),
('Assam','cf9fcf33-0eb8-4408-87ac-3693307822ea'),
('Bihar','cf9fcf33-0eb8-4408-87ac-3693307822ea'),
('Chhattisgarh','cf9fcf33-0eb8-4408-87ac-3693307822ea'),
('Goa','cf9fcf33-0eb8-4408-87ac-3693307822ea'),
('Gujarat','cf9fcf33-0eb8-4408-87ac-3693307822ea'),
('Haryana','cf9fcf33-0eb8-4408-87ac-3693307822ea'),
('Himachal Pradesh','cf9fcf33-0eb8-4408-87ac-3693307822ea'),
('Jammu & Kashmir','cf9fcf33-0eb8-4408-87ac-3693307822ea'),
('Jharkhand','cf9fcf33-0eb8-4408-87ac-3693307822ea'),
('Karnataka','cf9fcf33-0eb8-4408-87ac-3693307822ea'),
('Kerala','cf9fcf33-0eb8-4408-87ac-3693307822ea'),
('Madhya Pradesh','cf9fcf33-0eb8-4408-87ac-3693307822ea'),
('Maharashtra','cf9fcf33-0eb8-4408-87ac-3693307822ea'),
('Manipur','cf9fcf33-0eb8-4408-87ac-3693307822ea'),
('Meghalaya','cf9fcf33-0eb8-4408-87ac-3693307822ea'),
('Mizoram','cf9fcf33-0eb8-4408-87ac-3693307822ea'),
('Nagaland','cf9fcf33-0eb8-4408-87ac-3693307822ea'),
('Odisha','cf9fcf33-0eb8-4408-87ac-3693307822ea'),
('Punjab','cf9fcf33-0eb8-4408-87ac-3693307822ea'),
('Rajasthan','cf9fcf33-0eb8-4408-87ac-3693307822ea'),
('Sikkim','cf9fcf33-0eb8-4408-87ac-3693307822ea'),
('Tamil Nadu','cf9fcf33-0eb8-4408-87ac-3693307822ea'),
('Telangana','cf9fcf33-0eb8-4408-87ac-3693307822ea'),
('Tripura','cf9fcf33-0eb8-4408-87ac-3693307822ea'),
('Uttar Pradesh','cf9fcf33-0eb8-4408-87ac-3693307822ea'),
('Uttarakhand','cf9fcf33-0eb8-4408-87ac-3693307822ea'),
('West Bengal','cf9fcf33-0eb8-4408-87ac-3693307822ea'),
('Andaman and Nicobar Islands','cf9fcf33-0eb8-4408-87ac-3693307822ea'),
('Chandigarh','cf9fcf33-0eb8-4408-87ac-3693307822ea'),
('Dadra and Nagar Haveli','cf9fcf33-0eb8-4408-87ac-3693307822ea'),
('Daman & Diu','cf9fcf33-0eb8-4408-87ac-3693307822ea'),
('Lakshadweep','cf9fcf33-0eb8-4408-87ac-3693307822ea'),
('Puducherry','cf9fcf33-0eb8-4408-87ac-3693307822ea'),
('The Government of NCT of Delhi','cf9fcf33-0eb8-4408-87ac-3693307822ea'),
('Alabama','d666418d-b248-4231-acf1-cf7d212ed993'),
('Alaska','d666418d-b248-4231-acf1-cf7d212ed993'),
('Arizona','d666418d-b248-4231-acf1-cf7d212ed993'),
('Arkansas','d666418d-b248-4231-acf1-cf7d212ed993'),
('California','d666418d-b248-4231-acf1-cf7d212ed993'),
('Colorado','d666418d-b248-4231-acf1-cf7d212ed993'),
('Connecticut','d666418d-b248-4231-acf1-cf7d212ed993'),
('Delaware','d666418d-b248-4231-acf1-cf7d212ed993'),
('District Of Columbia','d666418d-b248-4231-acf1-cf7d212ed993'),
('Florida','d666418d-b248-4231-acf1-cf7d212ed993'),
('Georgia','d666418d-b248-4231-acf1-cf7d212ed993'),
('Hawaii','d666418d-b248-4231-acf1-cf7d212ed993'),
('Idaho','d666418d-b248-4231-acf1-cf7d212ed993'),
('Illinois','d666418d-b248-4231-acf1-cf7d212ed993'),
('Indiana','d666418d-b248-4231-acf1-cf7d212ed993'),
('Iowa','d666418d-b248-4231-acf1-cf7d212ed993'),
('Kansas','d666418d-b248-4231-acf1-cf7d212ed993'),
('Kentucky','d666418d-b248-4231-acf1-cf7d212ed993'),
('Louisiana','d666418d-b248-4231-acf1-cf7d212ed993'),
('Maine','d666418d-b248-4231-acf1-cf7d212ed993'),
('Maryland','d666418d-b248-4231-acf1-cf7d212ed993'),
('Massachusetts','d666418d-b248-4231-acf1-cf7d212ed993'),
('Michigan','d666418d-b248-4231-acf1-cf7d212ed993'),
('Minnesota','d666418d-b248-4231-acf1-cf7d212ed993'),
('Mississippi','d666418d-b248-4231-acf1-cf7d212ed993'),
('Missouri','d666418d-b248-4231-acf1-cf7d212ed993'),
('Montana','d666418d-b248-4231-acf1-cf7d212ed993'),
('Nebraska','d666418d-b248-4231-acf1-cf7d212ed993'),
('Nevada','d666418d-b248-4231-acf1-cf7d212ed993'),
('New Hampshire','d666418d-b248-4231-acf1-cf7d212ed993'),
('New Jersey','d666418d-b248-4231-acf1-cf7d212ed993'),
('New Mexico','d666418d-b248-4231-acf1-cf7d212ed993'),
('New York','d666418d-b248-4231-acf1-cf7d212ed993'),
('North Carolina','d666418d-b248-4231-acf1-cf7d212ed993'),
('North Dakota','d666418d-b248-4231-acf1-cf7d212ed993'),
('Ohio','d666418d-b248-4231-acf1-cf7d212ed993'),
('Oklahoma','d666418d-b248-4231-acf1-cf7d212ed993'),
('Oregon','d666418d-b248-4231-acf1-cf7d212ed993'),
('Pennsylvania','d666418d-b248-4231-acf1-cf7d212ed993'),
('Rhode Island','d666418d-b248-4231-acf1-cf7d212ed993'),
('South Carolina','d666418d-b248-4231-acf1-cf7d212ed993'),
('South Dakota','d666418d-b248-4231-acf1-cf7d212ed993'),
('Tennessee','d666418d-b248-4231-acf1-cf7d212ed993'),
('Texas','d666418d-b248-4231-acf1-cf7d212ed993'),
('Utah','d666418d-b248-4231-acf1-cf7d212ed993'),
('Vermont','d666418d-b248-4231-acf1-cf7d212ed993'),
('Virginia','d666418d-b248-4231-acf1-cf7d212ed993'),
('Washington','d666418d-b248-4231-acf1-cf7d212ed993'),
('West Virginia','d666418d-b248-4231-acf1-cf7d212ed993'),
('Wisconsin','d666418d-b248-4231-acf1-cf7d212ed993'),
('Wyoming','d666418d-b248-4231-acf1-cf7d212ed993'),
('Adıyaman','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Afyonkarahisar','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Ağrı','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Aksaray','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Amasya','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Ankara','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Antalya','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Ardahan','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Artvin','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Aydın','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Balıkesir','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Bartın','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Batman','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Bayburt','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Bilecik','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Bingöl','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Bitlis','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Bolu','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Burdur','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Bursa','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Çanakkale','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Çankırı','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Çorum','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Denizli','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Diyarbakır','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Düzce','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Edirne','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Elazığ','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Erzincan','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Erzurum','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Eskişehir','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Gaziantep','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Giresun','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Gümüşhane','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Hakkâri','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Hatay','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Iğdır','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Isparta','e9909d92-9955-4eed-9314-68745cc34ab3'),
('İstanbul','e9909d92-9955-4eed-9314-68745cc34ab3'),
('İzmir','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Kahramanmaraş','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Karabük','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Karaman','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Kars','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Kastamonu','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Kayseri','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Kırıkkale','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Kırklareli','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Kırşehir','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Kilis','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Kocaeli','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Konya','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Kütahya','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Malatya','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Manisa','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Mardin','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Mersin','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Muğla','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Muş','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Nevşehir','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Niğde','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Ordu','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Osmaniye','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Rize','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Sakarya','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Samsun','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Siirt','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Sinop','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Sivas','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Şanlıurfa','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Şırnak','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Tekirdağ','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Tokat','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Trabzon','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Tunceli','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Uşak','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Van','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Yalova','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Yozgat','e9909d92-9955-4eed-9314-68745cc34ab3'),
('Zonguldak','e9909d92-9955-4eed-9314-68745cc34ab3');