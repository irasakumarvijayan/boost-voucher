# Voucher Application

A Spring Boot application for generating, validating, and listing voucher codes assigned to recipients for specific offers.

---

## Requirements

- Java 8
- Spring Boot
- Spring Data JPA
- MySQL
- Maven

---

## Features

- Generate unique vouchers for all recipients based on a special offer
- Validate a voucher code by email and usage status
- List all valid (non-expired, unused) vouchers for a recipient

---

## Database Setup

### 1. Create Database

```sql
CREATE DATABASE voucher;
```

---
### Files Need to check
```
1. database.sql - SQL Query Statement added 
2. Postman_endpoint - all the endpoint is listed
```
---

### Run Commands

```
mvn clean install
mvn spring-boot:run

```