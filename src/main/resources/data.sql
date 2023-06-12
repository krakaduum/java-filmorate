INSERT INTO MPA (ID,NAME)
	select 1, 'G' from dual
	where not exists(select * from MPA where id = 1);
INSERT INTO MPA (ID,NAME)
	select 2,'PG' from dual
	where not exists(select * from MPA where id = 2);
INSERT INTO MPA (ID,NAME)
	select 3,'PG-13' from dual
	where not exists(select * from MPA where id = 3);
INSERT INTO MPA (ID,NAME)
	select 4,'R' from dual
	where not exists(select * from MPA where id = 4);
INSERT INTO MPA (ID,NAME)
	select 5,'NC-17' from dual
	where not exists(select * from MPA where id = 5);

INSERT INTO GENRE (ID,NAME)
    select 1,'Комедия' from dual
	where not exists(select * from GENRE where id = 1);
INSERT INTO GENRE (ID,NAME)
    select 2,'Драма' from dual
	where not exists(select * from GENRE where id = 2);
INSERT INTO GENRE (ID,NAME)
    select 3,'Мультфильм' from dual
	where not exists(select * from GENRE where id = 3);
INSERT INTO GENRE (ID,NAME)
    select 4,'Триллер' from dual
	where not exists(select * from GENRE where id = 4);
INSERT INTO GENRE (ID,NAME)
    select 5,'Документальный' from dual
	where not exists(select * from GENRE where id = 5);
INSERT INTO GENRE (ID,NAME)
    select 6,'Боевик' from dual
	where not exists(select * from GENRE where id = 6);