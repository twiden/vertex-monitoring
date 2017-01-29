# Monitoring with Vertx.io

### Building the project

Requires Java 1.8

```
mvn package
```

### Running tests

```
mvn test
```

## Example usage

Start the backend

```
LISTEN_PORT=8000 mvn exec:exec@run-backend
```

Start the supervisor

```
SLEEP_SECONDS=5 BACKEND_URL="http://localhost:8000/service/" mvn exec:exec@run-supervisor
```

Start a service

```
LISTEN_PORT=5000 mvn exec:exec@run-service
```

Register the service in the backend

```
curl -XPOST http://localhost:8000/service -d '{"name": "Pizza Service", "url": "http://localhost:5000/"}'

CREATED eee7856e-fcd6-4199-bf53-9d4abfa90e75
```

Wait 5 seconds and check that it is monitored

```
curl -XGET http://localhost:8000/service

{
  "services" : [ {
    "id" : "eee7856e-fcd6-4199-bf53-9d4abfa90e75",
    "name" : "Pizza Service",
    "status" : "OK",
    "url" : "http://localhost:5000/",
    "lastCheck" : "2017-01-21 11:43"
  } ]
}
```

Make the service unresponsive

```
curl -XPUT http://localhost:5000/down

OK, Down!
```

Wait 5 seconds and check that its status has changed

```
curl -XGET http://localhost:8000/service

{
  "services" : [ {
    "id" : "eee7856e-fcd6-4199-bf53-9d4abfa90e75",
    "name" : "Pizza Service",
    "status" : "FAIL",
    "url" : "http://localhost:5000/",
    "lastCheck" : "2017-01-21 11:45"
  } ]
}

```

Make the service responsive again

```
curl -XPUT http://localhost:5000/up

OK, Up!
```

Delete the service
```
curl -XDELETE "http://localhost:8000/service/eee7856e-fcd6-4199-bf53-9d4abfa90e75"

DELETED eee7856e-fcd6-4199-bf53-9d4abfa90e75
```

Check that it is gone
```
curl -XGET http://localhost:8000/service

{
  "services" : [ ]
}
```

# REST Backend

Running the backend

```
LISTEN_PORT=8000 mvn exec:exec@run-backend
```

Listing services

```
curl -XGET http://localhost:8000/service
```

Registering a new service.

```
curl -XPOST http://localhost:8000/service -d '{"name": "Pizza Service", "url": "http://localhost:5000/"}'
```

Deleting a service (with id 07a9953d-6604-4968-8bd1-df33a075980a)

```
curl -XDELETE "http://localhost:8000/service/07a9953d-6604-4968-8bd1-df33a075980a"
```

Marking a service as up or down. This endpoint is used by the supervisor.

```
curl -XPATCH http://localhost:8000/service/07a9953d-6604-4968-8bd1-df33a075980a -d '{"status": "OK", "timestamp": "1914-06-24 16:42"}'
curl -XPATCH http://localhost:8000/service/07a9953d-6604-4968-8bd1-df33a075980a -d '{"status": "FAIL", "timestamp": "1914-06-24 16:42"}'
```

# Monitored services
Http server process that should be monitored by the backend application. It takes a listen port as an environment variable so that many services can be run at once.

Running a service

```
LISTEN_PORT=5000 mvn exec:exec@run-service
```

To call the service and get a 200 response you make a GET request to the root url. It should reply OK if it is up and running. If the service is down it will simply close the TCP connection.

```
curl -XGET http://localhost:5000/
```

To simulate an outage

```
curl -XPUT http://localhost:5000/down
```

To simulate an outage recovery

```
curl -XPUT http://localhost:5000/up
```

# Supervisor

Running the supervisor (Backend url must end with a /)

```
SLEEP_SECONDS=5 BACKEND_URL="http://localhost:8000/service/" mvn exec:exec@run-supervisor
```
