# Saga Pattern - Orchestration

This is a sample project to demo saga pattern.

## Prerequisites:

* Kafka cluster

# High Level Architecture

![](doc/saga-orchestration.png)

* Prometheus: http://localhost:9090
* Grafana: http://localhost:3000
* Order service: http://localhost:8080/webjars/swagger-ui/index.html


Production info:
* Kafka: https://console.upstash.com/kafka/51eacce7-918a-460f-a1ed-9326d5cda6d4
* Database: https://customer.elephantsql.com/instance

Run prod docker compose
```
docker-compose -f docker-compose-prod.yml up
```