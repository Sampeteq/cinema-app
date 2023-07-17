This is web app to manage a cinema.

Features:

1.Storing and searching films, screenings and seats:

Endpoints:

Catalog module:

/films (POST) - add a new film (admin only)

/films (GET) - search all films

/screenings (POST) - add a new screening (admin only)

/screenings (GET) - search all screenings

/screenings/title?title= (GET) - search screenings by film title

/screenings/category?category= (GET) - search screenings by film category

/screenings/date?date= (GET) - search screenings by date

/screenings/{id}/seats (GET) - search screening seats

/////////////////////

2.Booking seats

Endpoints:

/bookings (POST) - book a seat

/bookings/{bookingId}/cancelled (POST) - cancel a seat booking

/bookings/my (GET) - search a user seat bookings

/////////////////////

3.User account:

Endpoints:

/signup (POST) - create a new user account

/signin (POST) - logg into a user account

/signout (POST) - logg out a user account

Tech stack:Java 17, Spring Boot, Lombok, H2 DB, Open API

API available on localhost:8080/swagger-ui: