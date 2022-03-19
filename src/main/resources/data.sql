/*
SELECT * FROM album;
SELECT * FROM song;
SELECT * FROM soundcarrier;

DELETE FROM soundcarrier;
DELETE FROM song;
DELETE FROM album;

 */

INSERT INTO album (id, genre, label, name, release) VALUES (1, 'Rock', 'Parlophone Records Ltd', 'Fear of the Dark', '1992-01-01');
INSERT INTO song (id, title, artist, release, album_id) VALUES (1, 'Fear Is the Key', 'Iron Maiden', '1992-01-01', 1);
INSERT INTO soundcarrier (id, medium, price, stock, album_id) VALUES (1, 'CD', 35.99, 3, 1);