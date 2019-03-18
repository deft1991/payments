create schema payment;

CREATE TABLE payment.PAYMENT_DATA
(
  -- only integer type can be autoincremented, got VARCHAR(36)
  ID          VARCHAR(36) PRIMARY KEY NOT NULL,
  SENDER_ID   VARCHAR(36)             NOT NULL,
  RECEIVER_ID VARCHAR(36)             NOT NULL,
  AMOUNT      BIGINT                  NOT NULL
);
CREATE UNIQUE INDEX PAYMENT_DATA_ID_uindex
  ON payment.PAYMENT_DATA (ID);
COMMENT ON TABLE payment.PAYMENT_DATA IS 'Информация о платежах';