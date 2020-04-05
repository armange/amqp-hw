
# amqp - Advanced Message Queue Protocol

### Example project for exchanging messages between services through middleware.

[![](https://mermaid.ink/img/eyJjb2RlIjoiZ3JhcGggVERcblx0QVtTZXJ2aWNlIEFdIC0tPnxNZXNzYWdlIHRvICdCJ3wgQltNaWRkbGV3YXJlIGZhOmZhLXJhYmJpdF1cblx0QiAtLT58TWVzc2FnZSBmcm9tICdBJ3wgQ1tTZXJ2aWNlIEJdXG5cdERbU2VydmljZSBEXSAtLT58TWVzc2FnZSB0byAnQyd8IEIgXG4gIEIgLS0-fE1lc3NhZ2UgZnJvbSAnRCd8IEVbU2VydmljZSBDXSBcblx0XHRcdFx0XHQiLCJtZXJtYWlkIjp7InRoZW1lIjoiZGVmYXVsdCJ9LCJ1cGRhdGVFZGl0b3IiOmZhbHNlfQ)](https://mermaid-js.github.io/mermaid-live-editor/#/edit/eyJjb2RlIjoiZ3JhcGggVERcblx0QVtTZXJ2aWNlIEFdIC0tPnxNZXNzYWdlIHRvICdCJ3wgQltNaWRkbGV3YXJlIGZhOmZhLXJhYmJpdF1cblx0QiAtLT58TWVzc2FnZSBmcm9tICdBJ3wgQ1tTZXJ2aWNlIEJdXG5cdERbU2VydmljZSBEXSAtLT58TWVzc2FnZSB0byAnQyd8IEIgXG4gIEIgLS0-fE1lc3NhZ2UgZnJvbSAnRCd8IEVbU2VydmljZSBDXSBcblx0XHRcdFx0XHQiLCJtZXJtYWlkIjp7InRoZW1lIjoiZGVmYXVsdCJ9LCJ1cGRhdGVFZGl0b3IiOmZhbHNlfQ)

### How-to

1. Start middleware to handle message exchanges.

```shell
docker run -it --rm --name middleware -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```

2. Start one or more services to exchange messages with each other.

```shell
docker run -it --rm --name my-service -e "BR_COM_AMQP_HW_MIDDLEWARE_HOST=<middleware-ip>" -p 4321:4321 diegoarmangecosta/amqp-hw-service-one:1.0.0-SNAPSHOT
```

2.1 **Optionally**, a service can be started with a continuous sending message thread.

A new message will be sent to the middleware each five seconds.

```shell
docker run -it --rm --name repeater -e "BR_COM_AMQP_HW_MIDDLEWARE_HOST=<middleware-ip>" -e "BR_COM_AMQP_HW_START_CONTINUOUS_MESSAGE_SENDING=true" -p 4321:4321 diegoarmangecosta/amqp-hw-service-one:1.0.0-SNAPSHOT
```

3. **Optionally**, start the frontend to test the sending and receiving messages(over port 4321).
```shell
docker run -it --rm --name frontend diegoarmangecosta/amqp-hw-mq-tester:1.0.0-SNAPSHOT
```
