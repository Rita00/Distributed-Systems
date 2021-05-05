select * from person;

select * from department;
select * from election;

select * from election_department;

drop table person;

select count(*) from department WHERE hasMulticastServer = 1;
SELECT * FROM department WHERE hasMulticastServer IS NULL AND id = 2;

SELECT * FROM department WHERE hasMulticastServer = 1;

select * from voting_record;
UPDATE department SET hasMulticastServer = null WHERE (hasmulticastserver = 1 OR hasmulticastserver = 0);

SELECT name FROM department WHERE id = 1;

select * from election_department;

SELECT id, name FROM department, election_department WHERE department.id = election_department.department_id AND election_department.election_id = 1;

select name from department where id = 1;

select election_id from election_department where department_id != 1;

select id, name FROM department WHERE department.hasmulticastserver = 1
    EXCEPT
SELECT id, name FROM department, election_department WHERE department.id = election_department.department_id AND election_department.election_id = 1;

SELECT id, name FROM department, election_department WHERE department.id = election_department.department_id AND election_department.election_id = 1;

SELECT id, name FROM department WHERE hasmulticastserver = 1;

SELECT * FROM candidacy;

INSERT INTO person(job,password,department_id,phone,address,cc_number,cc_validity) VALUES('Estudante','123',11,354,'ferw',35,'2000-15-31');

INSERT INTO election(title,type,description,begin_date,end_date) VALUES('dasdfdg','fsd','ty','2000-12-12 14:30:00','2000-13-13 14:30:00');

UPDATE department SET hasMulticastServer = null WHERE id = 2;

SELECT * FROM election WHERE end_date < date('now');

UPDATE election SET blank_votes = 8 WHERE id = 1;

UPDATE candidacy SET votes = 50 WHERE election_id = 1 and id = 2;
UPDATE candidacy SET votes = 10 WHERE election_id = 1 and id = 3;
UPDATE candidacy SET votes = 2 WHERE election_id = 1 and id = 5;
UPDATE candidacy SET votes = 22 WHERE election_id = 1 and id = 6;
UPDATE candidacy SET votes = 1 WHERE election_id = 2 and id = 4;
SELECT SUM(votes) FROM candidacy WHERE election_id = 1;

SELECT votes FROM candidacy WHERE election_id = 1 and id = 2;

SELECT DISTINCT * FROM person JOIN election_department ed on person.department_id = ed.department_id JOIN election e on ed.election_id = e.id AND ed.department_id = 8 AND ed.election_id = 9 AND name = 'Rita';
select * from election;
select * from person;
select * from person where cc_number = '11111113';

SELECT * FROM election, election_department WHERE begin_date <= date('now') AND end_date >= date('now') AND election.id = election_department.election_id AND department_id = 9;

SELECT * from candidacy, election where candidacy.election_id = election.id and election.id = 8;

select * from voting_record;
select * from person;

select * from election;

select * FROM voting_record
                  JOIN person p on voting_record.person_cc_number = p.cc_number
                  JOIN election e on voting_record.election_id = e.id
                  JOIN department d on d.id = voting_record.department
ORDER BY voting_record.election_id;

SELECT * FROM voting_record;
select * from department, voting_record where voting_record.department = department.id;

SELECT DISTINCT * FROM person JOIN election_department ed JOIN election e on ed.election_id = e.id AND e.type = person.job AND ed.election_id = 14 AND person.name like '%Rita%' AND ed.department_id = -1;

SELECT DISTINCT * FROM person JOIN election_department ed on person.department_id = ed.department_id JOIN election e on ed.election_id = e.id AND e.type = person.job AND ed.department_id = 5 AND ed.election_id = 14 AND person.name like '%rt%';

SELECT count(*), d.name as Name, e.title as Title
FROM voting_record
         JOIN department d on voting_record.department = d.id
         JOIN election e on e.id = voting_record.election_id
WHERE e.begin_date < date('now') AND e.end_date > date('now')
group by voting_record.department, voting_record.election_id;

select * from department;

select * from election;

SELECT DISTINCT * FROM election, election_department WHERE begin_date <= date('now') AND end_date >= date('now') AND election.id = election_department.election_id AND (department_id = 1 OR department_id = -1);

SELECT count(*), d.name as Name, e.title as Title
FROM voting_record
         JOIN department d on voting_record.department = d.id
         JOIN election e on e.id = voting_record.election_id
WHERE e.begin_date < date('now') AND e.end_date > date('now')
group by voting_record.department, voting_record.election_id;

SELECT department.name as depname, department.hasmulticastserver as statusPoll, vt.id as terminalId, status as statusTerminal FROM department
                                                                                                                                       LEFT JOIN voting_terminal vt on department.id = vt.department_id WHERE hasmulticastserver not null;

select * from voting_terminal;

select * from election_department;
select * from election WHERE begin_date <= date('now') AND end_date >= date('now');
SELECT * FROM election, election_department WHERE begin_date <= date('now') AND end_date >= date('now') AND election.id = election_department.election_id AND department_id = -1;


SELECT * FROM department WHERE (hasMulticastServer IS NULL OR hasMulticastServer = 0) AND id = 2;

SELECT * FROM election WHERE begin_date <= date('now') AND end_date >= date('now') AND election.type = 'Funcionário'
