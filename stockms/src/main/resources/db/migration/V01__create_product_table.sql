CREATE TABLE tb_product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    product_price DECIMAL(19, 2) NOT NULL,
    product_code VARCHAR(255) UNIQUE NOT NULL,
    product_quantity INT NOT NULL
);