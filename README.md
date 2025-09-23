# Spring Boot Webhook App

This app sends a POST request to generate a webhook, solves a SQL problem, and submits the solution to the webhook using a JWT token. All logic runs on startup.

## How it works
- On startup, sends POST to generate webhook
- Receives webhook URL and JWT token
- Solves SQL problem (Question 1)
- Submits SQL query to webhook

## Build
```
mvn clean package
```

## Run
```
java -jar target/webhook-0.0.1-SNAPSHOT.jar
```
