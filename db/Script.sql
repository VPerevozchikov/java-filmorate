SELECT * FROM FILMS AS f
WHERE ID IN (
	SELECT FILM_ID
	FROM LIST_OF_LIKES AS lol
	GROUP BY lol.FILM_ID 
	ORDER BY COUNT(lol.FILM_ID) 
	LIMIT 3
	)
ORDER BY 

	

SELECT FILM_ID,
	COUNT(FILM_ID)
	FROM LIST_OF_LIKES AS lol
	JOIN FILMS f ON lol.FILM_ID =f.ID
	GROUP BY FILM_ID
	ORDER BY COUNT(FILM_ID) DESC;

SELECT f.id,
       f.NAME,
       f.DESCRIPTION,
       f.DURATION,
       f.RELEASE_DATE,
       f.RATING_MPA_ID,
       COUNT(lol.film_id)
FROM FILMS f
LEFT JOIN LIST_OF_LIKES lol ON f.id=lol.film_id
GROUP BY f.id
ORDER BY COUNT(lol.film_id) DESC
LIMIT 3;

SELECT * 
FROM USERS
JOIN (
	SELECT USER_2_ID, 
	COUNT(USER_2_ID)
	FROM LIST_OF_FRIENDS
	WHERE USER_1_ID = 3
	OR USER_1_ID = 1
	GROUP BY USER_2_ID 
	HAVING COUNT(USER_2_ID) =2
	) m ON id = m.user_2_ID;

select * from films join mpa on films.MPA_ID = mpa.ID where films.id = 1;


SELECT *,
       COUNT(lol.film_id),
       mpa.MPA_NAME 
FROM FILMS f
LEFT JOIN LIST_OF_LIKES lol ON f.id=lol.film_id
join mpa on f.MPA_ID = mpa.MPA_ID
GROUP BY f.id
ORDER BY COUNT(lol.film_id) DESC
LIMIT 3;

SELECT *,
       COUNT(lol.film_id)
FROM FILMS f
LEFT JOIN LIST_OF_LIKES lol ON f.id=lol.film_id
GROUP BY f.id
ORDER BY COUNT(lol.film_id) DESC
LIMIT 3;

SELECT *, mpa.mpa_name, COUNT(LIST_OF_LIKES.film_id)    FROM FILMS     LEFT JOIN LIST_OF_LIKES ON FILMS.id=LIST_OF_LIKES.film_id    join mpa on FILMS.MPA_ID = mpa.MPA_ID    GROUP BY films.id    ORDER BY COUNT(LIST_OF_LIKES.film_id) DESC    LIMIT 3;
   
   INSERT INTO LIST_OF_LIKES (FILM_ID, USER_ID)
   VALUES(1,2)
   
   SELECT FILMS.ID, FILMS.NAME, FILMS.DESCRIPTION, FILMS.DURATION, FILMS.RELEASE_DATE, FILMS.MPA_ID , mpa.mpa_name, COUNT(LIST_OF_LIKES.film_id)
    FROM FILMS 
    LEFT JOIN LIST_OF_LIKES ON FILMS.id=LIST_OF_LIKES.film_id
    join mpa on FILMS.MPA_ID = mpa.MPA_ID
    GROUP BY films.id
    ORDER BY COUNT(LIST_OF_LIKES.film_id) DESC
    LIMIT 3;

   
   SELECT FILMS.ID, NAME, DESCRIPTION, DURATION, RELEASE_DATE, MPA_ID, MPA_NAME, COUNT(LIST_OF_LIKES.film_id)
               FROM FILMS
               LEFT JOIN LIST_OF_LIKES ON FILMS.id=LIST_OF_LIKES.film_id
               join mpa on FILMS.MPA_ID = mpa.MPA_ORIG_ID
               GROUP BY FILMS.ID
               ORDER BY COUNT(LIST_OF_LIKES.film_id) DESC
               LIMIT 3;

















