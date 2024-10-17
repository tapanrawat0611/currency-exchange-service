# Currency Exchange and Billing System

## Problem Statement

This project is a Spring Boot application that integrates with a currency exchange API to calculate the payable amount for a bill after applying discounts based on user type. It allows users to submit a bill in one currency and get the payable amount in another currency.

## Solution

The solution consists of:
- **CurrencyExchangeService**: Retrieves real-time exchange rates from a third-party API.
- **DiscountService**: Applies discounts based on user type and bill conditions.
- **PaymentController**: Handles bill processing requests and returns the payable amount.

## Project Structure

Spring Boot "Currency-Exchange" Example Project
This is a sample Java / Maven / Spring Boot (version 3.1.1) application that can be used as a starter for 
creating a microservice. I hope it helps you.

Requirements:
For building and running the application you need:
	1. JDK 17
	2. Maven 3.1

How to Run:
This application is packaged as a jar. 
	1. Clone this repository.
	2. Make sure you are using JDK 17 and Maven 3.x.
	3. You can build the project running mvn clean install package.
	4. Once successfully built, you can run the main method in the CurrencyExchangeServiceApplication class 
		from your IDE.
	5. Once application is up and running. You can open any API testing tool like POSTMAN and run the API. 


Currency Conversion API

Request Type : POST
URL: '/api/calculate'
Authentication: Basic Auth (user/password)

Request Body:
{
    "amount": 100,
    "userType": "employee",
    "isGrocery": false,
    "customerSince": "2021-01-01T00:00:00",
    "originalCurrency": "USD",
    "targetCurrency": "EUR"
 } 
  

Expected Response:
{
    "payableAmount": 5465.8102200,
    "currency": "INR"
}

