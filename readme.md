This is web app to manage a cinema.

Features:

1.Storing and searching films:

Endpoints:

/films (POST) - add a new film (admin only)

/films (GET) - search all films

/films?category={category} (GET) - search films by category

/////////////////////

2.Storing and searching screenings:

Endpoints:

/screenings (POST) - add a new screening (admin only)

/screenings (GET) - search all films screenings

/screenings?{paramName}=paramValue - search screenings by params

/screenings-rooms (POST) - add a new screening room (admin only)

/screenings-rooms (GET) - search all screenings room (admin only)

/////////////////////

3.Booking seats

Endpoints:

/seats-bookings (POST) - book a seat for screening

/seats-bookings/{bookingId}/cancelled (PATCH) - cancel a seat booking

/seats-bookings/bookingId (GET) - search a seat booking by id

/////////////////////

4.User account:

Endpoints:

/signup (POST) - create a new user account

/signin (POST) - logg into a user account

/signout (POST) - logg out a user account

Tech stack:Java 17, Spring Boot, Lombok, H2 DB, Open API

API available on localhost:8080/swagger-ui:






