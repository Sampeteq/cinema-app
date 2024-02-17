
# Cinema manager

This is web app to manage a cinema built on modular monolith architecture.




## Tech Stack

**Language:** Java 21

**Frameworks:** Spring Boot 3, Hibernate, Junit

**Libraries:** Lombok, Liquibase, Testcontainers, SpringDoc (Swagger)

**Databases:** PostgresSQL

**Build tool:** Maven

**UI:** REST API

Admin user is added on start up.

```
  Credentials:
  username: admin@mail.com
  password: 12345
```

Example request body for creating hall (might be helpful) :

```json
{
    "seats" : [
      {"rowNumber": 1, "number": 1},
      {"rowNumber": 1, "number": 2},
      {"rowNumber": 1, "number": 3},
      {"rowNumber": 1, "number": 4},
      {"rowNumber": 1, "number": 5},
      {"rowNumber": 1, "number": 6},
      {"rowNumber": 1, "number": 7},
      {"rowNumber": 1, "number": 8},
      {"rowNumber": 1, "number": 9},
      {"rowNumber": 1, "number": 10},

      {"rowNumber": 2, "number": 1},
      {"rowNumber": 2, "number": 2},
      {"rowNumber": 2, "number": 3},
      {"rowNumber": 2, "number": 4},
      {"rowNumber": 2, "number": 5},
      {"rowNumber": 2, "number": 6},
      {"rowNumber": 2, "number": 7},
      {"rowNumber": 2, "number": 8},
      {"rowNumber": 2, "number": 9},
      {"rowNumber": 2, "number": 10}
    ]
}
```

## Features and API

### Available on:
```http
  localhot:8080/swagger-ui
```

### 1.Films

Endpoints:

/public/films (GET) - get all films, optional params: /{title}, /{category}"

/admin/films (POST) - add a new film

/admin/films/{id} (DELETE) - delete a film

### 2.Screenings

Endpoints:

/public/screenings (GET) - get all screenings, optional params: /{date}

/admin/screenings (POST) - add a new screening

/admin/screenings/{id} (DELETE) - delete a screening

### 3.Halls

Endpoints:

/admin/halls (GET) - get all halls

/admin/halls (POST) - add a hall

/admin/halls/{id} (DELETE) - delete a hall

### 4.Tickets

Endpoints:

/tickets?screeningId= (GET) - get all tickets by screening id

/tickets/book (PATCH) - book a ticket

/tickets/{ticketId}/cancel (PATCH) - cancel ticket

/tickets/my (GET) - get user tickets

### 5.Users

Endpoints:

/public/users (POST) - create a new user

/public/users/password/reset (PATCH) - reset password (a mail with token will be sent)

/public/users/password/new (PATCH) - set new password after reset

Auth: basic auth

## How to run it

Download docker-compose.yaml file and run this command in the same folder:

```bash
  docker-compose up -d
```
    