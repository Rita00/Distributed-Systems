
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
