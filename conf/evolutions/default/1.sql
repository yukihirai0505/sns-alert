# --- !Ups

CREATE TABLE "user" (
  id Serial,
  email VARCHAR(500) UNIQUE NOT NULL ,
  password VARCHAR(1024) NOT NULL,
  keywords TEXT,
  instagram_id VARCHAR(1024),
  instagram_access_token VARCHAR(100),
  facebook_id VARCHAR(1024),
  facebook_access_token VARCHAR(512),
  twitter_id VARCHAR(1024),
  twitter_access_token VARCHAR(255),
  PRIMARY KEY (id)
);

CREATE TABLE "splash_post"
(
  id            SERIAL       NOT NULL
    CONSTRAINT splash_post_pkey
    PRIMARY KEY,
  user_id       BIGINT       NOT NULL,
  post_id       VARCHAR(100) NOT NULL,
  message       VARCHAR(500) NOT NULL,
  sns_type      INTEGER      NOT NULL,
  post_datetime TIMESTAMP    NOT NULL
);