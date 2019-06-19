To run Spring Boot server:
	mvn package && java -jar target/gs-spring-boot-0.1.0.jar

Primary key is composite (id, city, date), so can't uniquely identify resource by id alone.
Case sensitive [city] and [temp].

API documentation at http://localhost:8080/swagger-ui.html#/weather-controller