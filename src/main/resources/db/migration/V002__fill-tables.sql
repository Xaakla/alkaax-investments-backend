INSERT INTO users VALUES (nextval('user_id_seq'), 'Diego', 'Rocha', 'xaakla', 'xaakla@gmail.com', '123456');

INSERT INTO stocks VALUES (nextval('stock_id_seq'), 'RECR11', 0);

INSERT INTO batch_investments VALUES (nextval('batch_investments_id_seq'), 'Junho/22', 315.96);

INSERT INTO investment_moves VALUES (nextval('investment_moves_id_seq'), 4, 78.99, 'BUY', 1, 1);

INSERT INTO batch_dividends VALUES (nextval('batch_dividends_id_seq'), 'Junho/22', 6);

INSERT INTO dividend_moves VALUES (nextval('dividend_moves_id_seq'), 4, 1.5, 1, 1);