# Online Banking System

## Users

- **Customer** (Multi-customer type)
- **Admin**


## Features

### 1) Customer banking services
- customer can check his balance.
- customer can do a deposit.
- customer can do a withdrawal (This depends on the amount the customer has in the account).
- customer can send a loan request asking for a loan.
- customer can send a creditcard request asking for a specific amount.

### 2) Admin management services
- admin can check the following:
  - customer details list of the bank.
  - deposits made in the bank.
  - withdrawals made from bank accounts in the bank.
- admin can approve or reject the following:
  - loan requests.
  - credit card requests asking for a specific amount.

## API endpoints used for customer related data and services.

- **POST** - `/customer` - for creating a new customer.

- **DELETE** - `/customer/id/{customerId}` - for deleting a customer account along with customer history.

- **GET** - `/customer/customerDetails` - for getting data of all customers.

- **GET** - `/customer/id/{customerId}` - for getting data of a specific customer.

- **PUT** - `/customer/id/{customerId}` - for updating details to customerDetails.json.

- **PUT** - `/customer/deposit/{customerId}` - for updating details to deposits.json and customer details.json during deposit.

- **PUT** - `/customer/withdraw/{customerId}` - for updating details to withdrawals.json and customer details.json during withdrawal.

- **POST** - `/customer/request/loan/{customerId}` - to allow the customer to create a loan request.

- **POST** - `/customer/request/creditcard/{customerId}` - to allow the customer to create a credit card request.

---

## API endpoints used for admin services.

- **GET** - `/admin/customerDetails` - to get details of all the customers.

- **GET** - `/admin/deposits` - to get deposit history of the bank.

- **GET** - `/admin/withdrawals` - to get withdrawal history of the bank.

- **PUT** - `/admin/approve/{customerId}/{requestType}/{requestId}` - it approves a loan or credit card request (changes reflect in respective requests.json file and customerdetails.json file) (pass requestType as a string with no quotes).

- **PUT** - `/admin/reject/{customerId}/{requestType}/{requestId}` - it rejects a loan or a credit card request (changes reflect in respective requests.json file and customerdetails.json file) (pass requestType as a string with no quotes).

## Collaborators and Contributions

- **Mitansh Shringi** — Handled the Controller and Service components of the Admin.

- **Muawiyah Surve** — Handled the logic of sending loanRequest and creditCardrequests.

- **Anish Nagubandi** — Handled the Model part and the Controller, Service parts of the Customer.

---

## Files used

- **CustomerDetails.json** — to store details of the customer.

- **Deposits.json** — to store deposits made in the bank.

- **Withdrawals.json** — to store withdrawal history of the bank.

- **LoanRequests.json** — to store loan requests.

- **CreditCardRequests.json** — to store requests regarding credit card amount withdrawal.
