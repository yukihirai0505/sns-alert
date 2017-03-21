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
