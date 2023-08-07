### URL

Get metadata

http://localhost:8080/fhir/metadata

### Document Restful APIs

Follow this guide to define the Restful APIs

https://hapifhir.io/hapi-fhir/docs/server_plain/rest_operations.html

### Build image

``docker build -t fhir .``

``docker images``

``docker run -p 8081:8080 fhir``

### Docker

#### Postgres and PGAdmin

``docker-compose -f docker-compose-postgres.yaml up``

Go to http://localhost:8888/ with username/password is defined in .env

Then, create keycloak database.

#### Keycloak

``docker-compose -f docker-compose-keycloak.yaml up``

Go to http://localhost:8080/ with username/password is defined in .env