drop table if exists person;
drop table if exists election;
drop table if exists election_person;

CREATE TABLE person
(
    funcao     VARCHAR(512)  NOT NULL,
    password   VARCHAR(512)  NOT NULL,
    depart     VARCHAR(512)  NOT NULL,
    phone      BIGINT UNIQUE,
    address    CHAR(255)     NOT NULL,
    numcc      BIGINT UNIQUE NOT NULL,
    validadecc DATE          NOT NULL,
    PRIMARY KEY (numcc)
);

CREATE TABLE election
(
    inidate     BIGINT       NOT NULL,
    fimdate     BIGINT       NOT NULL,
    title       VARCHAR(512) NOT NULL,
    description VARCHAR(512) NOT NULL
);

CREATE TABLE election_person
(
    person_numcc BIGINT,
    PRIMARY KEY (person_numcc),
    FOREIGN KEY (person_numcc) REFERENCES Person (numCC)
);
