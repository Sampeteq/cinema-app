
# Cinema manager

This is web app to manage a cinema builded on modular monolith architecture.




## Tech Stack

**Language:** Java 17

**Frameworks:** Spring Boot 3, Hibernate, Junit

**Libraries:** Open API (Swagger)

**Databases:** PostgresSQL

**Build tool:** Maven

**UI:** REST API


## Features and API

### Available on:
```http
  localhot:8080/swagger-ui
```

### 1.Storing and searching films and screenings

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

### 2.Booking making

Endpoints:

/seats?screeningId= (GET) - search screening seats

/bookings (POST) - make a booking for seat

/bookings/{bookingId}/cancelled (POST) - cancel booking

/bookings/my (GET) - search user bookings

### 3.User account

Endpoints:

/sign-up (POST) - create a new user account

/password/reset (POST) - reset password (a mail with token will be sent)

/password/new (POST) - set new password after reset

Auth: basic auth

## How to run it

Download docker-compose.yaml file and run this command in the same folder:

```bash
  docker-compose up -d
```
    