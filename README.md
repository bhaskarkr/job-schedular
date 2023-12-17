# jobscheduler
# HBase-based Job Scheduler for Task Execution

## Overview

The HBase-based Job Scheduler is a robust and scalable job scheduling system designed to efficiently manage and execute tasks for various clients, including prominent organizations like Swiggy. This project leverages the power of HBase to perform time range queries, allowing precise task execution at specific times and accommodating time-sensitive use cases, such as payment loading pages and links with time-limited validity.

## Key Features

- **HBase Integration**: The core of the system relies on HBase to perform time range queries efficiently. This enables the scheduler to determine which tasks need to be executed at a particular time.

- **Support for Time-Sensitive Tasks**: Time-sensitive tasks, as seen in scenarios like payment loading pages, are handled seamlessly. The system allows clients to schedule API calls to execute tasks at precise times, ensuring optimal user experience.

- **Configurable Workers**: Each client has a configurable set of workers. Currently, three types of workers are supported:
    - **Real-Time Worker**: Queries HBase every second to promptly execute tasks.
    - **Hourly Worker**: Checks for any pending tasks every hour, ensuring no tasks are left incomplete due to unforeseen circumstances.
    - **Daily Worker**: Runs once every 24 hours to handle periodic tasks and maintenance.

- **Client-Specific Configurations**: The system provides flexibility by allowing client-specific configurations. Clients can fine-tune the scheduling and execution of tasks according to their unique business requirements.

- **API Call Support**: The system offers seamless integration with external services through API calls. Clients can schedule API calls as tasks, ensuring data synchronization and interaction with external systems.

## Getting Started

To test the features of this project, you can use a mock API service such as [Mocki](https://mocki.io/fake-json-api) to generate a dummy API with a mock response. This allows you to simulate task execution and API interaction within the system.

## Usage Scenarios

- **E-commerce**: Schedule inventory updates, price changes, and order processing tasks at specific times to optimize customer experience.

- **Financial Services**: Manage time-critical tasks like payment processing, transaction reconciliation, and account updates with precision.

- **Content Delivery**: Schedule content publishing, caching updates, and CDN purges at optimal times for seamless content delivery.

- **Healthcare**: Ensure timely data synchronization, patient record updates, and appointment reminders for healthcare providers.

## Contributions Welcome

This open-source project welcomes contributions from the developer community. Feel free to explore the codebase, propose enhancements, and contribute to the project's growth.

The HBase-based Job Scheduler empowers businesses to streamline task execution, enhance user experiences, and optimize their operations through efficient time-based scheduling and task management.



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
```
brew install rabbitmq
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

