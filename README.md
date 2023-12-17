# jobscheduler

How to start the jobscheduler application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/jobscheduler-1.0-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

## Setup
Download dropwizard RabbitMQ actor for 4.x.x
```
https://github.com/bhaskarkr/dropwizard-db-sharding-bundle/tree/dropwizard-4xx
```

## HBase
#### CREATE
```
create 'clients', 'meta'
create 'jobs', 'meta', 'tasks'
create 'tasks', 'meta'
```
#### SCAN / SEARCH
```
scan 'tasks', {STARTROW => 'task9:001701510915564', ENDROW => 'task9:001703512715534'}
scan 'tasks'
```
#### CLEAR TABLE
```
truncate 'tasks'
```

## APIs
#### Add client
```
curl --location 'http://localhost:5050/v1/clients' \
--header 'Content-Type: application/json' \
--data '{
    "clientId" : "test00"
}'
```
#### Get Clients
```
curl --location 'http://localhost:5050/v1/clients' \
--header 'Accept: application/json'
```

#### Schedule Job - ONCE
```
curl --location 'http://localhost:5050/v1/jobs' \
--header 'Content-Type: application/json' \
--data '{
    "clientId" : "test00",
    "url": "https://mocki.io/v1/16bfdb53-ceed-4fe8-9fda-f6eb89741a7b",
    "callType" : "GET",
    "headers" : {
        "Content-Type" : "application/json"
    },
    "scheduleRequest" : {
        "scheduleType" : "ONCE",
        "executeAt" : 1702850885134
    }
}
'
```
#### Schedule Job - REPEAT
```
curl --location 'http://localhost:5050/v1/jobs' \
--header 'Content-Type: application/json' \
--data '{
    "clientId" : "test8",
    "url": "abc.com",
    "callType" : "POST",
    
    "scheduleRequest" : {
        "scheduleType" : "REPEAT",
        "intervalUnit" : "SECONDS",
        "interval" : 2,
        "startExecutionDate" : 1702494688000,
        "endExecutionDate" : 1702498288000
    }
}
'
```
#### Schedule Job - RECURRING
```
curl --location 'http://localhost:5050/v1/jobs' \
--header 'Content-Type: application/json' \
--data '{
    "clientId" : "test6",
    "url": "abc.com",
    "callType" : "POST",
    
    "scheduleRequest" : {
        "scheduleType" : "RECURRING",
        "executionDates" : [1702450245000]
    }
}
'
```

Health Check
---
To see your applications health enter url `http://localhost:8081/healthcheck`

