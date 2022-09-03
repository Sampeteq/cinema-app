This is a cinema-app to browse films, making reservations for their screenings and management for admins.


Tech stack: Java 17, Spring Boot 2.6.7, Lombok, H2 DB, Open API

API available on localhost:8080/swagger-ui:

Films:

/films (POST) - add a new film (admin only)

/films (GET) - read all films

/films/{category} - read all films by category

Screenings:

/screenings (POST) - add a new film's screening (admin only)

/screenings (GET) - read all films screenings

/screenings/{filmID} - read all screenings by film id

Tickets:

/tickets (POST) - reserve a new ticket for film's screening

/tickets/{ticketId}/reservation/cancelled - cancel a ticket's reservation

/tickets (GET) - read all tickets (admin only)




