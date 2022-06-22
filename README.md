# music-shop
Team E
developing an online platform for a music shop

## Application Backend

### Anwendung lokal starten

Dadurch, dass wir eine Enterprise Java Beans verwenden, haben wir keine Main Klasse, 
um die Applikation auszuführen. Die EJBs der Applikation müssen auf einen Java-EE Server
deployed werden, damit sie von einem Client aufgerufen werden können. Wir haben `Wildfly` als JavaEE
Server verwendet. Mit Gradle kann man eine `.war`-Datei builden, die auf den Wildfly Server deployed 
werden muss. Damit die Applikation voll funktionsfähig ist, muss das `Application-Backend` gemeinsam mit
der `.war`-Datei vom `Application-Customer-Backend` deployed werden.

### Tests ausführen

`./gradlew test`


### SPA aufrufen

`http://localhost:8080/`
