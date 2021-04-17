
CREATE TABLE IF NOT EXISTS CUSTOMERDETAILS(
   `customerId`  VARCHAR(100) PRIMARY KEY,
   `name`  VARCHAR(100) NOT NULL UNIQUE,
   `emailId`  VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS `ACCOUNTDETAILS`
(	
	`accountId`  VARCHAR(100) PRIMARY KEY,
    `customerId` VARCHAR(100) REFERENCES CUSTOMERDETAILS(customerId),
    `country` VARCHAR(100) NOT NULL,
    `openedOnDate` DATE NOT NULL,
    `balance` DECIMAL(15,2) NOT NULL,
    `allowedCurrencies` VARCHAR(100) NOT NULL
);


CREATE TABLE IF NOT EXISTS `TRANSACTIONDETAILS`
(	
	
	`transactionId`  VARCHAR(15) NOT NULL UNIQUE,
    `customerId` VARCHAR(100) REFERENCES CUSTOMERDETAILS(customerId),
    `accountId` VARCHAR(100) REFERENCES ACCOUNTDETAILS(accountId),
    `transactionType` VARCHAR(10) NOT NULL,
    `description` VARCHAR(100) ,
    `trasactionDate` DATE NOT NULL,
    `amount` DECIMAL(15,2) NOT NULL,
    `status` VARCHAR(10),
    `transactionCurrency` VARCHAR(10),
    `accountBalance` DECIMAL(15,2) NOT NULL
    
);

