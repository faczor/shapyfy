-- Waitlist signup table for collecting early access registrations
CREATE TABLE waitlist_signup (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    platform VARCHAR(50),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_waitlist_email UNIQUE (email)
);

-- Index for faster email lookups
CREATE INDEX idx_waitlist_email ON waitlist_signup(email);
