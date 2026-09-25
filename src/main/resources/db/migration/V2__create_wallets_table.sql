CREATE TABLE wallets (
                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                         user_id UUID NULL REFERENCES users(id),
                         wallet_number VARCHAR(10) NOT NULL UNIQUE,
                         type VARCHAR(10) NOT NULL,
                         balance BIGINT NOT NULL DEFAULT 0,
                         version BIGINT NOT NULL DEFAULT 0,
                         created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
                         CONSTRAINT chk_wallet_type CHECK (type IN ('USER', 'SYSTEM')),
                         CONSTRAINT chk_balance_nonnegative CHECK (type = 'SYSTEM' OR balance >= 0)
);

CREATE INDEX idx_wallets_user_id ON wallets (user_id);

INSERT INTO wallets (wallet_number, type, balance)
VALUES
    ('0000000001', 'SYSTEM', 0),
    ('0000000002', 'SYSTEM', 0);