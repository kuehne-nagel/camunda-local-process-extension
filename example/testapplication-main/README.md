# BPA Platform Testapplication for LEX

This application does not use the bpa-platform.
It is just a completely separated camunda application using the camunda-spring-boot-starter.
The database used is an h2 in memory db only.

It communicates with the main testapplication via jms messages.

To ease message/java conversion, we share the bpa-model, but besides that, no platform components are used.

To run it, type:
 
  `mvn spring-boot:run 
  
or execute the Java class `BpaLexApplication` from within your IDE.

This runs a tomcat with the BPA Testapplication deployed on port 10080.


### Links
To create/see a process go to:

* [http://localhost:10080/swagger-ui.html](http://localhost:10080/swagger-ui.html)

Activemq monitoring

* http://localhost:8161/admin/ (admin/admin)

Lex Camunda Cockpit

* http://localhost:10080/ 
