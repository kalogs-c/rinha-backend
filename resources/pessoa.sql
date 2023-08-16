-- :name create-pessoa-table
-- :command :execute
-- :results :raw
-- :doc Cria a tabela pessoa
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE IF NOT EXISTS pessoa (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    apelido VARCHAR(32) UNIQUE NOT NULL,
    nome VARCHAR(100) NOT NULL,
    nascimento DATE NOT NULL,
    stack VARCHAR(32)[]
);

-- :name drop-pessoa-table! :!
-- :doc Dropa a tabela, se exitir
DROP TABLE IF EXISTS pessoa

-- :name get-pessoa :? :1
-- :doc Retorna a pessoa baseado no ID
SELECT * FROM pessoa
WHERE id = :id

-- :name list-pessoas :? :*
-- :doc Retorna todas as pessoas
SELECT * FROM pessoa

-- :name count-pessoas :1
-- :doc Retorna a contagem total de registros na tabela pessoa.
SELECT count(*) FROM pessoa;

-- :name insert-pessoa :1
-- :doc Insere um novo registro na tabela pessoa.
INSERT INTO pessoa (apelido, nome, nascimento, stack)
VALUES (:apelido, :nome, :nascimento, :stack)
RETURNING id;

-- :name search-pessoa :? :*
-- :doc Busca registros na tabela pessoa onde algum dos campos contém a substring especificada.
SELECT * FROM pessoa
WHERE apelido ILIKE '%' || :substring || '%'
   OR nome ILIKE '%' || :substring || '%'
   OR array_to_string(stack, ',') ILIKE '%' || :substring || '%';