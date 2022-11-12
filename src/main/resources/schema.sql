CREATE TABLE IF NOT EXISTS FILMS
(
    ID       UUID PRIMARY KEY,
    TITLE    VARCHAR,
    CATEGORY VARCHAR,
    YEAR     int
);

CREATE TABLE IF NOT EXISTS SCREENING_ROOMS
(

    ID         UUID PRIMARY KEY,
    NUMBER     INT,
    FREE_SEATS INT
);

CREATE TABLE IF NOT EXISTS SCREENINGS
(
    ID                  UUID PRIMARY KEY,
    MIN_AGE             INT,
    DATE                DATETIME,
    FREE_SEATS_QUANTITY INT,
    FILM_ID             UUID,
    ROOM_ID             UUID,
    FOREIGN KEY (FILM_ID) REFERENCES FILMS (ID),
    FOREIGN KEY (ROOM_ID) REFERENCES SCREENING_ROOMS (ID)
);

CREATE TABLE IF NOT EXISTS SCREENINGS_TICKETS
(
    ID           UUID PRIMARY KEY,
    FIRST_NAME   VARCHAR,
    LAST_NAME    VARCHAR,
    STATUS       ENUM ('OPEN', 'CANCELLED'),
    SCREENING_ID UUID,
    FOREIGN KEY (SCREENING_ID) REFERENCES SCREENINGS (ID)
);
CREATE TABLE IF NOT EXISTS USERS
(
    USERNAME VARCHAR PRIMARY KEY,
    PASSWORD VARCHAR,
    ENABLED  BOOLEAN
);
CREATE TABLE IF NOT EXISTS AUTHORITIES
(
    USERNAME  VARCHAR,
    AUTHORITY VARCHAR,
    FOREIGN KEY (USERNAME) REFERENCES USERS (USERNAME)
)