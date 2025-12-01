Flight Booking System – Microservices Architecture
This project implements a Flight Booking System using Spring Boot (Web), Java 17, and MongoDB. The system is designed as a set of microservices, each responsible for a specific domain, with centralized configuration and resilient communication patterns.

Goals
Build a modular airline booking platform with microservices.

Manage airlines, flights, passengers, and bookings independently.

Enable asynchronous communication with RabbitMQ for email notifications.

Externalize configuration with Spring Cloud Config Server.

Route and secure traffic via API Gateway.

Ensure resilience with Circuit Breaker patterns.

Features Implemented
Airline Service: add and list airlines.

Flight Service: booking workflow, seat validation, one‑way and round‑trip pricing.

Passenger Service: manage passenger details linked to bookings.

Booking Service: generate PNRs, maintain booking history, retrieve tickets.

RabbitMQ Integration:

Producer publishes booking events (BOOKING_CREATED, BOOKING_DELETED).

Consumer listens and sends email notifications via JavaMailSender.

Spring Cloud Config Server: centralized configuration for all services.

Config Server link: https://github.com/Pranitha161/flightbookingsystem-config-server

API Gateway: single entry point for routing requests to microservices.

Circuit Breaker: isolates failures and ensures graceful fallback in case of service downtime.

Global Exception Handling: validation errors handled consistently across services.

Testing:

Unit tests with JUnit 5 and Mockito.

Integration tests with MockMvc/WebTestClient.

Load testing with JMeter (performance summary reports).

Code Quality: SonarQube analysis for coverage, bugs, and maintainability.

Tech Stack
Spring Boot (Web) – REST APIs

Spring Cloud Config Server – centralized configuration

Spring Cloud Gateway – API Gateway

Resilience4j Circuit Breaker – fault tolerance

RabbitMQ – messaging broker for booking events

MongoDB – persistence layer

Jakarta Validation – input validation

JUnit 5 + Mockito – unit testing

MockMvc / WebTestClient – integration testing

Apache JMeter – performance/load testing

SonarQube – static analysis and code quality
