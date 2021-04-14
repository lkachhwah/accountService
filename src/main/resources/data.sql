
INSERT INTO CustomerDetails
VALUES ('1', 'Lalit Kachhwah','lalitkkachhwah@gmail.co','MALE',TO_DATE('1989/06/01', 'yyyy/mm/dd'));

INSERT INTO CustomerDetails
VALUES ('2', 'Priyanka Kachhwah','priyankakachhwah@gmail.co','FEMALE',TO_DATE('1988/06/01', 'yyyy/mm/dd'));



INSERT INTO ACCOUNTDETAILS
VALUES ('CITI123456', 'Saving account','1','INDIVIDUAL','Bajaj Nagar',TO_DATE('2003/05/03', 'yyyy/mm/dd'),55555.33);

INSERT INTO ACCOUNTDETAILS
VALUES ('CITI123457', 'Fixed Deposit','1','CERTIFICATE_OF_DEPOSIT','Bajaj Nagar',TO_DATE('2021/01/03', 'yyyy/mm/dd'),99999.33);


INSERT INTO ACCOUNTDETAILS
VALUES ('CITI123458', 'Fixed Deposit','2','CERTIFICATE_OF_DEPOSIT','Bajaj Nagar',TO_DATE('2021/01/02', 'yyyy/mm/dd'),66666.33);



INSERT INTO TRANSACTIONDETAILS
VALUES ('T1', '1','CITI123456','DEBIT','REpayment of loan',TO_DATE('2021/01/02', 'yyyy/mm/dd'),50.33 ,'SUCCESS');
INSERT INTO TRANSACTIONDETAILS
VALUES ('T2', '1','CITI123457','CREDIT','Shopping',TO_DATE('2021/01/02', 'yyyy/mm/dd'),100.33 ,'SUCCESS');

