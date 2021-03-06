drop table if exists election;
drop table if exists candidacy;
drop table if exists person;
drop table if exists department;
drop table if exists voting_record;
drop table if exists election_department;
drop table if exists candidacy_person;
drop table if exists voting_terminal;

CREATE TABLE voting_terminal (
    id		        INTEGER,
    department_id   INTEGER,
    status          INTEGER,
    infoPerson      INTEGER,
    infoElection    INTEGER,
    FOREIGN KEY (department_id) REFERENCES department(id),
    PRIMARY KEY(id)
);


CREATE TABLE election (
id		 INTEGER,
title	 VARCHAR(512) NOT NULL,
type	 VARCHAR(512) NOT NULL,
description VARCHAR(512),
begin_date	 VARCHAR(32) NOT NULL CONSTRAINT begin_date CHECK (begin_date is datetime(begin_date)),
end_date	 VARCHAR(32) NOT NULL CONSTRAINT end_date CHECK (end_date is datetime(end_date) and end_date > election.begin_date),
blank_votes BIGINT NOT NULL DEFAULT 0,
null_votes	 BIGINT NOT NULL DEFAULT 0,
null_percent float,
blank_percent float,
PRIMARY KEY(id)
);

CREATE TABLE candidacy (
id		 INTEGER,
name	 VARCHAR(512) NOT NULL,
type	 VARCHAR(512) NOT NULL,
votes	 BIGINT NOT NULL DEFAULT 0,
election_id INTEGER NOT NULL,
votes_percent float,
PRIMARY KEY(id),
FOREIGN KEY (election_id) REFERENCES election(id)
);

CREATE TABLE person (
name    VARCHAR(512) NOT NULL,
job		 VARCHAR(512) NOT NULL,
password	 VARCHAR(512),
department_id INTEGER NOT NULL,
phone	 INTEGER NOT NULL,
address	 VARCHAR(512) NOT NULL,
cc_number	 INTEGER,
cc_validity	 DATE NOT NULL CONSTRAINT cc_validity CHECK (cc_validity is date(cc_validity) AND cc_validity > date('now)')),
fbID    VARCHAR(512) UNIQUE,
PRIMARY KEY(cc_number),
FOREIGN KEY (department_id) REFERENCES department(id)
);

CREATE TABLE department (
id	 INTEGER,
name VARCHAR(512),
hasmulticastserver SMALLINT,
PRIMARY KEY(id)
);

CREATE TABLE voting_record (
   vote_date	    DATE,
   department  INTEGER,
   person_cc_number INTEGER,
   election_id	    INTEGER,
   PRIMARY KEY(person_cc_number,election_id),
   FOREIGN KEY (person_cc_number) REFERENCES person(cc_number),
   FOREIGN KEY (election_id) REFERENCES election(id)
);

CREATE TABLE election_department (
         election_id	 INTEGER,
         department_id INTEGER,
         FOREIGN KEY (election_id) REFERENCES election(id),
         FOREIGN KEY (department_id) REFERENCES department(id)
);

CREATE TABLE candidacy_person (
      candidacy_id	 INTEGER NOT NULL,
      person_cc_number INTEGER,
      PRIMARY KEY(person_cc_number),
      FOREIGN KEY (candidacy_id) REFERENCES candidacy(id),
      FOREIGN KEY (person_cc_number) REFERENCES person(cc_number)
);

ALTER TABLE person ADD isAdmin INTEGER;