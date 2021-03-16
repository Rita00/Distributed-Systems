CREATE TABLE election (
	id		 BIGINT,
	title	 VARCHAR(512) NOT NULL,
	type		 VARCHAR(512) NOT NULL,
	description	 VARCHAR(512),
	begin_date	 DATE NOT NULL,
	end_date	 DATE NOT NULL,
	blank_votes	 BIGINT NOT NULL DEFAULT 0,
	null_votes	 BIGINT NOT NULL DEFAULT 0,
	department_id INTEGER NOT NULL,
	PRIMARY KEY(id),
	FOREIGN KEY (department_id) REFERENCES department(id)
);

CREATE TABLE candidacy (
	id		 BIGINT,
	type	 VARCHAR(512) NOT NULL,
	votes	 BIGINT NOT NULL DEFAULT 0,
	election_id BIGINT NOT NULL,
	PRIMARY KEY(id),
	FOREIGN KEY (election_id) REFERENCES election(id)
);

CREATE TABLE person (
	name		 VARCHAR(32) NOT NULL,
	cc_number	 INTEGER,
	cc_validity	 DATE NOT NULL,
	address	 VARCHAR(512) NOT NULL,
	phone	 INTEGER NOT NULL,
	job		 VARCHAR(512) NOT NULL,
	password	 VARCHAR(512),
	department_id INTEGER NOT NULL,
	PRIMARY KEY(cc_number),
	FOREIGN KEY (department_id) REFERENCES department(id)
);

CREATE TABLE department (
	id	 INTEGER,
	name VARCHAR(512),
	PRIMARY KEY(id),
);

CREATE TABLE candidacy_person (
	candidacy_id	 BIGINT NOT NULL,
	person_cc_number INTEGER,
	PRIMARY KEY(person_cc_number),
	FOREIGN KEY (candidacy_id) REFERENCES candidacy(id),
	FOREIGN KEY (person_cc_number) REFERENCES person(cc_number)
);


