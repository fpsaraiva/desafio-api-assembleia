create table assembleia.sessao (
    id uuid primary key default gen_random_uuid(),
    pauta_id uuid not null,
    duracao_em_minutos int,
    inicio_sessao timestamp,
    fim_sessao timestamp,
    constraint fk_pauta foreign key (pauta_id) references assembleia.pauta (id)
);
