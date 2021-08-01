# TransactionStatistics

Run Application:

>gradlew bootRun

### Endpoints

````
POST /transactions : adds a new transaction

Request body: Transaction object as follows,
{
    "amount": "3009.345",
    "timestamp": "2021-08-01T14:37:50.431563200Z"
}

Response:
Empty body with one of the following:
201 – in case of success
204 – if the transaction is older than 60 seconds
400 – if the JSON is invalid
422 – if any of the fields are not parsable or the transaction date is in the future
````

````
DELETE /transactions : deletes all transactions

No request body
Response:
No Content
204 – deleted successfully
````

````
GET /statistics : Returns the statistic based on the transactions which happened in the last 60 seconds.

No request body
Response:
200 – in case of success
204 – No content, if there are no transaction older than 60 seconds
````
### Design

#### Services
- Transaction Service: Used to deal with transactions happening around.
- Statistics Service: Returns statistics of transaction that occurred in last 60 seconds. It calls transaction service to fetch the transaction data.

#### Controllers
-  Transaction Controller
-  Statistics Controller

#### Concurrency

- Used ReentrantReadWriteLock that is an implementation of the ReadWriteLock interface which provides a pair of read-write lock
- It is designed as a high-level locking mechanism that allows you to add thread-safety feature to a data structure while increasing throughput
- surrounded the critical section with the write lock, so that only one thread can get access to it

### Unit testing

- Unit tested services using Mock.
- Unit tested controllers using TestRestTemplate.