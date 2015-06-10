# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table "datas" ("id" INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"feed_id" INTEGER NOT NULL,"version" BIGINT NOT NULL,"network" VARCHAR(254) NOT NULL,"media" VARCHAR(254) NOT NULL,"url" VARCHAR(254) NOT NULL,"text" VARCHAR(254) NOT NULL,"date" TIMESTAMP NOT NULL);
create table "feed_relates" ("parent_feed_id" INTEGER NOT NULL,"child_feed_id" INTEGER NOT NULL);
alter table "feed_relates" add constraint "feed_relates_pk_a" primary key("parent_feed_id","child_feed_id");
create table "feeds" ("id" INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"version" BIGINT NOT NULL,"category" VARCHAR(254) NOT NULL,"name" VARCHAR(254) NOT NULL,"facebook_api" VARCHAR(254),"twitter_api" VARCHAR(254),"instagram_api" VARCHAR(254),"youtube_api" VARCHAR(254),"date_created" TIMESTAMP NOT NULL,"date_updated" TIMESTAMP NOT NULL,"location" VARCHAR(254) NOT NULL,"latitude" DOUBLE PRECISION NOT NULL,"longitude" DOUBLE PRECISION NOT NULL);
create table "reset_password_tokens" ("user_id" INTEGER NOT NULL PRIMARY KEY,"password_token" VARCHAR(254) NOT NULL);
create table "users" ("id" INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"version" BIGINT NOT NULL,"role" VARCHAR(254) NOT NULL,"email" VARCHAR(254) NOT NULL,"password" VARCHAR(254) NOT NULL,"date_created" TIMESTAMP NOT NULL,"date_updated" TIMESTAMP NOT NULL,"location" VARCHAR(254) NOT NULL,"latitude" DOUBLE PRECISION NOT NULL,"longitude" DOUBLE PRECISION NOT NULL);
create unique index "users_idx_a" on "users" ("email");
create table "users_feeds" ("user_id" INTEGER NOT NULL,"feed_id" INTEGER NOT NULL);
alter table "users_feeds" add constraint "pk_a" primary key("user_id","feed_id");
alter table "datas" add constraint "datas_feed_fk" foreign key("feed_id") references "feeds"("id") on update NO ACTION on delete CASCADE;
alter table "feed_relates" add constraint "feed_relates_feed_fk" foreign key("child_feed_id") references "feeds"("id") on update NO ACTION on delete CASCADE;
alter table "feed_relates" add constraint "feed_relates_parent_feed_fk" foreign key("parent_feed_id") references "feeds"("id") on update NO ACTION on delete CASCADE;
alter table "reset_password_tokens" add constraint "reset_password_tokens_user_fk" foreign key("user_id") references "users"("id") on update NO ACTION on delete NO ACTION;
alter table "users_feeds" add constraint "users_feeds_feed_fk" foreign key("feed_id") references "feeds"("id") on update NO ACTION on delete CASCADE;

# --- !Downs

alter table "users_feeds" drop constraint "users_feeds_feed_fk";
alter table "reset_password_tokens" drop constraint "reset_password_tokens_user_fk";
alter table "feed_relates" drop constraint "feed_relates_feed_fk";
alter table "feed_relates" drop constraint "feed_relates_parent_feed_fk";
alter table "datas" drop constraint "datas_feed_fk";
alter table "users_feeds" drop constraint "pk_a";
drop table "users_feeds";
drop table "users";
drop table "reset_password_tokens";
drop table "feeds";
alter table "feed_relates" drop constraint "feed_relates_pk_a";
drop table "feed_relates";
drop table "datas";
