# --- First database schema

create table image (
  id                        bigint not null,
  name                      varchar(255) not null,
  albumId                   bigint not null,
  constraint pk_image primary key (id))
;

create table album (
  id                        bigint not null,
  name                      varchar(255) not null,
  constraint pk_album primary key (id))
;
