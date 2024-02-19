CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS users (
    id UUID DEFAULT uuid_generate_v4(),
    misis_id VARCHAR(100),
    email VARCHAR(500) NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    middle_name VARCHAR(255),
    about VARCHAR(5000),
    is_moderator BOOLEAN,
    is_staff BOOLEAN,
    CONSTRAINT PK_USER PRIMARY KEY (id),
    CONSTRAINT UQ_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS skills (
    id UUID DEFAULT uuid_generate_v4(),
    title VARCHAR(255),
    CONSTRAINT PK_SKILL PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS user_skills (
    id UUID DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL,
    skill_id UUID NOT NULL,
    CONSTRAINT PK_USER_SKILL PRIMARY KEY (id),
    CONSTRAINT FK_USER_FOR_USER_SKILL FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT FK_SKILL_FOR_USER_SKILL FOREIGN KEY (skill_id) REFERENCES skills (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS categories (
    id UUID DEFAULT uuid_generate_v4(),
    title VARCHAR(255) NOT NULL,
    CONSTRAINT PK_CATEGORY PRIMARY KEY (id)
);