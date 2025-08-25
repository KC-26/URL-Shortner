CREATE TABLE urls (
  id BIGSERIAL PRIMARY KEY,
  short_code VARCHAR(12) UNIQUE NOT NULL,
  long_url TEXT NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  expires_at TIMESTAMPTZ,
  owner_id VARCHAR(64),
  is_active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE INDEX idx_urls_expires_at ON urls(expires_at) WHERE is_active = true;
