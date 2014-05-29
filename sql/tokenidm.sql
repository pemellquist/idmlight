DROP TABLE IF EXISTS tenants;
CREATE TABLE tenants (
  id          INTEGER PRIMARY KEY AUTOINCREMENT,
  name	      VARCHAR(128)	NOT NULL,
  description VARCHAR(128)	NOT NULL,
  enabled     INTEGER	        NOT NULL
);

DROP TABLE IF EXISTS users;
CREATE TABLE users (
  id          INTEGER PRIMARY KEY AUTOINCREMENT,
  name        VARCHAR(128)      NOT NULL,
  email       VARCHAR(128)      NOT NULL,
  password    VARCHAR(128)      NOT NULL,
  description VARCHAR(128)      NOT NULL,
  enabled     INTEGER           NOT NULL
);
