CREATE KEYSPACE coc_payments
	WITH replication = {
		'class':'SimpleStrategy', 
		'replication_factor' : 1
	};

USE coc_payments;

CREATE TYPE coc_payments.address (
    city text,
    countrycode text,
    line1 text,
    line2 text,
    name text,
    phone text,
    postcode text,
    state text
);

CREATE TYPE coc_payments.amount (
    currency text,
    shipping text,
    subtotal text,
    tax text,
    total text
);

CREATE TYPE coc_payments.transaction (
    amount text,
    id uuid,
    type text,
    userid text
);

CREATE TABLE coc_payments.paymentrecord (
    id text PRIMARY KEY,
    address frozen<address>,
    amount frozen<amount>,
    currency text,
    description text,
    idempotency_key text,
    intent text,
    payer_id text,
    payment_method text,
    payment_provider text,
    payment_status text,
    shipping text,
    sub_total text,
    tax text,
    total text,
    transactions set<frozen<transaction>>,
    user_id text
);

CREATE TABLE coc_payments.paymentrequest (
    idempotency_key text PRIMARY KEY,
    auth_url text,
    payment_id text,
    payment_status text
);

CREATE TABLE coc_payments.paymentcard (
    user_id text PRIMARY KEY,
    expiration_month text,
    expiration_year text,
    id text,
    number text
);

INSERT INTO paymentRecord (id, idempotency_key, payment_provider, payment_method, description, intent, payer_id, user_id, total, sub_total, shipping, tax, currency, payment_status, transactions) 
VALUES ('id', 'idempotency_key', 'paypal', 'paypal', 'description', 'sale', 'payer_id', 'user_id', '7', '5', '1', '1', 'USD', 'created', {{ id : now(), type : 'create' }});

INSERT INTO paymentRequest (idempotency_key, payment_id, payment_status, auth_url)
VALUES ('idempotency_key', 'id', 'created', 'http://paypal.com/auth');

INSERT INTO paymentCard (id, user_id, number, expiration_year, expiration_month)
VALUES ('id', '1234', '4111111111111111', '2030', '12');

SELECT * FROM paymentRecord WHERE id = 2aa40790-fcd4-11e9-a936-e176235bcdf6;

UPDATE paymentRecord 
SET transactions = transactions + {{ id : now(), type : 'update' }} WHERE id = aae90430-fbb9-11e9-a936-e176235bcdf6;

SELECT * FROM paymentRecord;
SELECT * FROM paymentRequest;
SELECT * FROM paymentCard;
TRUNCATE paymentRecord;
TRUNCATE paymentRequest;
TRUNCATE paymentCard;
DESCRIBE TYPE address;
DESCRIBE TYPE amount;
DESCRIBE TYPE transaction;
DESCRIBE paymentRecord;
DESCRIBE paymentRequest;
DESCRIBE paymentCard;
DROP TABLE IF EXISTS paymentRequest;
DROP TABLE IF EXISTS paymentRecord;
DROP TABLE IF EXISTS paymentCard;
DROP TYPE IF EXISTS transaction;
DROP TYPE IF EXISTS address;
DROP TYPE IF EXISTS amount;
DROP KEYSPACE IF EXISTS coc_payments;