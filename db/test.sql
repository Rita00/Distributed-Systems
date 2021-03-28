
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


