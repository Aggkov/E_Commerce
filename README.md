# E-Commerce

## Features
- REST API
- Docker
- Cart & order management
- Checkout
- Filtering
- Pagination
- Keycloak
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

git clone https://github.com/yourusername/your-repo.git
cd your-repo

**Backend**

1. Install [PostgreSQL](https://www.postgresql.org/download/) (Only if not using Docker)
2. Run `docker-compose up -d` to start the docker container and load up keycloak and database.
3. Go to localhost:9090 and log in with `admin` `admin` as credentials. Create a new realm by importing `realm-export.json` file.
   Activate email for `user` and `admin` from `frontend client` in keycloak management console. You can also create more users
   and assign them roles `user` and `admin` from `frontend client`.
4. `cd backend`.
5. Run `mvn clean install`
6. Run the app.

**Frontend**
1. Install [Node.js and npm](https://www.npmjs.com/get-npm)
2. `cd frontend`.
3. Run `npm install`.
4. Run `ng serve`.
