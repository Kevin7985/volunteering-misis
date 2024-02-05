CREATE TABLE IF NOT EXISTS users (
    id UUID DEFAULT uuid_generate_v4(),
    misis_id VARCHAR(100),
    email VARCHAR(500) NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    middle_name VARCHAR(255),
    CONSTRAINT PK_USER PRIMARY KEY (id)
);