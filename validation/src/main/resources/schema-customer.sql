CREATE TABLE tb_customer (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_name VARCHAR(255) NOT NULL,
    document VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL UNIQUE,
    is_active BOOLEAN NOT NULL,
    street_name VARCHAR(255),
    house_number VARCHAR(50),
    complement VARCHAR(255)
);
