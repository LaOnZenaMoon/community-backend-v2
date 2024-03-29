drop table if exists oauth_client_details;
drop table if exists oauth_client_token;
drop table if exists oauth_access_token;
drop table if exists oauth_refresh_token;
drop table if exists oauth_code;
drop table if exists oauth_approvals;

create table oauth_client_details
(
    client_id               VARCHAR(255) PRIMARY KEY,
    resource_ids            VARCHAR(255),
    client_secret           VARCHAR(255),
    scope                   VARCHAR(255),
    authorized_grant_types  VARCHAR(255),
    web_server_redirect_uri VARCHAR(255),
    authorities             VARCHAR(255),
    access_token_validity   INTEGER,
    refresh_token_validity  INTEGER,
    additional_information  VARCHAR(4096),
    autoapprove             VARCHAR(255)
);

create table oauth_client_token
(
    token_id          VARCHAR(255),
    token             MEDIUMBLOB,
    authentication_id VARCHAR(255) PRIMARY KEY,
    user_name         VARCHAR(255),
    client_id         VARCHAR(255)
);

create table oauth_access_token
(
    token_id          VARCHAR(255),
    token             MEDIUMBLOB,
    authentication_id VARCHAR(255) PRIMARY KEY,
    user_name         VARCHAR(255),
    client_id         VARCHAR(255),
    authentication    MEDIUMBLOB,
    refresh_token     VARCHAR(255)
);

create table oauth_refresh_token
(
    token_id       VARCHAR(255),
    token          MEDIUMBLOB,
    authentication MEDIUMBLOB
);

create table oauth_code
(
    code           VARCHAR(255),
    authentication MEDIUMBLOB
);

create table oauth_approvals
(
    userId         VARCHAR(255),
    clientId       VARCHAR(255),
    scope          VARCHAR(255),
    status         VARCHAR(10),
    expiresAt      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    lastModifiedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

insert into oauth_client_details(client_id, resource_ids, client_secret, scope, authorized_grant_types,
                                 web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity,
                                 additional_information, autoapprove)
values ('laonzenamoon_client_id', NULL, '$2a$10$yBX91.BYfwjPRY0qnlFjdO.c/b6unLnGzd.903Rmi02WoDFJw9Tr6',
        'read,write', 'password,refresh_token', 'http://localhost:8082/oauth2/callback', 'ROLE_USER', '360000', '50000',
        NULL, NULL);

create table users (
                       user_id bigint generated by default as identity,
                       created_date timestamp,
                       last_modified_date timestamp,
                       created_by bigint,
                       is_use boolean,
                       last_modified_by bigint,
                       password varchar(255) not null,
                       login_id varchar(50) not null,
                       name varchar(50) not null,
                       role varchar(255),
                       primary key (user_id)
);

insert into users (login_id, is_use, name, role, password) values ('system', 1, '시스템', 'ROLE_SYSTEM', '$2a$10$HR457uqqM09pKTlUXkVuQepLJLQpK8NJ9J1Wwp.N1gIn3SsNHdtfK');