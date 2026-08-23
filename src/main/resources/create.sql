drop database if exists bankingdb;

create database bankingdb;
use bankingdb;

/*
A USER ACCOUNT IS CREATED WHENEVER A ADMIN OR CUSTOMER ACCOUNT IS CREATED.NOW WE NEED TO MAP OUR CUSTOMER
TO THE CUSTOMER.
THE SERVICE REQUEST CLASS TAKES CARE OF HAVING THE REQUEST DETAILS MAPPED WITH CUSTOMER.
*/

create table User(
    userId INT AUTO_INCREMENT,
    username varchar(100) UNIQUE NOT NULL,
    password varchar(100) NOT NULL,
    role varchar(50) NOT NULL,
   constraint pk_product_id PRIMARY KEY (userId)
);

create table Customer(
    userId INT,
    customerId INT AUTO_INCREMENT,
    balance DECIMAL(10,2), -- USED DECIMAL(10,2) INSTEAD OF DOUBLE TO PREVENT FLOATING-POINT ROUNDING ERRORS.
    constraint pk_customer_user_id PRIMARY KEY (customerId),
    constraint customer_fk_user_id FOREIGN KEY (userId) REFERENCES User(userId)
);

create table Admin(
    adminId INT AUTO_INCREMENT,
    userId INT NOT NULL,
    constraint pk_admin_id PRIMARY KEY (adminId),
    constraint admin_fk_user_id FOREIGN KEY (userId) REFERENCES User(userId)
);
#TABLE TO HANDLE DEPOSIT REQUESTS.

create table Deposit(
    depositId INT AUTO_INCREMENT,
    customerId INT NOT NULL,
    depositAmount DECIMAL(10,2) NOT NULL CHECK(depositAmount>0),
    depositDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,-- Automatically records the exact date and time it was inserted
    constraint pk_deposit_id PRIMARY KEY (depositId),
    constraint fk_deposit_customer_id FOREIGN KEY (customerId) REFERENCES Customer(customerId)
);

#TABLE TO HANDLE WITHDRAWAL REQUESTS.

create table Withdraw(
    withdrawId INT AUTO_INCREMENT,
    customerId INT NOT NULL,
    withdrawalAmount DECIMAL(10,2) NOT NULL CHECK(withdrawalAmount>0),
    withdrawalDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP, # Automatically records the exact date and time it was inserted
    constraint pk_withdraw_id PRIMARY KEY (withdrawId),
    constraint fk_withdraw_customer_id FOREIGN KEY (customerId) REFERENCES Customer(customerId)
);
-- I HAVE MADE LOAN,CREDIT CARD REQUESTS INTO A SINGLE TABLE
create table serviceRequest(
    requestId int AUTO_INCREMENT, # TO UNIQUELY IDENTIFY REQUEST
    customerId int NOT NULL,
    requestType varchar(50) NOT NULL, # CAN BE LOAN OR CREDIT CARD REQUEST
    requestAmount DECIMAL(10,2) NOT NULL CHECK(requestAmount>=0),
    requestStatus varchar(50) DEFAULT 'pending', # CAN BE PENDING,APPROVED OR REJECTED
    requestDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,#Automatically records the exact date and time it was inserted
    constraint pk_request_id PRIMARY KEY (requestId),
    constraint fk_serviceRequest_customer_id FOREIGN KEY (customerId) REFERENCES Customer(customerId)
);


