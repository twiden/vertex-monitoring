# Monitoring with Vertex.io
Foo


# Backend

Running the backend

```
LISTEN_PORT=8000 mvn package exec:exec@run-backend
```

Listing services

```
curl -XGET http://localhost:8000/service
```

Registering a new service.

```
curl -XPOST http://localhost:8000/service -d '{"name": "Pizza Service", "url": "http://localhost:5000/"}'
```

# Service
This is a http server process that should be monitored by the backend application. It takes a listen port as an environment variable so that many services can be run at once.

Running a service

```
LISTEN_PORT=5000 mvn package exec:exec@run-service
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
