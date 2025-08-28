-- Delete the database if it already exists
DROP DATABASE IF EXISTS url_db;

-- Create new schema
CREATE DATABASE IF NOT EXISTS url_db;

USE url_db;
CREATE TABLE urls (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  short_code VARCHAR(12) UNIQUE NOT NULL,
  long_url TEXT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  expires_at TIMESTAMP,
  owner_id VARCHAR(64),
  is_active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE INDEX idx_urls_expires_at ON urls(expires_at);
