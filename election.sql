CREATE TABLE person (
                        funcao	 VARCHAR(512) NOT NULL,
                        password	 VARCHAR(512) NOT NULL,
                        depart	 VARCHAR(512) NOT NULL,
                        phone	 BIGINT UNIQUE,
                        address	 CHAR(255) NOT NULL,
                        numcc	 BIGINT UNIQUE NOT NULL,
                        validadecc bigint not null,
                        PRIMARY KEY(numcc)
);

CREATE TABLE election (
                          inidate		 DATE NOT NULL,
                          fimdate		 DATE NOT NULL,
                          title		 VARCHAR(512) NOT NULL,
                          description	 VARCHAR(512) NOT NULL,
                          votingtable_depart VARCHAR(512) NOT NULL,
                          person_cc	 BIGINT NOT NULL,
                          FOREIGN KEY (person_cc) REFERENCES Person(numcc)
);