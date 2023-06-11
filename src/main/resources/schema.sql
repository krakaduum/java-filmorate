-- PUBLIC."USER" definition

CREATE TABLE USERS (
	ID BIGINT auto_increment,
	EMAIL CHARACTER VARYING,
	LOGIN CHARACTER VARYING,
	NAME CHARACTER VARYING,
	BIRTHDAY DATE,
	CONSTRAINT USER_PK PRIMARY KEY (ID)
);


-- PUBLIC.GENRE definition

CREATE TABLE GENRE (
	ID INTEGER auto_increment,
	NAME CHARACTER VARYING,
	CONSTRAINT GENRE_PK PRIMARY KEY (ID)
);


-- PUBLIC.MPA definition

CREATE TABLE MPA (
	ID INTEGER auto_increment,
	NAME CHARACTER VARYING,
	CONSTRAINT MPA_PK PRIMARY KEY (ID)
);


-- PUBLIC.FILM definition

CREATE TABLE FILMS (
	ID BIGINT auto_increment,
	NAME CHARACTER VARYING,
	DESCRIPTION CHARACTER VARYING(200),
	RELEASE_DATE DATE,
	DURATION INTEGER,
	MPA INTEGER,
	CONSTRAINT FILM_PK PRIMARY KEY (ID),
	CONSTRAINT FILM_MPA_FK FOREIGN KEY (MPA) REFERENCES PUBLIC.MPA(ID)
);


-- PUBLIC.LIKES definition

CREATE TABLE LIKES (
	FILM_ID BIGINT,
	USER_ID BIGINT,
	CONSTRAINT LIKES_FILM_FK FOREIGN KEY (FILM_ID) REFERENCES PUBLIC.FILMS(ID),
	CONSTRAINT LIKES_USER_FK FOREIGN KEY (USER_ID) REFERENCES PUBLIC.USERS(ID)
);


-- PUBLIC.FILM_GENRES definition

CREATE TABLE FILM_GENRES (
	FILM_ID BIGINT,
	GENRE_ID INTEGER,
	CONSTRAINT FILM_FK FOREIGN KEY (FILM_ID) REFERENCES PUBLIC.FILMS(ID),
	CONSTRAINT GENRES_FK FOREIGN KEY (GENRE_ID) REFERENCES PUBLIC.GENRE(ID)
);


-- PUBLIC.FRIENDS definition

CREATE TABLE FRIENDS (
	FRIEND1_ID BIGINT,
	FRIEND2_ID BIGINT,
	CONSTRAINT FRIEND1_FK FOREIGN KEY (FRIEND1_ID) REFERENCES PUBLIC.USERS(ID),
	CONSTRAINT FRIENDS2_FK FOREIGN KEY (FRIEND2_ID) REFERENCES PUBLIC.USERS(ID)
);