# Test task
## Initial conditions:

1) Redis with stored transaction:
  - key - transaction id
  - value - metadata with json string

Commands to populate Redis:
- `set 123 '{"amount":100.05,"metadata":{"originatorId":1,"destinationId":2}}'`
- `set 124 '{"amount":150.75,"metadata":{"originatorId":10,"destinationId":20}}'`
- `set 125 '{"amount":1010.00,"metadata":{"originatorId":20,"destinationId":30}}'`
- `set 126 '{"amount":15.5,"metadata":{"originatorId":30,"destinationId":40}}'`

2) You receive transactions from Kafka topic. Example of a message in Kafka:
    ```json
    [
        {
          "PID": 123,
          "PAMOUNT": 94.7,
          "PDATA": 20160101120000
        },
        {   
          "PID": 123,
          "PAMOUNT": 94.7,
          "PDATA": 20160101120000
        },
        {   
          "PID": 124,
          "PAMOUNT": 150.75,
          "PDATA": 20160101120001
        },
        {   
          "PID": 125,
          "PAMOUNT": 1020.2,
          "PDATA": 20160101120002
        },
        {   
          "PID": 126,
          "PAMOUNT": 15.5,
          "PDATA": 20160101120003
        },
        {   
          "PID": 127,
          "PAMOUNT": 120.74,
          "PDATA": 20160101120004
        }
    ]
    ```

## Task:
You have to compare the `amount` field of the received transaction against the transaction in Redis. You should query Redis by transaction id.
Comparison has to be sent to another kafka topic, the message should contain enough information to understand the result.

## Requirements:
Take into account, that in future you:

* Might receive transactions not only from Kafka topic
* Might have to support several result message formats
* Might need to send check results via different channels, e.g. make an API call

## Expected result:

* Create a pull request to the `main` branch and assign it to `@wbcfp`
* Source code of Spring Boot 3 application, using Java 17+
* Maven should be able to package code to jar
* Convenient way to check that application works

## Running Kafka

To run Kafka, follow these steps:

1. Download Kafka from the official website: [https://kafka.apache.org/downloads](https://kafka.apache.org/downloads)
2. Extract the downloaded file to a directory of your choice.
3. Navigate to the Kafka directory: `cd kafka_2.x.x`
4. Start the ZooKeeper server: `bin/zookeeper-server-start.sh config/zookeeper.properties`
5. Start the Kafka server: `bin/kafka-server-start.sh config/server.properties`

## Running Redis Commands

To run Redis commands, follow these steps:

1. Download Redis from the official website: [https://redis.io/download](https://redis.io/download)
2. Extract the downloaded file to a directory of your choice.
3. Navigate to the Redis directory: `cd redis-x.x.x`
4. Start the Redis server: `src/redis-server`

## Transaction Controller

- `GET /transactions/{transactionId}`: Get transaction by id from redis
- `POST /transactions/stored`: Insert transactions to redis
- `DELETE /transactions/{transactionId}`: Delete transaction from redis
- `POST /transactions/income`: Publish transactions to transaction topic in kafka

## Result Controller

- `GET /results/{transactionId}`: Get result transaction by transaction id from redis

## Kafka Topics

- `transaction`: KafkaTransactionReceiver listening this topic to pickup and determine result after comparison with data
  in redis.
- `result`: Handler method will publish determined result object as specific format to this topic.

## Explanation of Application Flow

1. Redis and Kafka needs to be up and running.
2. populateScript.txt is checked by TransactionPopulateService on startup and insert transactions to redis.
3. We need to publish transaction to "transaction" topic to let application pick up and decide Result.
4. We can use POST /transactions/income endpoint to publish new transactions. The endpoint supports arrays and single
   objects.
5. KafkaTransactionReceiver will pick up the transactions once topic receive new transactions.
6. TransactionEventHandler class has handler method which will call by receiver.
7. Handler method will call resultService to determine result then will publish to "result" topic and save redis as
   well.
8. Result controller will return result of transaction which called by transactionId as JSON String.
9. Strategy Design Pattern helped to tackle this problem. So the project easily can be expanded.
