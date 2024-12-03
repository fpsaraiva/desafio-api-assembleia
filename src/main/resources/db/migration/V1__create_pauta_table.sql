create schema if not exists assembleia;

create table assembleia.pauta (
    id uuid primary key default gen_random_uuid(),
    titulo varchar(200) not null
);
