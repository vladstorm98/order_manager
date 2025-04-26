INSERT INTO users (name, password, role, email) VALUES
('Name_1', 'Password', 'USER', 'email_1@example.com'),
('Name_2', 'Password', 'USER', 'email_2@example.com');

INSERT INTO products (id, name, description, price) VALUES
(1, 'Product_1', 'Product Description', 1),
(2, 'Product_2', 'Product Description', 2);

INSERT INTO orders (quantity, user_id, status) VALUES
(1,1,'PENDING'),
(1,1,'PENDING');

INSERT INTO order_products (order_id, product_id) VALUES
(1, 1),
(1, 2),
(2, 1),
(2, 2);
