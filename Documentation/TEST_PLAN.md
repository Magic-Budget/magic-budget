# Test Plan

## Frontend Testing

The purpose of this test plan is to evaluate the end-to-end nature of
the application by testing the frontend navigation to ensure that
movement through the application is consistent and proper.

### Scope

The scope of this test is to test the main pages of the application that
the user will see and interact with to ensure that consistency is met,
and core functionality is retained.

### Tool Kit

The tool that is being used to test the frontend application is
Playwright as it allows for easily writing tests to interact with the
frontend application.

## Backend Testing

The purpose of this test plan is to evaluate the reliability of the
Spring Boot application, since the applications logic should remain
consistent across many different changes to the codebase to ensure that
new or old code is remaining logically sound.

### Scope

The scope of this test is primarily focused on the controller and
service layers of the application they can be easily tested. While the
repository cannot be easily test while are indirectly tested in the
service and controller layer. While focusing on testing the layers that
preform logic and not basic CRUD functionality to the application.

### Tool Kit

The tools that are being used to unit tests the backend application is
Junit and the built in Spring testing framework that enabled the
dependency creation in the unit test.

## Load Testing

The purpose of this test plan is to evaluate the performance and
reliability of the backend REST API under load. The primary focus is on
the authentication routes, as they are critical to the security and
usability of the application. Load testing will be conducted using
[Locust](https://locust.io/), an open-source load testing framework in
Python.

### Scope

The scope of the test will be on the “/api/auth/register” and
“/api/auth/sign-in” as they are critical authentication routes that must
be functional under load.

### Test Scenarios

The test scenario for this was with 1000 users concurrently accessing
the backend application to test registering and login to the created
accounts.

### Results

| Name | Request Count | Median Response Time | Average Response Time | Min Response Time | Max Response Time | Average Content Size | Requests/s |
|----|----|----|----|----|----|----|----|
| /api/auth/register | 7000 | 20000 | 20243.71762 | 205.8819 | 50945.235 | 247 | 11.92529497 |
| /api/auth/sign-in | 6449 | 16000 | 19185.18738 | 101.2618 | 48368.7001 | 247 | 10.98660389 |
| Aggregated | 13449 | 18000 | 19736.13627 | 101.2618 | 50945.235 | 247 | 22.91189886 |
