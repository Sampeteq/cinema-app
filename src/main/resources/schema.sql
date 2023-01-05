CREATE TABLE IF NOT EXISTS USERS
(
    USERNAME VARCHAR PRIMARY KEY,
    PASSWORD VARCHAR,
    ROLE ENUM('COMMON', 'ADMIN')
);

CREATE TABLE IF NOT EXISTS FILMS
(
    ID       UUID PRIMARY KEY,
    TITLE    VARCHAR,
    CATEGORY VARCHAR,
    YEAR     INT,
    DURATION_IN_MINUTES INT
);

CREATE TABLE IF NOT EXISTS SCREENINGS_ROOMS
(
    ID                        UUID PRIMARY KEY,
    NUMBER                    INT,
    ROWS_QUANTITY             INT,
    SEATS_IN_ONE_ROW_QUANTITY INT
);

CREATE TABLE IF NOT EXISTS SCREENINGS
(
    ID      UUID PRIMARY KEY,
    MIN_AGE INT,
    DATE    DATETIME,
    FILM_ID UUID,
    ROOM_ID UUID,
    FOREIGN KEY (FILM_ID) REFERENCES FILMS (ID),
    FOREIGN KEY (ROOM_ID) REFERENCES SCREENINGS_ROOMS (ID)
);

CREATE TABLE IF NOT EXISTS SEATS
(
    ID           UUID PRIMARY KEY,
    ROW_NUMBER   INT,
    NUMBER       INT,
    STATUS       ENUM ('FREE', 'BUSY'),
    ROOM_ID      UUID,
    SCREENING_ID UUID,
    FOREIGN KEY (ROOM_ID) REFERENCES SCREENINGS_ROOMS (ID),
    FOREIGN KEY (SCREENING_ID) REFERENCES SCREENINGS (ID)
);

CREATE TABLE IF NOT EXISTS SEATS_BOOKINGS
(
    ID         UUID PRIMARY KEY,
    FIRST_NAME VARCHAR,
    LAST_NAME  VARCHAR,
    SEAT_ID    UUID,
    USERNAME   VARCHAR,
    FOREIGN KEY (SEAT_ID) REFERENCES SEATS (ID),
    FOREIGN KEY (USERNAME) REFERENCES USERS(USERNAME)
);