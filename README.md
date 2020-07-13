# Tolar Gateway

A Spring Boot server acting as a proxy for connecting with Tolar HashNET
Transforms JSON-RPC calls (incoming) to gRPC calls (HashNET) and return JSON-RPC responses from gRPC responses

# Usage

To build a image, use:

```
mvn clean spring-boot:build-image
```

This will produce a docker image with the tag: `dreamfactoryhr/tolar-gateway:latest`

To run the image:

```
docker run -d --network=host --name tolar-gateway dreamfactoryhr/tolar-gateway:latest
```

`--network=host` is an optional parameter, you can change this to fit you needs, see below on how to configure...

`--name tolar-gateway` is also optional, this is the name of the docker container that will run the image

# Configuration:

## HashNET Networking

You can specify:
* Hosts (1 or more Tolar HashNET node, defaults to 127.0.0.1)
* Port (the port used to connect to Tolar HashNET nodes, defaults to 9200)

To change the hosts used for connecting to the HashNET, set the environment variable **TOLAR_HASHNET_HOSTS** when you run the docker image...

You can set only 1 host: 
` -e "TOLAR_HASHNET_HOSTS=127.0.0.1" `
or use multiple hosts, separated with commas:
` -e "TOLAR_HASHNET_HOSTS=127.0.0.1,127.0.0.5" `

To change the port, the environment variable **TOLAR_HASHNET_HOSTS**:
` -e "TOLAR_HASHNET_PORT=9300" `

## Service Networking

You can choose the networking mode of the docker image (service): 

`--network=host` -> uses host networking
`-p 8083:8080` -> exposes the docker port 8080 (default for the service) and maps it to 8083 of the host 
`-e "--server.port=8083"` -> changes the default server port to another one (8083 in this case), useful when using host networking

## Semaphore tuning

The proxy service (tolar gateway) uses semaphores to control the number of requests made on a HashNET node

By default, it's set to 10, but you can easily increase this by setting the environment variable **TOLAR_SEMAPHORE_PERMITS**:
` -e "TOLAR_SEMAPHORE_PERMITS=100" `

Also, you can change the semaphore timeout (request timeout) by setting the environment variable **TOLAR_SEMAPHORE_TIMEOUT**:
` -e "TOLAR_SEMAPHORE_TIMEOUT=100" `

The timeout is in seconds and the default is 60 seconds.

## Full configuration example

These are all arbitrary values, just to show how a customized configuration looks like!

```
docker run -d -e "TOLAR_HASHNET_PORT=9300" -e "TOLAR_HASHNET_HOSTS=127.0.0.1,127.0.0.5" -e "TOLAR_SEMAPHORE_PERMITS=100" -e "TOLAR_SEMAPHORE_TIMEOUT=100" -e "--server.port=8083" --network=host --name tolar-gateway dreamfactoryhr/tolar-gateway:latest
```