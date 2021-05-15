DELETE
FROM election
WHERE 1 = 1;
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
INSERT INTO department(id, name)
VALUES (NULL, 'Online');

-------------------------------Eleições-----------------------------------
--- ================================= Eleições a decorrer ==================
INSERT INTO election(id, title, type, description, begin_date, end_date)
VALUES (1, 'NEI -> Estudantes, no dei', 'Estudante', 'Eleicoes do NEI', '2021-03-20 00:00:00', '2021-05-31 23:59:59');
INSERT INTO election_department(election_id, department_id)
VALUES (1, 6);

INSERT INTO election(id, title, type, description, begin_date, end_date)
VALUES (2, 'Eleição -> Estudantes, atual, em vários departamentos -> Estudantes', 'Estudante',
        'Eleição para testar eleição em vários departamentos', '2021-03-20 00:00:00', '2021-05-31 23:59:59');
INSERT INTO election_department(election_id, department_id)
VALUES (2, -1);

--- ============================ Eleições passadas =========================
INSERT INTO election(id, title, type, description, begin_date, end_date)
VALUES (3, 'Eleição -> Estudantes, terminada em vários departamentos', 'Estudante',
        'Testar eleição que terminou e não tem departamento restringido', '2021-03-20 00:00:00', '2021-03-31 23:59:59');
INSERT INTO election_department(election_id, department_id)
VALUES (3, -1);

INSERT INTO election(id, title, type, description, begin_date, end_date)
VALUES (4, 'Eleição -> Estudantes, terminada e restringida -> Arquitetura', 'Estudante',
        'Testar eleição que terminou e tem departamento restringido (Arquitetura)', '2021-03-20 00:00:00',
        '2021-03-31 23:59:59');
INSERT INTO election_department(election_id, department_id)
VALUES (4, 1);

--- =========================== Eleições futuras ===========================
INSERT INTO election(id, title, type, description, begin_date, end_date)
VALUES (5, 'Eleição -> Estudantes, por começar em vários departamentos', 'Estudante',
        'Testar eleição que ainda não começou e não tem departamento restringido', '2021-04-20 00:00:00',
        '2021-04-31 23:59:59');
INSERT INTO election_department(election_id, department_id)
VALUES (5, -1);

INSERT INTO election(id, title, type, description, begin_date, end_date)
VALUES (6, 'Eleição -> Estudantes, por começar restringida -> Ciências da Terra', 'Estudante',
        'Testar eleição que ainda não começou e tem departamento restringido (Ciências da Terra)',
        '2021-04-20 00:00:00', '2021-04-31 23:59:59');
INSERT INTO election_department(election_id, department_id)
VALUES (6, 2);

------------------ O mesmo que o anterior mas para docentes ----------------------
--- ================================= Eleições a decorrer ==================
INSERT INTO election(id, title, type, description, begin_date, end_date)
VALUES (7, 'NEI -> Docentes, no dei', 'Docente', 'Eleicoes do NEI', '2021-03-20 00:00:00', '2021-05-31 23:59:59');
INSERT INTO election_department(election_id, department_id)
VALUES (7, 6);

INSERT INTO election(id, title, type, description, begin_date, end_date)
VALUES (8, 'Eleição -> Docentes, atual, em vários departamentos', 'Docente',
        'Eleição para testar eleição em vários departamentos', '2021-03-20 00:00:00', '2021-05-31 23:59:59');
INSERT INTO election_department(election_id, department_id)
VALUES (8, -1);

--- ============================ Eleições passadas =========================
INSERT INTO election(id, title, type, description, begin_date, end_date)
VALUES (9, 'Eleição -> Docentes, terminada em vários departamentos', 'Docente',
        'Testar eleição que terminou e não tem departamento restringido', '2021-03-20 00:00:00', '2021-03-31 23:59:59');
INSERT INTO election_department(election_id, department_id)
VALUES (9, -1);

INSERT INTO election(id, title, type, description, begin_date, end_date)
VALUES (10, 'Eleição -> Docentes, terminada e restringida -> Arquitetura', 'Docente',
        'Testar eleição que terminou e tem departamento restringido (Arquitetura)', '2021-03-20 00:00:00',
        '2021-03-31 23:59:59');
INSERT INTO election_department(election_id, department_id)
VALUES (10, 1);

--- =========================== Eleições futuras ===========================
INSERT INTO election(id, title, type, description, begin_date, end_date)
VALUES (11, 'Eleição -> Docentes, por começar em vários departamentos', 'Docente',
        'Testar eleição que ainda não começou e não tem departamento restringido', '2021-04-20 00:00:00',
        '2021-04-31 23:59:59');
INSERT INTO election_department(election_id, department_id)
VALUES (11, -1);

INSERT INTO election(id, title, type, description, begin_date, end_date)
VALUES (12, 'Eleição -> Docentes, por começar restringida -> Ciências da Terra', 'Docente',
        'Testar eleição que ainda não começou e tem departamento restringido (Ciências da Terra)',
        '2021-04-20 00:00:00', '2021-04-31 23:59:59');
INSERT INTO election_department(election_id, department_id)
VALUES (12, 2);

-------------------------- O mesmo que as anteriores, mas com funcionários --------------------
--- ================================= Eleições a decorrer ==================
INSERT INTO election(id, title, type, description, begin_date, end_date)
VALUES (13, 'NEI -> Funcionários, no dei', 'Funcionário', 'Eleicoes do NEI', '2021-03-20 00:00:00',
        '2021-05-31 23:59:59');
INSERT INTO election_department(election_id, department_id)
VALUES (13, 6);

INSERT INTO election(id, title, type, description, begin_date, end_date)
VALUES (14, 'Eleição -> Funcionários, atual, em vários departamentos', 'Funcionário',
        'Eleição para testar eleição em vários departamentos', '2021-03-20 00:00:00', '2021-05-31 23:59:59');
INSERT INTO election_department(election_id, department_id)
VALUES (14, -1);

--- ============================ Eleições passadas =========================
INSERT INTO election(id, title, type, description, begin_date, end_date)
VALUES (15, 'Eleição -> Funcionários, terminada em vários departamentos', 'Funcionário',
        'Testar eleição que terminou e não tem departamento restringido', '2021-03-20 00:00:00', '2021-03-31 23:59:59');
INSERT INTO election_department(election_id, department_id)
VALUES (15, -1);

INSERT INTO election(id, title, type, description, begin_date, end_date)
VALUES (16, 'Eleição -> Funcionários, terminada e restringida -> Arquitetura', 'Funcionário',
        'Testar eleição que terminou e tem departamento restringido (Arquitetura)', '2021-03-20 00:00:00',
        '2021-03-31 23:59:59');
INSERT INTO election_department(election_id, department_id)
VALUES (16, 1);

--- =========================== Eleições futuras ===========================
INSERT INTO election(id, title, type, description, begin_date, end_date)
VALUES (17, 'Eleição -> Docentes, por começar em vários departamentos', 'Docente',
        'Testar eleição que ainda não começou e não tem departamento restringido', '2021-04-20 00:00:00',
        '2021-04-31 23:59:59');
INSERT INTO election_department(election_id, department_id)
VALUES (17, -1);

INSERT INTO election(id, title, type, description, begin_date, end_date)
VALUES (18, 'Eleição -> Docentes, por começar restringida -> Ciências da Terra', 'Docente',
        'Testar eleição que ainda não começou e tem departamento restringido (Ciências da Terra)',
        '2021-04-20 00:00:00', '2021-04-31 23:59:59');
INSERT INTO election_department(election_id, department_id)
VALUES (18, 2);

---------------- Listas candidatas ----------------------------
-- Eleição 1
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA A1', 'Estudante', 1);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA B1', 'Estudante', 1);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA C1', 'Estudante', 1);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA D1', 'Estudante', 1);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA E1', 'Estudante', 1);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA F1', 'Estudante', 1);

--- Eleição 2
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA A2', 'Estudante', 2);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA B2', 'Estudante', 2);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA C2', 'Estudante', 2);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA D2', 'Estudante', 2);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA E2', 'Estudante', 2);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA F2', 'Estudante', 2);

--- Eleição 3
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA A3', 'Estudante', 3);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA B3', 'Estudante', 3);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA C3', 'Estudante', 3);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA D3', 'Estudante', 3);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA E3', 'Estudante', 3);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA F3', 'Estudante', 3);

--- Eleição 4
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA A4', 'Estudante', 4);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA B4', 'Estudante', 4);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA C4', 'Estudante', 4);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA D4', 'Estudante', 4);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA E4', 'Estudante', 4);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA F4', 'Estudante', 4);

--- Eleição 5
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA A5', 'Estudante', 5);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA B5', 'Estudante', 5);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA C5', 'Estudante', 5);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA D5', 'Estudante', 5);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA E5', 'Estudante', 5);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA F5', 'Estudante', 5);

--- Eleição 6
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA A6', 'Estudante', 6);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA B6', 'Estudante', 6);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA C6', 'Estudante', 6);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA D6', 'Estudante', 6);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA E6', 'Estudante', 6);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA F6', 'Estudante', 6);

--- Eleição 7
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA A7', 'Estudante', 7);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA B7', 'Estudante', 7);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA C7', 'Estudante', 7);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA D7', 'Estudante', 7);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA E7', 'Estudante', 7);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA F7', 'Estudante', 7);

--- Eleição 8
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA A8', 'Estudante', 8);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA B8', 'Estudante', 8);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA C8', 'Estudante', 8);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA D8', 'Estudante', 8);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA E8', 'Estudante', 8);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA F8', 'Estudante', 8);

--- Eleição 9
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA A9', 'Estudante', 9);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA B9', 'Estudante', 9);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA C9', 'Estudante', 9);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA D9', 'Estudante', 9);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA E9', 'Estudante', 9);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA F9', 'Estudante', 9);

--- Eleição 10
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA A10', 'Estudante', 10);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA B10', 'Estudante', 10);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA C10', 'Estudante', 10);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA D10', 'Estudante', 10);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA E10', 'Estudante', 10);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA F10', 'Estudante', 10);

--- Eleição 11
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA A11', 'Estudante', 11);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA B11', 'Estudante', 11);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA C11', 'Estudante', 11);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA D11', 'Estudante', 11);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA E11', 'Estudante', 11);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA F11', 'Estudante', 11);

--- Eleição 12
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA A12', 'Estudante', 12);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA B12', 'Estudante', 12);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA C12', 'Estudante', 12);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA D12', 'Estudante', 12);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA E12', 'Estudante', 12);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA F12', 'Estudante', 12);

--- Eleição 13
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA A13', 'Estudante', 13);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA B13', 'Estudante', 13);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA C13', 'Estudante', 13);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA D13', 'Estudante', 13);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA E13', 'Estudante', 13);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA F13', 'Estudante', 13);

--- Eleição 14
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA A14', 'Estudante', 14);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA B14', 'Estudante', 14);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA C14', 'Estudante', 14);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA D14', 'Estudante', 14);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA E14', 'Estudante', 14);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA F14', 'Estudante', 14);

--- Eleição 15
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA A15', 'Estudante', 15);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA B15', 'Estudante', 15);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA C15', 'Estudante', 15);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA D15', 'Estudante', 15);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA E15', 'Estudante', 15);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA F15', 'Estudante', 15);

--- Eleição 16
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA A16', 'Estudante', 16);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA B16', 'Estudante', 16);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA C16', 'Estudante', 16);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA D16', 'Estudante', 16);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA E16', 'Estudante', 16);
INSERT INTO candidacy(id, name, type, election_id)
VALUES (NULL, 'LISTA F16', 'Estudante', 16);

----- Pessoas (password)
--- Rita -> rita, estudante, ndep 2, cc 11111111
--- Dylan -> dylan, estudante, ndep 1, cc 22222222
--- Nuno Markl -> 1986, Docente, ndep 2, cc 33333333
--- Matias Correios -> dux, Estudante, ndep 5, cc 44444444
--- Marco Paulo -> cantor, Funcionario, ndep 5, cc 55555555
--- Joaquim Jorge -> gajodajunta, Docente, ndep 2, cc 66666666
--- Cristina Ferreira -> estacerta, Funcionario, ndep 1, 77777777
--- Manuel Luis -> goucha, Funcionario, ndep 6, cc 88888888

INSERT INTO person(name, cc_number, cc_validity, address, phone, job, password, department_id)
VALUES ('Rita', 12191119, '2025-01-01', 'Rua dos BV', 934100200, 'Estudante', 'qwertz', 8);
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
VALUES (3, 5);
--- Elição que não está restrita a um único departamento
INSERT INTO election(id, title, type, description, begin_date, end_date)
VALUES (NULL, 'Não Restrito', 'Docente', 'Eleicoes dos Docentes da UC', '2021-03-20 00:00:00',
        '2022-01-01 00:00:00');
INSERT INTO election_department(election_id, department_id)
VALUES (14, -1);
INSERT INTO election_department(election_id, department_id)
VALUES (4001, 1);
--ELEICAO PASSADA
INSERT INTO election(id, title, type, description, begin_date, end_date)
VALUES (NULL, 'BestDEECFuncionarios', 'Funcionário', 'Eleicoes dos Funcionarios do DEEC', '2022-01-01 00:00:00',
        '2023-01-01 00:00:00');
INSERT INTO election_department(election_id, department_id)
VALUES (13, 5);
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
VALUES ('Cristina Ferreira', 55555557, '2025-01-02', 'Rua da TVI', 934555557, 'Docente', '-664788032', 5);
--password: estacerta
--- Pessoa para testar que pode votar numa eleição que nao esta restrita a um departamento
INSERT INTO person(name, cc_number, cc_validity, address, phone, job, password, department_id)
VALUES ('Rita Rodrigues', 55555558, '2025-01-02', 'Rua da TVI', 934555557, 'Docente', '-861301537', 5);
--password: estacerta

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

SELECT *
FROM voting_record;
