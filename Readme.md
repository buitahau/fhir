### Endpoints

Actuator prometheus http://localhost:8081/domain-resource-service/actuator/prometheus

Prometheus http://localhost:9090/

Grafana http://localhost:3000/

Postgres admin http://localhost:8888/

Keycloak http://localhost:8080/

---

### Docker

#### Postgres and PGAdmin

``docker-compose -f docker-compose-postgres.yml up``

Go to http://localhost:8888/ with username/password is defined in .env

Then, create keycloak database.

#### Keycloak

``docker-compose -f docker-compose-keycloak.yml up``

Go to http://localhost:8080/ with username/password is defined in .env

### OPA

``docker run -p 8181:8181 openpolicyagent/opa run --server --log-level debug``

---

### Deploy

#### EFK (Elasticsearch - Fluentd - Kibana)

``kubectl apply -f k8s/efk-stack.yaml``

``kubectl -n kube-logging get services``

To get the external IP, tunnel minikube ``minikube tunnel``, get the external IP, then go to Kibana UI with port 5601

---

### Reference

Prometheus Grafana https://refactorfirst.com/spring-boot-prometheus-grafana

Fluentd https://arnoldgalovics.com/java-multiline-logs-fluentd/

OPA https://jcompetence.se/2023/06/22/open-policy-agent-opa-with-spring-boot-3/