--liquibase formatted sql

--changeset valiit:202207131534
CREATE TABLE customers
(
    id   varchar(36) NOT NULL,
    name varchar(25),
    PRIMARY KEY (id)
);

--changeset valiit:202207131535
CREATE TABLE invoices
(
    id          varchar(36) NOT NULL,
    customer_id varchar(36) NOT NULL,
    date        timestamp DEFAULT localtimestamp,
    amount      DECIMAL(8, 2),
    PRIMARY KEY (id),
    FOREIGN KEY (customer_id) REFERENCES customers (id)
);

--changeset valiit:202207131536
CREATE TABLE payments
(
    id     varchar(36) NOT NULL,
    date   timestamp DEFAULT localtimestamp,
    amount DECIMAL(8, 2),
    PRIMARY KEY (id)
);

--changeset valiit:202207131537
CREATE TABLE invoice_payments
(
    invoice_id varchar(36) NOT NULL,
    payment_id varchar(36) NOT NULL,
    amount     DECIMAL(8, 2),
    FOREIGN KEY (invoice_id) REFERENCES invoices (id),
    FOREIGN KEY (payment_id) REFERENCES payments (id)
);