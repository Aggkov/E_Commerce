# E-Commerce

## Features
- REST API
- HTTPS
- Docker
- Payment with PayPal
- Cart & order management
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

git clone https://github.com/yourusername/your-repo.git
cd your-repo

**Backend**

1. Install [PostgreSQL](https://www.postgresql.org/download/) (Only if not using Docker)
2. Run `docker-compose up -d` to start the docker container and load up keycloak and database migrations.
3. Go to localhost:9090 and log in with `admin` `admin` as credentials. Create a new realm `E-Commerce` by importing `realm-export.json` file.
   Main users are `user` `admin`. You can create more users and assign them roles `user` and `admin` from `frontend client`.
4. `cd backend`.
5. Run `mvn clean install`
6. Open a terminal with admin privileges and Import Certificates into `cacerts` `JAVA_HOME/lib/security/cacerts`. Also set your IDE to use the JDK specified in `-keystore` command.
   a) From project root run 
      `keytool -importcert -trustcacerts -alias keycloak-cert -file ./keycloak-cert.crt -keystore "Your absolute path to cacerts" -storepass changeit`
   b) `cd backend` and run for all microservices
      `discovery`
      `keytool -importcert -trustcacerts -alias discovery-cert -file ./discovery-server/src/main/resources/discovery-cert.crt -keystore "Your absolute path to cacerts" -storepass changeit`
         
      `api-gateway`
      `keytool -importcert -trustcacerts -alias gateway-cert -file ./api-gateway/src/main/resources/gateway-cert.crt -keystore "Your absolute path to cacerts" -storepass changeit`
    
      `core`
      `keytool -importcert -trustcacerts -alias core-cert -file ./api-gateway/src/main/resources/core-cert.crt -keystore "Your absolute path to cacerts" -storepass changeit`
      
      `payment`
      `keytool -importcert -trustcacerts -alias payment-cert -file ./payment/src/main/resources/payment-cert.crt -keystore "Your absolute path to cacerts" -storepass changeit`

7. Create PayPal sandbox accounts and modify `client_id` and `client_secret` in application properties.
8. Run the app.

**Frontend**
1. Install [Node.js and npm](https://www.npmjs.com/get-npm)
2. `cd frontend`.
3. Run `npm install`.
4. Run `npm start`.
