CREATE SEQUENCE IF NOT EXISTS user_id_seq;
CREATE SEQUENCE IF NOT EXISTS stock_id_seq;
CREATE SEQUENCE IF NOT EXISTS batch_investments_id_seq;
CREATE SEQUENCE IF NOT EXISTS investment_moves_id_seq;
CREATE SEQUENCE IF NOT EXISTS batch_dividends_id_seq;
CREATE SEQUENCE IF NOT EXISTS dividend_moves_id_seq;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY DEFAULT nextval('user_id_seq'),
    firstname VARCHAR(12) NOT NULL,
    lastname VARCHAR(12) NOT NULL,
    username VARCHAR(12) NOT NULL,
    email VARCHAR(64) NOT NULL,
    password VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS stocks (
    id BIGINT PRIMARY KEY DEFAULT nextval('stock_id_seq'),
    code VARCHAR(12) NOT NULL,
    quotas INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS batch_investments (
    id BIGINT PRIMARY KEY DEFAULT nextval('batch_investments_id_seq'),
    name VARCHAR(32) NOT NULL,
    total INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS investment_moves (
    id BIGINT PRIMARY KEY DEFAULT nextval('investment_moves_id_seq'),
    quantity INTEGER NOT NULL,
    price INTEGER NOT NULL,
    status VARCHAR(12) NOT NULL,
    stock_id BIGINT NOT NULL,
    batch_investment_id BIGINT NOT NULL,

    CONSTRAINT fk_investment_moves_stock FOREIGN KEY (stock_id) REFERENCES stocks,
    CONSTRAINT fk_investment_moves_batch_investment FOREIGN KEY (batch_investment_id) REFERENCES batch_investments
);

CREATE TABLE IF NOT EXISTS batch_dividends (
    id BIGINT PRIMARY KEY DEFAULT nextval('batch_dividends_id_seq'),
    name VARCHAR(32) NOT NULL,
    total INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS dividend_moves (
    id BIGINT PRIMARY KEY DEFAULT nextval('dividend_moves_id_seq'),
    quantity INTEGER NOT NULL,
    price INTEGER NOT NULL,
    stock_id BIGINT NOT NULL,
    batch_dividend_id BIGINT NOT NULL,

    CONSTRAINT fk_dividend_moves_stock FOREIGN KEY (stock_id) REFERENCES stocks,
    CONSTRAINT fk_dividend_moves_batch_dividend FOREIGN KEY (batch_dividend_id) REFERENCES batch_dividends
);