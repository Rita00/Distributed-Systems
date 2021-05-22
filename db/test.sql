
SELECT *
FROM person
WHERE cc_number IN (
    SELECT person_cc_number
    FROM candidacy_person
    WHERE candidacy_id = 1
);

UPDATE election
SET title='y',
    type= 'y',
    description='y',
    begin_date='1000-10-10 11:11:11',
    end_date='1000-10-10 11:11:11'
WHERE id=1;

INSERT INTO candidacy(id,name,type,election_id) VALUES (NULL,'aaaa','student',1);

SELECT * FROM person WHERE cc_number='55555557' AND password='estacerta';

DELETE FROM voting_terminal WHERE department_id=1;
INSERT INTO voting_terminal(id,department_id) VALUES (65667544567,1);
INSERT INTO voting_terminal(id,department_id) VALUES (35463456354,1);


SELECT COUNT(job) as Total,
       SUM(job='Estudante') as Estudante,
       SUM(job='Docente') as Docente,
       SUM(job='Funcionário') as Funcionario,
       d.name as Name, e.title as Title
FROM voting_record v
JOIN department d on v.department = d.id
JOIN election e on e.id = v.election_id
JOIN person p on p.cc_number = v.person_cc_number
WHERE e.begin_date < date('now') AND e.end_date > date('now') group by v.department, v.election_id,p.job;

SELECT count(*),d.name as Name, e.title as Title
FROM voting_record
JOIN department d on voting_record.department = d.id
JOIN election e on e.id = voting_record.election_id
WHERE e.begin_date < date('now') AND e.end_date > date('now') group by voting_record.department, voting_record.election_id;


INSERT INTO voting_terminal VALUES (1, 1, 1, 77777777,2);
INSERT INTO voting_terminal VALUES (2, 1, 1, 11111111,2);
INSERT INTO voting_terminal VALUES (4, 12, 1, 77777777,2);
INSERT INTO voting_terminal VALUES (5, 12, 1, null,2);
SELECT p.cc_number as id, p.name as name, d.name as department
FROM voting_terminal v,person p, department d
WHERE v.infoPerson=p.cc_number and v.department_id=d.id
ORDER BY d.name