This is web app to manage a cinema.

Features:

1.Storing and searching films and screenings:

Endpoints:

/films (POST) - add a new film (admin only)

/films (GET) - search all films

/films?category={category} (GET) - search films by category

/films/screenings (POST) - add a new screening (admin only)

/films/screenings (GET) - search all films screenings

/films/screenings?{paramName}=paramValue - search screenings by params

/films/screenings/rooms (POST) - add a new screening room (admin only)

/films/screenings/rooms (GET) - search all screenings room (admin only)

/////////////////////

2.Booking seats

Endpoints:

/bookings (POST) - book a seat for screening

/bookings/{bookingId}/cancelled (PATCH) - cancel a seat booking

/bookings/bookingId (GET) - search a seat booking by id

/////////////////////

3.User account:

Endpoints:

/signup (POST) - create a new user account

/signin (POST) - logg into a user account

/signout (POST) - logg out a user account

Tech stack:Java 17, Spring Boot, Lombok, H2 DB, Open API

API available on localhost:8080/swagger-ui:






