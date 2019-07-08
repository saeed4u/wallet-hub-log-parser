create schema if not exists "log-parser" collate utf8_general_ci;

create table if not exists "log-parser".hibernate_sequence
(
  next_val bigint null
)
  engine=MyISAM;

create table if not exists "log-parser".log_comments
(
  ip varchar(255) not null
    primary key,
  comment varchar(255) null,
  count bigint null,
  end_date datetime null,
  start_date datetime null
)
  engine=MyISAM;

create table if not exists "log-parser".logs
(
  id bigint auto_increment
    primary key,
  ip varchar(255) null,
  request varchar(255) null,
  request_date_time datetime null,
  request_status_code varchar(255) null,
  user_agent varchar(255) null
)
  engine=MyISAM;

create index IDX5q3vx6uwv6yr9tqk730bw7ivd
  on "log-parser".logs (ip, request_date_time);

