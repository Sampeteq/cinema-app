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

3.Booking tickets

Endpoints:

/screening-tickets (POST) - book a new ticket for screening

/screenings-tickets/{ticketId}/cancelled - cancel a ticket's booking

/screenings-tickets (GET) - search all tickets (admin only)

/////////////////////

Tech stack:Java 17, Spring Boot 2.6.7, Lombok, H2 DB, Open API

API available on localhost:8080/swagger-ui:






