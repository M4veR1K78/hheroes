CREATE SCHEMA hheroes;

CREATE TABLE hheroes.boss (
 id integer PRIMARY KEY,
 orgasm integer,
 ego float,
 x float,
 d float,
 nb_org integer,
 figure integer,
 world text,
 libelle text
);

CREATE TABLE hheroes.user (
 id serial PRIMARY KEY,
 email text NOT NULL UNIQUE,
 password text,
 boss_id INTEGER,
 admin boolean NOT NULL,
 auto_salary boolean NOT NULL DEFAULT false,
 FOREIGN KEY (boss_id) REFERENCES hheroes.boss (id) 
);