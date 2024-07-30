CREATE TABLE tb_customer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(255) NOT NULL,
    document VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(255) UNIQUE NOT NULL,
    is_active BOOLEAN NOT NULL,
    street_name VARCHAR(255),
    house_number VARCHAR(50),
    complement VARCHAR(255)
);