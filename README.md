# Spring Boot 3 Webflux OAuth2 Client + Keycloak Authorization Server

This project is an example of Spring Boot Webflux OAuth2 client server that works with Keycloak as authorization server.

## How to use this project?

### Install Keycloak
To install keycloak you can install it form `kafka-docker-compose.yml` file by executing this command :
```
docker-compose -f kafka-docker-compose.yml up -d
```
If the installation succeed, you will get 2 container in your docker like below
```
$ docker ps
CONTAINER ID   IMAGE                              COMMAND                  CREATED       STATUS          PORTS                                                   NAMES
7200bad0747c   quay.io/keycloak/keycloak:latest   "/opt/keycloak/bin/k…"   5 weeks ago   Up 14 minutes   8443/tcp, 0.0.0.0:28080->8080/tcp, :::28080->8080/tcp   local-keycloak
b9dfc5ce2638   postgres:12                        "docker-entrypoint.s…"   5 weeks ago   Up 14 minutes   0.0.0.0:5444->5432/tcp, :::5444->5432/tcp               postgres-keycloak
```

You can change your Keycloak admin password, PostgreSQL user and password through `kafka-docker-compose.yml` file.

### Setup Keycloak realm, client, and users

You can follow Keycloak documentation for this case

### Setup Spring Boot properties (`application.yml` file)

Here is the `application.yml` file that we will use in this project :
```yaml
server:
  port: 9091
spring:
  main:
    allow-bean-definition-overriding: true
  security:
    oauth2:
      client:
        registration:
          keycloak:
            client-id: ${CLIENT_ID}
            client-secret: ${CLIENT_SECRET}
            authorization-grant-type: authorization_code
            scope:
              - openid
        provider:
          keycloak:
            authorization-uri: http://localhost:28080/realms/${REALM_NAME}/protocol/openid-connect/auth
            token-uri: http://localhost:28080/realms/${REALM_NAME}/protocol/openid-connect/token
            user-info-uri: http://localhost:28080/realms/${REALM_NAME}/protocol/openid-connect/userinfo
            jwk-set-uri: http://localhost:28080/realms/${REALM_NAME}/protocol/openid-connect/certs
            issuer-uri: http://localhost:28080/realms/${REALM_NAME}
            user-name-attribute: preferred_username
      resourceserver:
        jwt:
          jwk-set-uri: http://localhost:28080/realms/${REALM_NAME}/protocol/openid-connect/certs
logging:
  level:
    org.springframework.security: DEBUG
    org.springframework.web: DEBUG
    org.springframework.boot.autoconfigure.security: DEBUG
    org.springframework.boot.autoconfigure.security.oauth2: DEBUG

keycloak-admin:
  server-url: http://localhost:28080
  realm: ${REALM_NAME}
  admin-client-id: ${ADMIN_CLIENT_ID}
  admin-client-secret: ${ADMIN_CLIENT_SECRET}
  client-id: ${CLIENT_ID}
  client-secret: ${CLIENT_SECRET}
```

You need to set the value for :
- `CLIENT_ID`
- `CLIENT_SECRET`
- `REALM_NAME`
- `ADMIN_CLIENT_ID`
- `ADMIN_CLIENT_SECRET`

You can add the value to your system environment or just directly change code.