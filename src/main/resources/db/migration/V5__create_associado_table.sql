create table assembleia.associado (
    id uuid primary key default gen_random_uuid(),
    nome varchar(100) not null,
    cpf varchar(20) not null
);
