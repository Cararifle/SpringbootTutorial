INSERT INTO category(name) VALUES ('Eurogames');
INSERT INTO category(name) VALUES ('Ameritrash');
INSERT INTO category(name) VALUES ('Familiar');

INSERT INTO author(name, nationality) VALUES ('Alan R. Moon', 'US');
INSERT INTO author(name, nationality) VALUES ('Vital Lacerda', 'PT');
INSERT INTO author(name, nationality) VALUES ('Simone Luciani', 'IT');
INSERT INTO author(name, nationality) VALUES ('Perepau Llistosella', 'ES');
INSERT INTO author(name, nationality) VALUES ('Michael Kiesling', 'DE');
INSERT INTO author(name, nationality) VALUES ('Phil Walker-Harding', 'US');

INSERT INTO game(title, age, category_id, author_id) VALUES ('On Mars', '14', 1, 2);
INSERT INTO game(title, age, category_id, author_id) VALUES ('Aventureros al tren', '8', 3, 1);
INSERT INTO game(title, age, category_id, author_id) VALUES ('1920: Wall Street', '12', 1, 4);
INSERT INTO game(title, age, category_id, author_id) VALUES ('Barrage', '14', 1, 3);
INSERT INTO game(title, age, category_id, author_id) VALUES ('Los viajes de Marco Polo', '12', 1, 3);
INSERT INTO game(title, age, category_id, author_id) VALUES ('Azul', '8', 3, 5);

INSERT INTO client(name) VALUES ('Cliente 1');
INSERT INTO client(name) VALUES ('Cliente 2');
INSERT INTO client(name) VALUES ('Cliente 3');

INSERT INTO loan(game_id, client_id, loan_date, return_date) VALUES (1,1,'2026-04-24', '2026-04-25');
INSERT INTO loan(game_id, client_id, loan_date, return_date) VALUES (2,1,'2026-04-26', '2026-04-27');
INSERT INTO loan(game_id, client_id, loan_date, return_date) VALUES (3,2,'2026-04-28', '2026-04-29');
INSERT INTO loan(game_id, client_id, loan_date, return_date) VALUES (4,2,'2026-05-02', '2026-05-03');
INSERT INTO loan(game_id, client_id, loan_date, return_date) VALUES (5,3,'2026-05-04', '2026-05-05');
INSERT INTO loan(game_id, client_id, loan_date, return_date) VALUES (6,3,'2026-04-06', '2026-04-07');
