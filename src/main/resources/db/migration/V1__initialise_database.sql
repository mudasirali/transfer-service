DROP SCHEMA transactions IF EXISTS;

CREATE SCHEMA transactions;

CREATE TABLE transactions.transfers (
  id BIGINT PRIMARY KEY auto_increment,
  sender_id BIGINT NOT NULL,
  receiver_id BIGINT NOT NULL,
  amount BIGINT NOT NULL,
  status TINYINT NOT NULL,
  created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE transactions.customers (
  id BIGINT PRIMARY KEY auto_increment,
  title VARCHAR(100) NOT NULL ,
  branch_id INT NOT NULL,
  account_number BIGINT,
  account_balance BIGINT,
  created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE INDEX created_on_idx ON transactions.transfers(created_on);
CREATE INDEX acc_idx ON transactions.customers(branch_id, account_number);
CREATE UNIQUE INDEX uq_customers
  ON transactions.customers(branch_id, account_number);