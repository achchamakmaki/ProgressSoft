-- V1__create_fx_deals_table.sql
DROP TABLE IF EXISTS fx_deals CASCADE;

CREATE TABLE fx_deals (
                          id BIGSERIAL PRIMARY KEY,
                          deal_unique_id VARCHAR(50) NOT NULL,
                          from_currency VARCHAR(3) NOT NULL,
                          to_currency VARCHAR(3) NOT NULL,
                          deal_timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
                          amount NUMERIC(20,8) NOT NULL CHECK (amount > 0),

                          CONSTRAINT uk_deal_unique_id UNIQUE (deal_unique_id),
                          CONSTRAINT chk_from_currency_iso CHECK (from_currency ~ '^[A-Z]{3}$'),
    CONSTRAINT chk_to_currency_iso CHECK (to_currency ~ '^[A-Z]{3}$')
);

-- Index pour les perfs
CREATE INDEX idx_fx_deals_timestamp ON fx_deals(deal_timestamp);
CREATE INDEX idx_fx_deals_currencies ON fx_deals(from_currency, to_currency);