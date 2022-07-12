CREATE TABLE customers
(
    id   varchar(32) NOT NULL,
    name varchar(25),
    PRIMARY KEY (id)
);

CREATE TABLE invoices
(
    id          varchar(32) NOT NULL,
    customer_id varchar(32) NOT NULL,
    date        timestamp DEFAULT localtimestamp,
    amount      DECIMAL(8, 2),
    PRIMARY KEY (id),
    FOREIGN KEY (customer_id) REFERENCES customers (id)
);
CREATE TABLE payments
(
    id     varchar(32) NOT NULL,
    date   timestamp DEFAULT localtimestamp,
    amount DECIMAL(8, 2),
    PRIMARY KEY (id)
);

CREATE TABLE invoice_payments
(
    invoice_id varchar(32) NOT NULL,
    payment_id varchar(32) NOT NULL,
    amount     DECIMAL(8, 2),
    FOREIGN KEY (invoice_id) REFERENCES invoices (id),
    FOREIGN KEY (payment_id) REFERENCES payments (id)
);