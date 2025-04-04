CREATE TABLE IF NOT EXISTS task (
    id SERIAL PRIMARY KEY,
    title text NOT NULL UNIQUE,
    description text NOT NULL,
    completed BOOLEAN NOT NULL
);