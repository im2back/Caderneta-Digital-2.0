CREATE TABLE tb_product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(255) NOT NULL,
    product_price DECIMAL(10,2) NOT NULL,
    product_code VARCHAR(20) NOT NULL UNIQUE,
    product_quantity INT NOT NULL,
    product_url VARCHAR(255)
);
