# Currency Exchange and Billing System

## Problem Statement

This project is a Spring Boot application that integrates with a currency exchange API to calculate the payable amount for a bill after applying discounts based on user type. It allows users to submit a bill in one currency and get the payable amount in another currency.

## Solution

The solution consists of:
- **CurrencyExchangeService**: Retrieves real-time exchange rates from a third-party API.
- **DiscountService**: Applies discounts based on user type and bill conditions.
- **CurrencyExchangeController**: Handles bill processing requests and returns the payable amount.


## Build Steps

1. Clone the repository.
2. Add your API key for currency exchange service.
3. Run `mvn clean install` to build the project.
4. Run the application with `mvn spring-boot:run`.
5. Access the API at `http://localhost:8082/api/bill/calculate`.

## Test Coverage Report

1. Run `mvn test` to execute unit tests.
2. Coverage report can be generated using a plugin like `jacoco`.

## Assumptions

1. The user type is one of "employee", "affiliate", or "customer".
2. Currency codes are 3-letter ISO codes.

## Steps to Run
This application is packaged as a jar. 
1. Clone this repository.
2. Make sure you are using JDK 17 and Maven 3.x.
3. You can build the project running mvn clean install package.
4. Once successfully built, you can run the main method in the CurrencyExchangeServiceApplication class 
from your IDE.
5. Once application is up and running. You can open any API testing tool like POSTMAN and run the API. 

## UML Diagram
UML diagram is added on this path - src/main/resources/templates/UML_image.png
![UML Diagram](./resources/templates/UML_image.png)

## Currency Conversion API

**Request Type**: POST
**URL**: '/api/calculate'
**Authentication**: Basic Auth (user/password)

**Request Body**:
{
    "amount": 100,
    "userType": "employee",
    "isGrocery": false,
    "customerSince": "2021-01-01T00:00:00",
    "originalCurrency": "USD",
    "targetCurrency": "EUR"
 } 
  

**Expected Response**:
{
    "payableAmount": 5465.8102200,
    "currency": "INR"
}

