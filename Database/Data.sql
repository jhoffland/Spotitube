DELETE FROM PlaylistTrack;
DELETE FROM Playlists;
DELETE FROM Tracks;
DELETE FROM Users;

INSERT INTO Users (id, username, password)
VALUES
  (1, "met", "1234"),
  (2, "zonder", "1234");

INSERT INTO Playlists (id, name, owner)
VALUES
  (1, "Favorieten", 1),
  (2, "Random", 1);

INSERT INTO `Tracks` (`id`, `performer`, `title`, `url`, `duration`, `playcount`, `album`, `publicationDate`, `description`)
VALUES
	(1, 'Mabel', 'Don\'t Call Me Up', 'https://open.spotify.com/track/5WHTFyqSii0lmT9R21abT8', 178, 0, 'Ivy To Roses (Mixtape)', NULL, NULL),
	(2, 'Priceless, Frenna, Murda', 'Rompe', 'https://open.spotify.com/track/4YaKdKVcrM6Gf5ZALtp0gI', 184, 0, 'Rompe', NULL, NULL),
	(3, 'Kris Kross Amsterdam, Maan, Tabitha, Bizzey', 'Hij Is Van Mij (feat. Bizzey)', 'https://open.spotify.com/track/6716bTJI7qiHJFFSR0Ethe', 214, 0, 'Hij Is Van Mij (feat. Bizzy)', NULL, NULL),
	(4, 'Daddy Yankee, Snow', 'Con Calma', 'https://open.spotify.com/track/5w9c2J52mkdntKOmRLeM2m', 193, 0, 'Con Calma', NULL, NULL),
	(5, 'Suzan & Freek', 'Als Het Avond Is', 'https://open.spotify.com/track/3ciQQMSy1lKDqJbwU9l1LT', 205, 0, 'Als Het Avond Is', NULL, NULL),
	(6, 'Lady Gaga, Bradley Cooper', 'Shallow', 'https://open.spotify.com/track/2VxeLyX666F8uXCJ0dZF8B', 215, 0, 'A Star Is Born Soundtrack', NULL, NULL),
	(7, 'Yung Felix, Chivv, Bizzey, Kraantje Pappie', 'Last Man Standing', 'https://open.spotify.com/track/7JqF4AHCyokdICrFYF91zE', 190, 0, 'Last Man Standing', NULL, NULL),
	(8, 'Yung Felix, Poke, Bizzey', 'Baby Momma', 'https://open.spotify.com/track/1JPqCol2aFreho0bfkhiqc', 196, 0, 'Baby Momma', NULL, NULL),
	(9, 'Calvin Harris, Rag\'n\'Bone Man', 'Giant (with Rag\'n\'Bone Man)', 'https://open.spotify.com/track/5itOtNx0WxtJmi1TQ3RuRd', 229, 0, 'Giant (with Rag\'n\'Bone Man)', NULL, NULL),
	(10, 'Nielson', 'IJskoud', 'https://open.spotify.com/track/2zVNIG9cfMyFxt8XqJ64s4', 183, 0, 'Diamant', NULL, NULL),
	(11, 'Gio', 'MIJN VADER WORDT GEOPEREERD!', 'https://www.youtube.com/watch?v=LP-JA7rB5SQ', 1290, 0, NULL, '2019-03-27', 'MIJN VADER WORDT GEOPEREERD!'),
	(12, 'Jeremy', 'BESTE VLOGGER EN COMEDY WINNEN OP VEED!', 'https://www.youtube.com/watch?v=XkhVGlAldS8', 606, 0, NULL, '2019-03-25', 'BESTE VLOGGER EN COMEDY WINNEN OP VEED!'),
	(13, 'Gierige Gasten', 'Toeristen oplichten voor €30! | Gierige Gasten', 'https://www.youtube.com/watch?v=Mgg7MxsNIPQ', 589, 0, NULL, '2019-03-26', 'Toeristen oplichten voor €30!'),
	(14, 'Dude Perfect', 'Real Life Trick Shots Bloopers | Overtime 8 | Dude Perfect', 'https://www.youtube.com/watch?v=uUio9YoGhxo', 1211, 0, NULL, '2019-03-26', 'Real Life Trick Shots Bloopers | Overtime 8'),
	(15, 'TOP LIJSTJES', 'TOP 10 MISLUKTE PLASTISCHE CHIRURGIE BIJ BEROEMDHEDEN!', 'https://www.youtube.com/watch?v=AWYmeMjDFm8', 683, 0, NULL, '2019-03-23', 'TOP 10 MISLUKTE PLASTISCHE CHIRURGIE BIJ BEROEMDHEDEN!'),
	(16, '538 Muziek', 'Josylvio en Bokoesam strijden tegen Kaj en Vinchenzo! | PLATENBAZEN #3', 'https://www.youtube.com/watch?v=iR7qLhqVsHA&t=14s', 837, 0, NULL, '2019-03-26', 'Josylvio en Bokoesam strijden tegen Kaj en Vinchenzo! | PLATENBAZEN #3'),
	(17, 'De Telegraaf', 'Kalvijn boos over ophef: ‘Bullsh*t!’', 'https://www.youtube.com/watch?v=ET6dIyaxP_o&t=288s', 377, 0, NULL, '2019-03-24', 'Kalvijn boos over ophef: ‘Bullsh*t!’'),
	(18, 'Anna Nooshin', 'JayJay werd keihard geslagen tijdens de seks - Anna Nooshin', 'https://www.youtube.com/watch?v=FFyzU4g4_ws', 1237, 0, NULL, '2019-03-27', 'JayJay werd keihard geslagen tijdens de seks - Anna Nooshin'),
	(19, 'WatchDutch MD', 'AK-47 GEVONDEN IN AMSTERDAM ! - WATCHDUTCH MD - MAGNEETVISSEN - MET IRONMEN MD - POLITIE', 'https://www.youtube.com/watch?v=T8-eIicRvvc', 994, 0, NULL, '2019-03-23', 'AK-47 GEVONDEN IN AMSTERDAM ! - WATCHDUTCH MD - MAGNEETVISSEN - MET IRONMEN MD - POLITIE'),
	(20, 'MeisjeDjamila', '24 UUR LANG ALLEEN MAAR ORANJE VOEDSEL ETEN! - Challenge', 'https://www.youtube.com/watch?v=077bMAd-iMU', 805, 0, NULL, '2019-03-25', '24 UUR LANG ALLEEN MAAR ORANJE VOEDSEL ETEN! - Challenge');

INSERT INTO PlaylistTrack (playlist, track, offlineAvailable)
VALUES
	(1, 1, TRUE),
	(1, 3, TRUE),
	(1, 6, FALSE),
	(1, 9, TRUE),
	(1, 17, FALSE),
	(1, 14, TRUE),
	(2, 13, FALSE),
	(2, 2, FALSE),
	(2, 5, TRUE),
	(2, 19, TRUE),
	(2, 16, FALSE);

