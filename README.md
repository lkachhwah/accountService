# Pre install software need:
~~~
* Rabbit MQ
* MVN
* Java 8 JDK
~~~

#Steps To Run:
~~~
1.check out Project from repository : https://github.com/lkachhwah/accountService.git ,using git .
2.Check above Software are installed and keep rabbit MQ up and running.
3. Go to application.properties 
  1. Update RabbitMq property :
	    reportservice.topic.name=myTestQueue
	    accountservice.exchange.name=myTestQueueExchange
	    accountservice.topic.name=myTestQueue
	    accountservice.routing.key=myTestQueueKey
  
  2. Update Currency Exchange rate property :
	    accountservice.exchange.rate.usd.to.eur=0.83
	    accountservice.exchange.rate.usd.to.sek=8.44
	    accountservice.exchange.rate.usd.to.gbp=0.72

4. Go to the path where project is checkout and run command "mvn clean install"
5. Once build is ready run application using command : java -jar accountservice-0.0.1-SNAPSHOT.jar
6. Verification step check log And we should be able see log : "*************Started************" .
7. Open the swagger ui : localhost:<server.port>/swagger-ui.html
~~~
#Operation Details

###Customer  Operation Details :

1.Currently no api is provided to add and get Customer details, and this are created on start up of application ,based on data provided in file  data.sql. If you wan to
  more customer  add entry in this file below is sample request:
  
  INSERT INTO CustomerDetails VALUES ('<CustomerID>', '<Customer Name>','<Customer Email>');
  

###Account Operation Details :

1. Create Account api [POST]:
	- This endpoint is used to create the account for a customer ,with initial amount(USD) and list of supported currency.Please refer below sample request and attribute
	  details.
		Request:
		URL :http://localhost:<server.port>/account
		Body:
		{
		  "balance": 99.555,  - Initial expected balance.
		  "country": "India", - Country where this account is opened
		  "currencies": [   - list of supported Currency - possible values -EUR, SEK, GBP, USD
		    "EUR",
			"SEK",
			"USD"
		  ],
		  "customerId": "lkachhwah"  - CustomerId, Make sure this customer exist in system before creating account. Default customer    
		                               exist in sytem using which we can perform operation:lkachhwah,pkachhwah. 
		}
		  Response:
		   {
			  "accountId": "1618755265713", - Genrated Account number
			  "customerId": "lkachhwah",
			  "country": "India",
			  "openedOnDate": "2021-04-18T14:14:25.713+00:00",
			  "balanceInDifferentCurrency": [
			    {
			      "amount": 82.63065,
			      "currency": "EUR"
			    },
			    {
			      "amount": 840.2442,
			      "currency": "SEK"
			    },
			    {
			      "amount": 99.555,
			      "currency": "USD"
			    }
			  ]
			}
		
2. Get  Account Details api [GET]:
  -	This endpoint is used to get  the account detail for a accountId.
    Request:
        Header - accountId: <Account number generated in create call> e.g: 1618755265713
        URL : http://localhost:<server.port>/account
        
    Response:
		    {
		  "accountId": "1618755265713",
		  "customerId": "lkachhwah",
		  "country": "India",
		  "openedOnDate": "2021-04-17T18:30:00.000+00:00",
		  "balanceInDifferentCurrency": [
		    {
		      "amount": 82.6348,
		      "currency": "EUR"
		    },
		    {
		      "amount": 840.2864,
		      "currency": "SEK"
		    },
		    {
		      "amount": 99.56,
		      "currency": "USD"
		    }
		  ]
		}

###Transaction Operation Details :
1. Perform Transaction [POST]:
   - This endpoint is used to perform trasaction for a accountId.Please refer below sample request and attribute details.
      Request:
        URL :http://localhost:<server.port>/transaction
        Body:
		{
			  "accountId": "1618755265713", - Perform on accountID
			  "amount": 10,   - Amount of trasaction 
			  "description": "Token Amount", - Detail about transaction
			  "transactionCurrency": "EUR",   - Currency of transaction,  possible values -EUR, SEK, GBP, USD
			  "transactionType": "OUT"   Mode of operation - IN,OUT.
			}
	  Response:
				  {
			  "transactionId": "1618756518024",
			  "customerId": "lkachhwah",
			  "accountId": "1618755265713",
			  "transactionType": "OUT",
			  "description": "Token Amount",
			  "trasactionDate": "2021-04-18T14:35:18.024+00:00", 
			  "amount": 10,
			  "status": "SUCCESS",
			  "transactionCurrency": "EUR",
			  "accountBalance": "72.65",  - remaining balance after operation in transactionCurrency.
			  "accountBalanceInUSD": "87.51" - remaining balance after operation in USD.
			}
        
2.Transaction Details for a AccountId [GET]:
   - This endpoint is used to get all  trasactions details  for a accountId.Please refer below sample request and attribute details.
        Request:
        Header - accountId: <Account number generated in create call> e.g: 1618755265713
        URL : http://localhost:<server.port>/transaction
	  Response:
			[	  {
			  "transactionId": "1618756518024",
			  "customerId": "lkachhwah",
			  "accountId": "1618755265713",
			  "transactionType": "OUT",
			  "description": "Token Amount",
			  "trasactionDate": "2021-04-18T14:35:18.024+00:00", 
			  "amount": 10,
			  "status": "SUCCESS",
			  "transactionCurrency": "EUR",
			  "accountBalance": "72.65",  - remaining balance after operation in transactionCurrency.
			  "accountBalanceInUSD": "87.51" - remaining balance after operation in USD.
			}]
