drop table if exists assembleia.voto;

create table assembleia.voto (
    id uuid primary key default gen_random_uuid(),
    sessao_id uuid not null,
    id_associado uuid not null,
    voto varchar(10) not null,
    constraint fk_sessao foreign key (sessao_id) references assembleia.sessao(id),
    constraint unique_associado_sessao unique (id_associado, sessao_id)
);