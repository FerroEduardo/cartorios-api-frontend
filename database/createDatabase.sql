create table if not exists cartorios_api_dados
(
    id                       serial       not null,
    uf                       varchar(2)   not null,
    cnpj                     varchar(18),
    nome_oficial             varchar(150) not null,
    nome_fantasia            varchar(150),
    endereco                 varchar(120),
    bairro                   varchar(50),
    municipio                varchar(50)  not null,
    cep                      varchar(9),
    nome_do_titular          varchar(110),
    nome_do_substituto       varchar(110),
    nome_do_juiz             varchar(110),
    homepage                 varchar(90),
    email                    varchar(120),
    telefone                 varchar(40),
    fax                      varchar(40),
    horario_de_funcionamento varchar(150),
    area_de_abrangencia      varchar(720),
    atribuicoes              varchar(550)
    );

create table if not exists cartorios_api_key
(
    id      serial      not null,
    user_id integer     not null unique,
    api_key   varchar(36) unique
    );

create table if not exists cartorios_api_usuarios
(
    id                  serial       not null,
    username            varchar(60)  not null unique,
    email               varchar(120) not null unique,
    password            varchar(60)  not null,
    account_expired     boolean      not null,
    account_locked      boolean      not null,
    credentials_expired boolean      not null,
    account_enabled     boolean      not null,
    user_role           varchar(30)  not null,
    api_access          boolean      not null
    );

-- Cria um usuário administrador 'admin' com senha '12345'
insert into cartorios_api_usuarios (username, email, password, account_expired, account_locked, credentials_expired, account_enabled, user_role, api_access)
values  ('admin', 'admin@email.com', '$2a$10$1F2SAolovaMN5ViBWfkE7OeHwl8VoYt3g7wGbHkyY/uPcg98gDgjG', false, false, false, true, 'ROLE_USER,ROLE_ADMIN', false);