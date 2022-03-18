/*
SELECT * FROM album;
SELECT * FROM song;
SELECT * FROM soundcarrier;

DELETE FROM album;
DELETE FROM song;
DELETE FROM soundcarrier;

 */

INSERT INTO album (id, artist, genre, label, name, release) VALUES (1, 'Bill Gates', 'Rock', 'Microsoft', 'Windows 11', '2019-08-06');
INSERT INTO song (id, artist, genre, publishdate, recordlabel, title, album_id) VALUES (1, 'Bill Gates', 'Rock', '2019-08-06', 'Microsoft', 'Startup Sound', 1);
INSERT INTO soundcarrier (id, medium, price, stock, album_id) VALUES (1, 'CD', 35.99, 3, 1);