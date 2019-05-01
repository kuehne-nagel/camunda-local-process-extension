# Camunda local process extension

## How to start a lex process?
0) Build the project first `mvn clean install` on project root-level

1) Start activemq - `docker-compose up activemq` and verify, that [activemq](http://localhost:8161/admin/) was started and login via `admin`/`admin`

2) Start lex server - `mvn spring-boot:run -f example/testapplication-main`

3) Start lex client - `mvn spring-boot:run -f example/testapplication-lex`

4) GOTO [Swagger](http://localhost:9080/swagger-ui.html#!/lex-server-controller/startLexProcessUsingGET_1)
of the global application to generate a proxy task

5) GOTO [Swagger](http://localhost:9080/lex-tasktype-command/createLexTaskType?taskTypeId=ArrangeCustomsDE&taskTypeName=Arrange%20Customs%20for%20DE)
of the global application to start a global business process 

6) Use "DEKN_DEHAM_AE" as office and your own defined (unique) businessId to start the main process and trigger the LEX-bridge. 
If you use another office, no LEX-process will be started. 
For further details, please refer to the implementation `LexApplicationGateway` of the gateway within the global process.

## And now?

Both spring boot applications does also start a camunda cockpit:

[Global camunda cockpit](http://localhost:9080/) (admin/admin)

[Lex camunda cockpit](http://localhost:10080/) (admin/admin)

On the global-application you can see the lex bridge and the lex proxy task for the lex process (LEX_ArrangeCustomsDE).

On the lex-application is the lex process (Lex Demo process for AE0100_LexBridge) which has already triggered the lex proxy task on global side.

You can now use the tasklist on lex server to work on the lex proxy task. After completing it, the lex bridge and the lex process should also get finished.

## Enable history

To see the history you have to use the enterprise version of camunda:

1) Edit pom files:

Instead of `camunda-bpm-spring-boot-starter-webapp` use `camunda-bpm-spring-boot-starter-webapp-ee`

Set `camunda.version` to `7.10.5-ee`

Add this to dependency management of root pom: 

```
<dependency>
  <!-- Import dependency management from camunda to force usage of camunda ee -->
  <groupId>org.camunda.bpm</groupId>
  <artifactId>camunda-bom</artifactId>
  <version>${camunda.version}</version>
  <scope>import</scope>
  <type>pom</type>
</dependency>
```

2) Add license file to classpath:

Place a `camunda-license.txt` (with the license key in it) to resources folder of both testapps.

## Links

Activemq monitoring

* http://localhost:8161/admin/ (admin/admin)

## Maintainer

* [Stefan Becke](https://github.com/stefanbecke) - [Stefan.Becke@kuehne-nagel.com](Stefan.Becke@kuehne-nagel.com) - [Kühne + Nagel](https://home.kuehne-nagel.com/)
