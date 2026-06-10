INSERT INTO turmas (
    id,
    disciplina_id,
    codigo_turma,
    semestre,
    professor,
    vagas_total,
    vagas_ocupadas,
    status
) VALUES
(
    1,
    1,
    'ADS-2026-1',
    '2026/1',
    'Carlos Silva',
    40,
    5,
    'DISPONIVEL'
),
(
    2,
    2,
    'ES-2026-1',
    '2026/1',
    'Mariana Souza',
    30,
    10,
    'DISPONIVEL'
),
(
    3,
    3,
    'BD-2026-1',
    '2026/1',
    'João Pereira',
    25,
    25,
    'LOTADA'
);

-- Os INSERTs acima usam IDs explícitos (1, 2, 3). No H2, isso NÃO avança o
-- contador da coluna IDENTITY, então a próxima turma criada via API tentaria
-- reutilizar o ID 1 e colidiria com a PK. Reposicionamos a sequência para 4.
ALTER TABLE turmas ALTER COLUMN id RESTART WITH 4;