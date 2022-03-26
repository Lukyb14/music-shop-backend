/*
SELECT * FROM album;
SELECT * FROM song;
SELECT * FROM soundcarrier;
 */
DELETE FROM soundcarrier;
DELETE FROM song;
DELETE FROM album;

INSERT INTO album (id, genre, label, name, release) VALUES (1, 'Rock', 'Leidseplein Presse B.V.', 'The Razors Edge', '1990-01-01');
INSERT INTO album (id, genre, label, name, release) VALUES (2, 'Rock', 'Leidseplein Presse B.V.', 'Back In Black', '1980-01-01');
INSERT INTO album (id, genre, label, name, release) VALUES (3, 'Rock', 'Mercury Records Limited', 'Brothers In Arms', '1985-01-01');

INSERT INTO song (id, title, artist, release, album_id) VALUES (1, 'Thunderstruck', 'AC/DC', '1990-01-01', 1);
INSERT INTO song (id, title, artist, release, album_id) VALUES (2, 'Fire Your Guns', 'AC/DC', '1990-01-01', 1);
INSERT INTO song (id, title, artist, release, album_id) VALUES (3, 'Moneytalks', 'AC/DC', '1990-01-01', 1);
INSERT INTO song (id, title, artist, release, album_id) VALUES (4, 'Hells Bells', 'AC/DC', '1980-01-01', 2);
INSERT INTO song (id, title, artist, release, album_id) VALUES (5, 'Shoot to Thrill', 'AC/DC', '1980-01-01', 2);
INSERT INTO song (id, title, artist, release, album_id) VALUES (6, 'Back In Black', 'AC/DC', '1980-01-01', 2);
INSERT INTO song (id, title, artist, release, album_id) VALUES (7, 'Money For Nothing', 'Dire Straits', '1985-01-01', 3);
INSERT INTO song (id, title, artist, release, album_id) VALUES (8, 'Walk Of Life', 'Dire Straits', '1985-01-01', 3);
INSERT INTO song (id, title, artist, release, album_id) VALUES (9, 'Brothers In Arms', 'Dire Straits', '1985-01-01', 3);

INSERT INTO soundcarrier (id, medium, price, stock, album_id) VALUES (1, 'CD', 15.99, 3, 1);
INSERT INTO soundcarrier (id, medium, price, stock, album_id) VALUES (2, 'CD', 13.59, 4, 2);
INSERT INTO soundcarrier (id, medium, price, stock, album_id) VALUES (3, 'VINYL', 5.99, 2, 3);