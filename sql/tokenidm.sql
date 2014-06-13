DROP TABLE IF EXISTS domains;
CREATE TABLE domains (
  domainid    INTEGER PRIMARY KEY AUTOINCREMENT,
  name	      VARCHAR(128)	NOT NULL,
  description VARCHAR(128)	NOT NULL,
  enabled     INTEGER	        NOT NULL
);

DROP TABLE IF EXISTS users;
CREATE TABLE users (
  userid      INTEGER PRIMARY KEY AUTOINCREMENT,
  name        VARCHAR(128)      NOT NULL,
  email       VARCHAR(128)      NOT NULL,
  password    VARCHAR(128)      NOT NULL,
  description VARCHAR(128)      NOT NULL,
  enabled     INTEGER           NOT NULL
);

DROP TABLE IF EXISTS roles;
CREATE TABLE roles (
  roleid      INTEGER PRIMARY KEY AUTOINCREMENT,
  name        VARCHAR(128)      NOT NULL,
  description VARCHAR(128)      NOT NULL
);

DROP TABLE IF EXISTS grants;
CREATE TABLE grants (
  grantid     INTEGER PRIMARY KEY AUTOINCREMENT,
  description VARCHAR(128)      NOT NULL,
  domainid    INTEGER           NOT NULL,
  userid      INTEGER           NOT NULL,
  roleid      INTEGER           NOT NULL 
);
