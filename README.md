# Online Banking System

A Spring Boot–based **Online Banking System** that supports two user roles: **Customer** and **Admin**.  
The project is structured using a clean 4-layer architecture (**Model, Controller, Service, DAO**) and uses **JDBC with MySQL** for database connectivity.

---

## Users

- **Customer**
- **Admin**

---

## Features

### Customer Services
- Create a new customer account
- View customer details by ID
- Delete customer account
- Update account password
- Deposit money
- Withdraw money (with balance checks)
- Raise loan requests
- Raise credit card requests

### Admin Services
- View all customer details
- View deposit history
- View withdrawal history
- Approve loan/credit card requests
- Reject loan/credit card requests

---

## Architecture

This project follows a **4-layer architecture**:

1. **Model** – Entity/data classes  
2. **Controller** – REST API handling  
3. **Service** – Business logic and validation  
4. **DAO** – Data access and database operations  

### Recent Refactoring
- DAO operations separated from service logic  
- Service layer streamlined to business logic only  
- Implementations split and cleaned up for maintainability  
- JDBC integration introduced for MySQL persistence  

---

## Tech Stack

- Java 21  
- Spring Boot  
- Maven  
- JDBC  
- MySQL  
- Lombok  

---

## Project Structure (High-Level)

```text
Online_Banking_System_sem_3_project/
├── src/
│   ├── main/
│   │   └── java/com/banking/OnlineBankingSystem/
│   │       ├── controller/
│   │       ├── service/
│   │       ├── dao/
│   │       │   └── databaseConnection.java
│   │       ├── model/
│   │       └── ...
│   └── test/
├── CustomerDetails.json
├── Deposits.json
├── Withdrawals.json
├── LoanRequests.json
├── CreditCardRequests.json
├── API_TESTING.md
├── pom.xml
└── README.md
```

> Note: JSON files are still present from file-based implementation/testing paths, while JDBC + MySQL support has been added.

---

## Setup & Run

### 1) Prerequisites
- Java 21
- Maven 3.8+
- MySQL 8+

### 2) Clone Repository
```bash
git clone https://github.com/anishnagubandi/Online_Banking_System_sem_3_project.git
cd Online_Banking_System_sem_3_project
```

### 3) Configure JDBC Database Connection
This project uses a dedicated JDBC connection class:

- `src/main/java/com/banking/OnlineBankingSystem/dao/databaseConnection.java`

Update your MySQL details in that file:
- JDBC URL (example): `jdbc:mysql://localhost:3306/online_banking_system`
- Username
- Password

### 4) Build and Run
```bash
mvn clean install
mvn spring-boot:run
```

Base URL:  
`http://localhost:8084/`

---

## API Endpoints

### Customer APIs

| Method | Endpoint | Description |
|---|---|---|
| POST | `/customer` | Create a new customer |
| DELETE | `/customer/delete/{customerId}` | Delete customer account and history |
| GET | `/customer/id/{customerId}` | Get customer details by ID |
| PUT | `/id/{customerId}/{oldPassword}/{newPassword}` | Update customer password |
| PUT | `/customer/deposit/{customerId}` | Deposit money |
| PUT | `/customer/withdraw/{customerId}` | Withdraw money |
| POST | `/customer/request/loan/{customerId}` | Create loan request |
| POST | `/customer/request/creditcard/{customerId}` | Create credit card request |

### Admin APIs

| Method | Endpoint | Description |
|---|---|---|
| GET | `/admin/customerDetails` | Get all customer details |
| GET | `/admin/deposits` | Get deposit history |
| GET | `/admin/withdrawals` | Get withdrawal history |
| PUT | `/admin/approve/{customerId}/{requestType}/{requestId}` | Approve loan/credit card request |
| PUT | `/admin/reject/{customerId}/{requestType}/{requestId}` | Reject loan/credit card request |

---

## Sample Request JSON

### Create Customer
```json
{
  "username": "customerXYZ",
  "password": "secure@123",
  "balance": 0.0,
  "serviceRequests": []
}
```

### Deposit
```json
{
  "customerId": 7,
  "depositAmount": 110.0
}
```

### Withdrawal
```json
{
  "username": "userAlpha",
  "withdrawalAmount": 10.0
}
```

### Loan Request
```json
{
  "type": "Loan",
  "amount": 1234.0
}
```

### Credit Card Request
```json
{
  "type": "CreditCard",
  "amount": 12344.0
}
```

---

## Testing

For detailed API endpoint testing notes and examples, refer to the [API_TESTING](API_TESTING.md).

---

## Collaborators

- **Mitansh Shringi** — Admin Controller and Service components  
- **Muawiyah Surve** — Loan and Credit Card request logic  
- **Anish Nagubandi** — Model layer, Customer Controller/Service components, and JDBC integration  

---

## Demo Video (Old JSON-Only Version)

The following demo was recorded for the **earlier file-based implementation (JSON storage only)**, before JDBC + MySQL integration:

`https://1drv.ms/v/c/cbfad922f512e4f1/IQCxQNU3AjEJSbunf8RlAFCaATIithbz6C5ljlMyZ92mdkk?e=xlZTgM`
