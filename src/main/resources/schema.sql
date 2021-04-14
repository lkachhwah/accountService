
CREATE TABLE IF NOT EXISTS CUSTOMERDETAILS(
   `customerId`  VARCHAR(100) PRIMARY KEY,
   `name`  VARCHAR(100) NOT NULL UNIQUE,
   `emailId`  VARCHAR(100) NOT NULL,
   `gender`  VARCHAR(100) NOT NULL,
   `dob` DATE
);

CREATE TABLE IF NOT EXISTS `ACCOUNTDETAILS`
(	
	`accountNumber`  VARCHAR(100) PRIMARY KEY,
    `description` VARCHAR(100) NOT NULL,
    `customerId` VARCHAR(100) REFERENCES CUSTOMERDETAILS(customerId),
    `accountType` VARCHAR(100) NOT NULL,
    `openedByBranch`  VARCHAR(100) NOT NULL,
    `openedOn` DATE NOT NULL,
    `balance` DECIMAL(15,2) NOT NULL
);




CREATE TABLE IF NOT EXISTS `TRANSACTIONDETAILS`
(	
	`transactionId`  VARCHAR(15) NOT NULL UNIQUE,
    `customerId` VARCHAR(100) REFERENCES CUSTOMERDETAILS(customerId),
    `accountNumber` VARCHAR(100) REFERENCES ACCOUNTDETAILS(accountNumber),
    `transactionType` VARCHAR(10) NOT NULL,
    `description` VARCHAR(100) ,
    `trasactionDate` DATE NOT NULL,
    `amount` DECIMAL(15,2) NOT NULL,
    `status` VARCHAR(10)
);

