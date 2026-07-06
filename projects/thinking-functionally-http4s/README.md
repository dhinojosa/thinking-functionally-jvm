# Thinking Functionally http4s

A small http4s + doobie application for demonstrating functional service
composition, repositories as `ConnectionIO`, and transaction interpretation with
`ConnectionIO ~> IO`.

## Run

Start Postgres:

```bash
docker compose up -d postgres
```

Postgres runs the schema and seed data from `docker/postgres/init.sql` when the
container initializes.

Start the API:

```bash
sbt run
```

The app listens on `8080` by default. Use `PORT` to change it:

```bash
PORT=8082 sbt run
```

The app defaults to:

```text
jdbc:postgresql://localhost:15432/orders
```

Override with `DATABASE_URL`, `DATABASE_USERNAME`, and `DATABASE_PASSWORD`.

## Endpoints

```bash
curl http://localhost:8080/api/customers

curl http://localhost:8080/api/inventory

curl -X POST http://localhost:8080/api/orders \
  -H 'Content-Type: application/json' \
  -d '{"customerId":"cust-1"}'

curl -X POST http://localhost:8080/api/orders/1/items \
  -H 'Content-Type: application/json' \
  -d '{"productId":"prod-1","quantity":2}'

curl -X POST http://localhost:8080/api/orders/1/submit
```

`cust-1` has a small credit limit, so adding enough inventory should return an
error response from the service error channel.
