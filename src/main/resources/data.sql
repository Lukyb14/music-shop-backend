/*
SELECT * FROM album;
SELECT * FROM song;
SELECT * FROM soundcarrier;
 */
DELETE FROM soundcarrier;
DELETE FROM song;
DELETE FROM album;

INSERT INTO album (id, genre, label, name, release, artist) VALUES (1, 'Rock', 'Leidseplein Presse B.V.', 'The Razors Edge', '1990-01-01', 'AC/DC');
INSERT INTO album (id, genre, label, name, release, artist) VALUES (2, 'Rock', 'Leidseplein Presse B.V.', 'Back In Black', '1980-01-01', 'AC/DC');
INSERT INTO album (id, genre, label, name, release, artist) VALUES (3, 'Rock', 'Mercury Records Limited', 'Brothers In Arms', '1985-01-01', 'Dire Straits');

INSERT INTO song (id, title, release, album_id) VALUES (1, 'Thunderstruck', '1990-01-01', 1);
INSERT INTO song (id, title, release, album_id) VALUES (2, 'Fire Your Guns', '1990-01-01', 1);
INSERT INTO song (id, title, release, album_id) VALUES (3, 'Moneytalks', '1990-01-01', 1);
INSERT INTO song (id, title, release, album_id) VALUES (4, 'Hells Bells', '1980-01-01', 2);
INSERT INTO song (id, title, release, album_id) VALUES (5, 'Shoot to Thrill', '1980-01-01', 2);
INSERT INTO song (id, title, release, album_id) VALUES (6, 'Back In Black', '1980-01-01', 2);
INSERT INTO song (id, title, release, album_id) VALUES (7, 'Money For Nothing', '1985-01-01', 3);
INSERT INTO song (id, title, release, album_id) VALUES (8, 'Walk Of Life', '1985-01-01', 3);
INSERT INTO song (id, title, release, album_id) VALUES (9, 'Brothers In Arms', '1985-01-01', 3);

INSERT INTO soundcarrier (id, articleid, medium, price, stock, album_id) VALUES (1, '1000', 'CD', 15.99, 3, 1);
INSERT INTO soundcarrier (id, articleid, medium, price, stock, album_id) VALUES (2, '1001', 'CD', 13.59, 4, 2);
INSERT INTO soundcarrier (id, articleid, medium, price, stock, album_id) VALUES (3, '1002', 'VINYL', 5.99, 2, 3);