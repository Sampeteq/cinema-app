This is web app to manage a cinema.

Features:

1.Storing and searching films and screenings (catalog)

Endpoints:

Common user:

/films (GET) - search all films

/screenings (GET) - search all screenings

/screenings/title?title= (GET) - search screenings by film title

/screenings/category?category= (GET) - search screenings by film category

/screenings/date?date= (GET) - search screenings by date

Admin:

/films (POST) - add a new film

/screenings (POST) - add a new screening

/////////////////////

2.Booking making (bookings)

Endpoints:

/seats?screeningId= (GET) - search screening seats

/bookings (POST) - make a booking for seat

/bookings/{bookingId}/cancelled (POST) - cancel booking

/bookings/my (GET) - search user bookings

/////////////////////

3.User account (user):

Endpoints:

/sign-up (POST) - create a new user account

/sign-in (POST) - logg into a user account

/sign-out (POST) - logg out a user account

/password/reset (POST) - reset password (a mail with token will be sent)

/password/new (POST) - set new password after reset

Tech stack:Java 17, Maven, Spring Boot, Lombok, Postgres, Open API

API available on localhost:8080/swagger-ui

How to run it:

1.Download docker-compose.yaml file

2.Run cmd in folder where you placed downloaded file and use this command: docker-compose up -d