# MyBank

A banking application built with microservices. All services live in this single repository.

## Services

| Folder | Description |
|---|---|
| `account` | Clients, accounts and balances |
| `authentication` | User login and authentication |
| `bank-frontend` | User interface |
| `frauddetection` | Checks transactions for fraud |
| `reporting` | Generates client reports |
| `transaction` | Deposits, withdrawals, transfers and transaction history |

## Tech stack

* Java, Spring Boot, Maven
* Node.js / npm (frontend)

## Getting started

Run a backend service (repeat for each one in a separate terminal):

```bash
cd account
mvn spring-boot:run
```

Run the frontend:

```bash
cd bank-frontend
npm install
npm start
```

## Configuration

Each backend service is configured in `src/main/resources/application.yml` (port, URLs of other services, etc.).

## Running tests

```bash
cd <service-folder>
mvn test
```

## Contributing

1. Create a branch from `main`: `git checkout -b feature/<short-description>`
2. Commit your changes with clear messages
3. Open a pull request and request a review
