
# Cinema manager

This is web app to manage a cinema built on modular monolith architecture.




## Tech Stack

**Language:** Java 21

**Frameworks:** Spring Boot 3, Hibernate, Junit

**Libraries:** Lombok, MapStruct, Liquibase, Testcontainers, SpringDoc (Swagger)

**Databases:** PostgresSQL

**Build tool:** Maven

**UI:** REST API

Admin user is added on start up.

```
  Credentials:
  username: admin@mail.com
  password: 12345
```

Halls are added on start up (halls_config.json file)

```json
[
  {
    "id": "1",
    "rowsNumber": 10,
    "rowSeatsNumber": 15
  },
  {
    "id": "2",
    "rowsNumber": 15,
    "rowSeatsNumber": 20
  }
]
```

## Features and API

### Available on:
```http
  localhot:8080/swagger-ui
```

### 1.Films

Endpoints:

Common user:

/films (GET) - search all films, optional params: /{title}, /{category}"

Admin:

/films (POST) - add a new film

/films/{id} (DELETE) - delete a film

### 2.Screenings

Endpoints:

Common user:

/screenings (GET) - search all screenings, optional params: /{date}

/screenings/{id}/seats (GET) - search seats by screening id

Admin:

/screenings (POST) - add a new screening

/screenings{id} (DELETE) - delete a screening

### 3.Halls

Endpoints:

Admin:

/halls (GET) - search all halls

### 4.Tickets

Endpoints:

/tickets (POST) - book a ticket

/tickets/{ticketId}/cancel (PATCH) - cancel ticket

/tickets/my (GET) - search user tickets

### 5.Users

Endpoints:

/users (POST) - create a new user

/users/password/reset (PATCH) - reset password (a mail with token will be sent)

/users/password/new (PATCH) - set new password after reset

Auth: basic auth

## How to run it

Download docker-compose.yaml file and run this command in the same folder:

```bash
  docker-compose up -d
```
    