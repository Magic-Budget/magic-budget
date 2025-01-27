# COMP 4350 Proposal - Team 4

## Project Summary and Vision

The project aims to create a comprehensive and user-friendly family budgeting and expense management application. The primary goal is to help families efficiently manage their finances by providing a centralized platform for tracking expenses, managing shared bills, and achieving savings goals. The application will cater to modern financial challenges by integrating innovative features like receipt scanning, and classification of expenditures.

---
Stakeholders include:

- **Primary Users:** Individuals, families, roommates, or groups managing shared expenses.
- **Secondary Users:** Financial advisors who may analyze data for budgeting advice.
- **Tertiary Users:** Development teams handling the app's infrastructure, integration, and scaling.

## Core Features

### Feature 1: Receipt Scanner

Uses want to scan there receipts so they can automatically keep track of their spending so they don’t have to manually import it.

### Feature 2: Tracking spending habits

Users want to see a log of transactions as well as the statistics of spending.

### Feature 3: Split and track shared payments

Users want to know how to properly split up bills and payments between other people.

### Feature 4: Keep track of credit and fixed income

Users want to know their debits and credits.

### Feature 5: Set saving goals

Users want to know how to split and save their income so that they can reach financial goals.

### Feature 6: Account and login

Users should have their own account.

### Feature 7: Support chat

AI enhanced chat that assists the users for finance related questions.

### Capacity/Testing Features

1,000 concurrent users accessing the application

## Technologies

The application will use Spring Boot for the web backend and Next.js integrated with ShadCN and Tailwind CSS for the frontend. The backend dependencies will be managed with Gradle, while NPM will handle dependencies for the frontend.

For continuous integration and delivery (CI/CD), we will utilize GitHub Actions, and the entire application will be containerized using Docker. The database of choice is PostgreSQL, as its one of the most popular database management systems. Additionally, the REST API in the Spring Boot backend will be documented using Swagger, providing a clear and interactive API reference for developers.

For testing the application we will use Playwright to test the website frontend. JUnit and Mockito to handle testing the backend of the application. While also using Locust to load test the backend.

## User Stories

### Receipt Scanner

![Feature 1](Documentation\UserStories\Feature1.png)

### Tracking spending habits

![Feature 2](Documentation\UserStories\Feature2.png)

### Split and track shared payments

![Feature 3](Documentation\UserStories\Feature3.png)

### Keep track of credit and fixed income

![Feature 4](Documentation\UserStories\Feature4.png)

### Set saving goals

![Feature 5](Documentation\UserStories\Feature5.png)

### Account and login

![Feature 6](Documentation\UserStories\Feature6.png)

### Support chat

![Feature 7](Documentation\UserStories\Feature7.png)
