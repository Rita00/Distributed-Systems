DELETE FROM election WHERE 1 = 1;
DELETE
FROM election_department
WHERE 1 = 1;
DELETE
FROM candidacy
WHERE 1 = 1;
DELETE
FROM person
WHERE 1 = 1;
DELETE
FROM candidacy_person
WHERE 1 = 1;
DELETE
FROM department
WHERE 1 = 1;

INSERT INTO department(id, name)
VALUES (NULL, 'Departamento de Arquitetura');
INSERT INTO department(id, name)
VALUES (NULL, 'Departamento de Ciências da Terra');
INSERT INTO department(id, name)
VALUES (NULL, 'Departamento de Ciências da Vida');
INSERT INTO department(id, name)
VALUES (NULL, 'Departamento de Engenharia Civil');
INSERT INTO department(id, name)
VALUES (NULL, 'Departamento de Engenharia Eletrotécnica e de Computadores');
INSERT INTO department(id, name)
VALUES (NULL, 'Departamento de Engenharia Informática');
INSERT INTO department(id, name)
VALUES (NULL, 'Departamento de Engenharia Mecânica');
INSERT INTO department(id, name)
VALUES (NULL, 'Departamento de Engenharia Química');
INSERT INTO department(id, name)
VALUES (NULL, 'Departamento de Física');
INSERT INTO department(id, name)
VALUES (NULL, 'Departamento de Matemática');
INSERT INTO department(id, name)
VALUES (NULL, 'Departamento de Química');

INSERT INTO election(id, title, type, description, begin_date, end_date)
VALUES (NULL, 'NEI', 'students', 'Eleicoes do NEI', '2021-03-20 00:00:00', '2021-05-31 23:59:59');
INSERT INTO election(id, title, type, description, begin_date, end_date)
VALUES (NULL, 'NEI', 'students', 'Eleicoes do NEI', '2021-03-20 00:00:00', '2021-05-31 23:59:59');
INSERT INTO election_department(election_id, department_id)
VALUES (1, -1);
INSERT INTO election_department(election_id, department_id)
VALUES (1, 4);
INSERT INTO election_department(election_id, department_id)
VALUES (1, 3);
INSERT INTO election_department(election_id, department_id)
VALUES (1, 5);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA A', 'student', 4001);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA B', 'student', 4001);
INSERT INTO person(name, cc_number, cc_validity, address, phone, job, password, department_id)
VALUES ('Rita', 12131113, '2025-01-01', 'Rua dos BV', 934100200, 'Estudante', 'qwertz', 8);
INSERT INTO person(name, cc_number, cc_validity, address, phone, job, password, department_id)
VALUES ('Dylan', 42222222, '2025-01-02', 'Rua da Figueira', 934300400, 'Estudante', 'qwerty', 8);
INSERT INTO person(name, cc_number, cc_validity, address, phone, job, password, department_id)
VALUES ('Francisco', 45222222, '2025-01-02', 'Rua da Figueira', 934300400, 'Estudante', 'qwerty', 8);
INSERT INTO candidacy_person(candidacy_id, person_cc_number)
VALUES (1, 11111111);
INSERT INTO candidacy_person(candidacy_id, person_cc_number)
VALUES (1, 22222222);

-------------------------------LISTAS DE TESTE COM RESPETIVAS PESSOAS------------------------
--ELEICAO POR INICIAR
INSERT INTO election(id, title, type, description, begin_date, end_date)
VALUES (10, 'NEEEC', 'Estudante', 'Eleicoes do NEEEC', '2021-03-20 00:00:00', '2021-03-26 23:59:59');
INSERT INTO election_department(election_id, department_id)
VALUES (10, 5);
--ELEICAO A DECORRER
INSERT INTO election(id, title, type, description, begin_date, end_date)
VALUES (4000, 'BestDEECDocentes', 'Docente', 'Eleicoes dos Docentes do DEEC', '2021-03-20 00:00:00',
        '2022-01-01 00:00:00');
INSERT INTO election_department(election_id, department_id)
VALUES (4000, 5);
INSERT INTO election_department(election_id, department_id)
VALUES(3, 5);
--- Elição que não está restrita a um único departamento
INSERT INTO election(id, title, type, description, begin_date, end_date)
VALUES (NULL, 'Não Restrito', 'Docente', 'Eleicoes dos Docentes da UC', '2021-03-20 00:00:00',
        '2022-01-01 00:00:00');
INSERT INTO election_department(election_id, department_id)
VALUES (14, -1);
INSERT INTO election_department(election_id, department_id)
VALUES(4001, 1);
--ELEICAO PASSADA
INSERT INTO election(id, title, type, description, begin_date, end_date)
VALUES (NULL, 'BestDEECFuncionarios', 'Funcionário', 'Eleicoes dos Funcionarios do DEEC', '2022-01-01 00:00:00',
        '2023-01-01 00:00:00');
INSERT INTO election_department(election_id, department_id)
VALUES (12, 5);
--pessoas
INSERT INTO person(name, cc_number, cc_validity, address, phone, job, password, department_id)
VALUES ('Nuno Markl', 55555551, '2025-01-01', 'Rua 1° Marco', 934555551, 'Funcionário', '-2096975966', 5);--password: 1986
INSERT INTO person(name, cc_number, cc_validity, address, phone, job, password, department_id)
VALUES ('Matias Correios', 55555553, '2025-01-02', 'Rua da AAC', 934555553, 'Estudante', '-344628535', 5);--password: dux
INSERT INTO person(name, cc_number, cc_validity, address, phone, job, password, department_id)
VALUES ('Marco Paulo', 55555554, '2025-01-02', 'Rua das Cancoes', 934555554, 'Estudante', '-1016627514', 5);--password: cantor
INSERT INTO person(name, cc_number, cc_validity, address, phone, job, password, department_id)
VALUES ('Joaquim Jorge', 55555555, '2025-01-02', 'Rua da Junta', 934555555, 'Estudante', '1063864532', 5);--password: gajodajunta
INSERT INTO person(name, cc_number, cc_validity, address, phone, job, password, department_id)
VALUES ('Cristina Ferreira', 55555557, '2025-01-02', 'Rua da TVI', 934555557, 'Docente', '-664788032', 5); --password: estacerta
--- Pessoa para testar que pode votar numa eleição que nao esta restrita a um departamento
INSERT INTO person(name, cc_number, cc_validity, address, phone, job, password, department_id)
VALUES ('Rita Rodrigues', 55555558, '2025-01-02', 'Rua da TVI', 934555557, 'Docente', '-861301537', 5); --password: estacerta

-------------------------------VOTOS REGISTADOS PARA A ELEICAO PASSADA------------------------
-- DEVE CONTER PARA eleicao do NEEEC 1 nulos 1 branco e 2 para a lista #1
--valido
INSERT INTO voting_record(vote_date, department, person_cc_number, election_id)
VALUES ('2021-03-21 00:00:00', 5, 55555553, 10);
INSERT INTO voting_record(vote_date, department, person_cc_number, election_id)
VALUES ('2021-03-21 00:00:00', 5, 55555554, 10);
--nulo porque antes da data
INSERT INTO voting_record(vote_date, department, person_cc_number, election_id)
VALUES ('2021-03-19 00:00:00', 5, 55555555, 10);

delete
FROM voting_record;

SELECT * FROM voting_record;
