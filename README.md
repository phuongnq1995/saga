# Saga Pattern - Orchestration

This is a sample project to demo saga pattern.

## High Level Architecture

![](doc/saga-orchestration.png)

### Context

Consider the real world example for transactions across distributed systems.
When user process orders, order-service needs manage inventory, and handle payments.
We need a solution that ensures data consistency and reliability, even in the face of failures or network issues.
That's where the Saga Pattern comes in.

### Sample Data

Given the hardcode data stored in services

**Order service:**

| Product Id  | Product Name | Price |  
|-------------|--------------|-------|
| 1           | Product A    | 100   |
| 2           | Product B    | 200   |
| 3           | Product C    | 300   |

**Payment service:**

| User Id | Balance |
|---------|---------|
| 1       | 1000    |
| 2       | 1000    |
| 3       | 1000    |

**Inventory service:**

| Product Id  | Quantity |
|-------------|----------|
| 1           | 5        |
| 2           | 5        |
| 3           | 5        |

## Live demo

[![Watch the video](https://img.youtube.com/vi/i7HbwnqPdWg/maxresdefault.jpg)](https://youtu.be/i7HbwnqPdWg)

**Production info:**

* Kafka: https://console.upstash.com/kafka/51eacce7-918a-460f-a1ed-9326d5cda6d4
* Database: https://customer.elephantsql.com/instance

**Run prod docker compose**

```
docker-compose -f docker-compose-prod.yml up -d
```

**Clean up data**

* http://localhost:8080/webjars/swagger-ui/index.html
* http://localhost:8081/inventory/swagger-ui/index.html
* http://localhost:8082/payment/swagger-ui/index.html