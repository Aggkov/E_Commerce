# E-Commerce

## Features
- REST API
- HTTPS
- Docker
- Payment with PayPal
- Mailing
- Checkout
- Export
- Filtering
- Pagination
- Keycloak
- Flyway

## Technology Stacks
**Backend**
- Java 17
- Spring Boot 3.3.3
- Spring Data JPA
- Hibernate
- PostgreSQL
- Maven

**Frontend**
- Angular 18
- Angular CLI
- Bootstrap

## How to  Run

git clone `<repository-url>`
cd your-repo

**Backend**

1. Run `docker-compose up -d` to start the docker container and load up keycloak and database migrations.
2. Go to `https://localhost:9090` and log in with `admin` `admin` as credentials. Go to the new realm `E-Commerce`.
   Main users are `user` `admin`. You can create more users and assign them roles `user` and `admin` from `frontend client`.
3. You might need to import the `local-ca.crt` and `localhost.crt` into your browser's trusted certificate authorities.
4. Create PayPal sandbox accounts and modify `client_id` and `client_secret` in application properties.
5. Test the app.

**Frontend**
1. Install [Node.js and npm](https://www.npmjs.com/get-npm)
2. `cd frontend`.
3. Run `npm install`.
4. Run `npm start`.
