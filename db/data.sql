--Election1
INSERT INTO department(id,name) VALUES (NULL,'DEI');
INSERT INTO election(id,title, type, description, begin_date, end_date) VALUES (NULL,'NEI','students','Eleicoes do NEI','2021-03-20 00:00:00','2021-05-31 23:59:59');
INSERT INTO election_department(election_id,department_id) VALUES (1,1);
INSERT INTO candidacy(id,name,type,election_id) VALUES (NULL,'LISTA A','student',1);
INSERT INTO candidacy(id,name,type,election_id) VALUES (NULL,'LISTA B','student',1);
INSERT INTO person(cc_number,cc_validity,address,phone,job,password,department_id) VALUES (11111111,2025-01-01,'Rua dos BV',934100200,'Estudante','qwertz',1);
INSERT INTO person(cc_number,cc_validity,address,phone,job,password,department_id) VALUES (22222222,2025-01-01,'Rua da Figueira',934300400,'Estudante','qwerty',1);
INSERT INTO candidacy_person(candidacy_id, person_cc_number) VALUES (1,11111111);
INSERT INTO candidacy_person(candidacy_id, person_cc_number) VALUES (1,22222222);

INSERT INTO department(id,name) VALUES(NULL, 'Departamento de Arquitetura');
INSERT INTO department(id,name) VALUES(NULL, 'Departamento de Ciências da Terra');
INSERT INTO department(id,name) VALUES(NULL, 'Departamento de Ciências da Vida');
INSERT INTO department(id,name) VALUES(NULL, 'Departamento de Engenharia Civil');
INSERT INTO department(id,name) VALUES(NULL, 'Departamento de Engenharia Eletrotécnica e de Computadores');
INSERT INTO department(id,name) VALUES(NULL, 'Departamento de Engenharia Informática');
INSERT INTO department(id,name) VALUES(NULL, 'Departamento de Engenharia Mecânica');
INSERT INTO department(id,name) VALUES(NULL, 'Departamento de Engenharia Química');
INSERT INTO department(id,name) VALUES(NULL, 'Departamento de Física');
INSERT INTO department(id,name) VALUES(NULL, 'Departamento de Matemática');
INSERT INTO department(id,name) VALUES(NULL, 'Departamento de Química');



