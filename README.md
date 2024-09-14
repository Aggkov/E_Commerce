# E-Commerce

## Features
- REST API
- Docker
- Cart & order management
- Checkout
- Filtering
- Pagination
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
2. Run `docker-compose up -d` to start the docker container and load up database and data.
3. Configure datasource in `application.properties or application.yml`. 
Set database name: e-commerce,
username and password: ecommerce_user
4. `cd backend`.
5. Run `mvn -N wrapper:wrapper`.
6. Run `mvn clean install`
7. Run `mvn spring-boot:run`.

**Frontend**
1. Install [Node.js and npm](https://www.npmjs.com/get-npm)
2. `cd frontend`.
3. Run `npm install`.
4. Run `ng serve`.
