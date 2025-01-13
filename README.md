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

**Deployment**
1. Create 2 RDS databases for core and payment-service and plug the endpoint username and password to the manifest files.
   Also run migrate on core `flyway -url=jdbc:postgresql://rds-endpoint/db-name -user=user -password=password -locations=filesystem:. migrate`
2. Create an S3 Bucket and upload images at `backend/core-service/uploads`
3. Create a EKS Cluster
`eksctl create cluster --name a name --region a region --nodegroup-name a group --node-type t3.medium --nodes 1`
4. cd to k8s/manifests and run `kubectl apply -f .`
5. Follow instructions at `https://docs.aws.amazon.com/eks/latest/userguide/lbc-manifest.html` and create an aws load balancer
6. Create a EC2 instance and follow instructions here `https://www.adaltas.com/en/2023/03/14/ec2-deploy-keycloak/` to create a keycloak
   Docker image. Then modify the url in keycloak.service.ts. Example url: 'http://ec2-public-ip:8080'. Access Keycloak console and import `realm.json`.
7. Access the app from ALB Controller created previously e.g. `k8s-default-ingress-dc60c41895-2121541636.eu-north-1.elb.amazonaws.com`
8. Create a PayPal sandbox account and plug `paypal.client-id` and `paypal.client-secret`

