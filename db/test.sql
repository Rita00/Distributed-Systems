
SELECT *
FROM person
WHERE cc_number IN (
    SELECT person_cc_number
    FROM candidacy_person
    WHERE candidacy_id = 1
);

