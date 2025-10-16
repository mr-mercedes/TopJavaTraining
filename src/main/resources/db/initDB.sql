DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS meals;
DROP TABLE IF EXISTS users;
DROP SEQUENCE IF EXISTS global_seq;

CREATE SEQUENCE global_seq START WITH 100000;

CREATE TABLE users
(
    id               INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    name             VARCHAR                           NOT NULL,
    email            VARCHAR                           NOT NULL,
    password         VARCHAR                           NOT NULL,
    registered       TIMESTAMP           DEFAULT now() NOT NULL,
    enabled          BOOL                DEFAULT TRUE  NOT NULL,
    calories_per_day INTEGER             DEFAULT 2000  NOT NULL
);
COMMENT ON TABLE users IS 'Users data';
CREATE UNIQUE INDEX IF NOT EXISTS users_unique_email_idx ON users (email);
COMMENT ON INDEX users_unique_email_idx IS 'For quick search by email';


CREATE TABLE user_role
(
    user_id INTEGER NOT NULL,
    role    VARCHAR NOT NULL,
    CONSTRAINT user_roles_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
COMMENT ON TABLE user_role IS 'Roles and their map with users';

CREATE TABLE meals
(
    id          INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    date_time   TIMESTAMP WITHOUT TIME ZONE,
    description TEXT,
    calories    INTEGER,
    user_id     INTEGER NOT NULL,
    CONSTRAINT meal_user_idx UNIQUE (user_id, date_time),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
COMMENT ON TABLE meals IS 'User meals';