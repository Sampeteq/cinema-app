CREATE TABLE IF NOT EXISTS USERS
(
    USERNAME VARCHAR PRIMARY KEY,
    PASSWORD VARCHAR,
    ROLE     VARCHAR
);

CREATE TABLE IF NOT EXISTS FILMS
(
    ID                  UUID PRIMARY KEY,
    TITLE               VARCHAR,
    CATEGORY            VARCHAR,
    YEAR                INT,
    DURATION_IN_MINUTES INT
);

CREATE TABLE IF NOT EXISTS ROOMS
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
    DATE    TIMESTAMP WITH TIME ZONE,
    FILM_ID UUID,
    ROOM_ID UUID,
    FOREIGN KEY (FILM_ID) REFERENCES FILMS (ID),
    FOREIGN KEY (ROOM_ID) REFERENCES ROOMS (ID)
    );

CREATE TABLE IF NOT EXISTS SEATS
(
    ID           UUID PRIMARY KEY,
    ROW_NUMBER   INT,
    NUMBER       INT,
    STATUS       VARCHAR,
    SCREENING_ID UUID,
    FOREIGN KEY (SCREENING_ID) REFERENCES SCREENINGS (ID)
    );

CREATE TABLE IF NOT EXISTS BOOKINGS
(
    ID           UUID PRIMARY KEY,
    STATUS       VARCHAR,
    SEAT_ID      UUID,
    USERNAME     VARCHAR,
    FOREIGN KEY (SEAT_ID) REFERENCES SEATS (ID),
    FOREIGN KEY (USERNAME) REFERENCES USERS (USERNAME)
    );