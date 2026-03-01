CREATE TABLE entrepreneur (
    id BIGSERIAL PRIMARY KEY,
    enterprise_name VARCHAR(255) NOT NULL,
    entrepreneur_name VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    operating_segment VARCHAR(50) NOT NULL,
    contact VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_entrepreneur_status ON entrepreneur(status);
CREATE INDEX idx_entrepreneur_operating_segment ON entrepreneur(operating_segment);
CREATE INDEX idx_entrepreneur_city ON entrepreneur(city);
CREATE INDEX idx_entrepreneur_enterprise_name ON entrepreneur(enterprise_name);
