create table assembleia.voto (
    id uuid primary key default gen_random_uuid(),
    pauta_id uuid not null,
    id_associado uuid not null,
    voto varchar(10) not null,
    constraint fk_pauta foreign key (pauta_id) references assembleia.pauta(id),
    constraint unique_associado_pauta unique (id_associado, pauta_id)
);
