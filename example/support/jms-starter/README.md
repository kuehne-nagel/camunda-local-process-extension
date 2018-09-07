# TestApp JMS Support

Enables JMS communication between TestApplication-Main and TestApplication-LEX
via activemq (Docker).

To use:

* run `docker-compose up activemq` to start the dockerized activemq broker
* include this lib in both test applications. It acts as a spring-boot-starter, so the configuration is used once the jar is present

There are four defined queues:

* DockerJmsConfiguration.QUEUE_PLATFORM_TO_LEX_PROCESS - to send command startProcess from main to lex
* DockerJmsConfiguration.QUEUE_PLATFORM_TO_LEX_TASK - to send command completeProcess from main to lex
* DockerJmsConfiguration.QUEUE_LEX_to_PLATFORM_PROCESS - to send event processCompleted from lex to main
* DockerJmsConfiguration.QUEUE_LEX_to_PLATFORM_TASK - to send event taskCreated from lex to main
