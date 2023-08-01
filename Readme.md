### Endpoints

#### Actuator prometheus

/actuator/prometheus

### Docker

#### Postgres and PGAdmin

``docker-compose -f docker-compose-postgres.yml up``

Go to http://localhost:8888/ with username/password is defined in .env

Then, create keycloak database.

#### Keycloak

``docker-compose -f docker-compose-keycloak.yml up``

Go to http://localhost:8080/ with username/password is defined in .env