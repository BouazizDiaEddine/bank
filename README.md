# Application Setup

Before running the application, you need to install and start the required Docker containers.

## Requirements

- Docker
- Docker Compose

## Step 1: Start Docker Containers

Run the following command in your terminal:

```sh
    
     docker compose up
```
This will install and start two Docker containers: postgres-bank and pgadmin-bank. Make sure these containers are running before launching the application.

# Application documentation

After running the application, please visit the link bellow for the openApi documentation:
      
    http://localhost:8080/swagger-ui/index.html#/ 

## Population the database

# USERS

```json

    {
    "userId": 1000,
    "name": "Branden Gibson",
    "email": "BG@mail.com"
    },
    {
    {
    "userId": 1001,
    "name": "Georgina Hazel",
    "email": "GH@mail.com"
    },
    {
    "userId": 1002,
    "name": "Arisha Barron",
    "email": "AB@mail.com"
    }
```

# CURRENCIES

```json

    {
    "currencyId": 1000,
    "value": "euro",
    "name": "euro"
    },
    {
    "currencyId": 1001,
    "value": "DZD",
    "name": "Algerian Dinar"
    },
    {
    "currencyId": 1002,
    "value": "LIRA",
    "name": "lira"
    }
```

# EXCHANGES

```json

[
  {
    "exchangeId": 1003,
    "fromCurrency": {
      "currencyId": 1000,
      "value": "euro",
      "name": "euro"
    },
    "toCurrency": {
      "currencyId": 1005,
      "value": "LIRA",
      "name": "lira"
    },
    "exchangeRate": 50
  },
  {
    "exchangeId": 1004,
    "fromCurrency": {
      "currencyId": 1005,
      "value": "LIRA",
      "name": "lira"
    },
    "toCurrency": {
      "currencyId": 1000,
      "value": "euro",
      "name": "euro"
    },
    "exchangeRate": 0.02
  },
  {
    "exchangeId": 1005,
    "fromCurrency": {
      "currencyId": 1000,
      "value": "euro",
      "name": "euro"
    },
    "toCurrency": {
      "currencyId": 1001,
      "value": "DZD",
      "name": "Algerian Dinar"
    },
    "exchangeRate": 50
  },
  {
    "exchangeId": 1006,
    "fromCurrency": {
      "currencyId": 1001,
      "value": "DZD",
      "name": "Algerian Dinar"
    },
    "toCurrency": {
      "currencyId": 1000,
      "value": "euro",
      "name": "euro"
    },
    "exchangeRate": 0.02
  }
]
```
#ACCOUNTS
```json
{
    "accountId": 1003,
    "user": {
      "userId": 1000,
      "name": "Branden Gibson",
      "email": "BG@mail.com"
    },
    "currency": {
      "currencyId": 1001,
      "value": "DZD",
      "name": "Algerian Dinar"
    },
    "balance": 201
  },
  {
    "accountId": 1006,
    "user": {
      "userId": 1000,
      "name": "Branden Gibson",
      "email": "BG@mail.com"
    },
    "currency": {
      "currencyId": 1000,
      "value": "euro",
      "name": "euro"
    },
    "balance": 100
  },
  {
    "accountId": 1002,
    "user": {
      "userId": 1000,
      "name": "Branden Gibson",
      "email": "BG@mail.com"
    },
    "currency": {
      "currencyId": 1005,
      "value": "LIRA",
      "name": "lira"
    },
    "balance": 499
  }
```

#TRANSACRIONS
````JSON
{
"transaction_id": 1007,
"fromAccount": {  // should only be present for 'SEND' transactions
"accountId": 1004,
"user": {
"userId": 1001,
"name": "Georgina Hazel",
"email": "GH@mail.com"
},
"currency": {
"currencyId": 1005,
"value": "LIRA",
"name": "lira"
},
"balance": 501
},
"toAccount": {
"accountId": 1004,
"user": {
"userId": 1001,
"name": "Georgina Hazel",
"email": "GH@mail.com"
},
"currency": {
"currencyId": 1005,
"value": "LIRA",
"name": "lira"
},
"balance": 501
},
"trxType": "SEND", // could be SEND, DEPOSIT, WITHDRAWAL
"amount": 1
}
````
