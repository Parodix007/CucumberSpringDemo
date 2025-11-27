# Cucumber Spring Demo

Demo app for BDD Cucumber tests with Spring Boot and [DummyJson](https://dummyjson.com/)

## Business drivers

- We need realistic BDD test scenarios that include user authentication and authorization.
- We want to use a free, publicly available API.
- We want user journeys that span multiple domains.

## Technical drivers

- The API must support JWT-based authentication.
- The API should be stable, publicly accessible.
- The API should expose multiple related resources to enable end-to-end scenarios.

## Decision

To satisfy these drivers, we use the public [DummyJson](https://dummyjson.com/), which provides JWT-based authentication
and resources from multiple domains that can be combined into rich end-to-end stories.

## Tech stack

* Cucumber
* Rest Assured
* JUnit 5
* Spring Boot


