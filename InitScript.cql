create table credentials
(
    id       bigint primary key,
    domain   text,
    password text,
    username text
);

create table migration
(
    id             bigint primary key,
    migrationstate text,
    mountpoints    list<text>,
    sourceid       bigint,
    targetcloudid  bigint
);

create table targetcloud
(
    id                 bigint primary key,
    cloudcredentialsid bigint,
    cloudtype          text,
    targetid           bigint
);

create table volume
(
    id         bigint primary key,
    mountpoint text,
    size       int
);

create table workload
(
    id            bigint primary key,
    credentialsid bigint,
    ip            text,
    volumeids     list<bigint>
);