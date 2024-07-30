CREATE TABLE tb_purchase (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    product_price DECIMAL(10, 2) NOT NULL,
    product_code VARCHAR(255) NOT NULL,
    purchase_date DATETIME NOT NULL,
    product_quantity INT NOT NULL,
    payment_status ENUM('PAGO', 'EM_ABERTO') NOT NULL,
    customer_id BIGINT,
    FOREIGN KEY (customer_id) REFERENCES tb_customer(id) ON DELETE CASCADE
);