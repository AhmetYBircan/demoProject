This project is a REST API built with Spring Boot. It includes the following features:

MySQL database integration.
MongoDB for log management.
JWT-based user authentication.
Spring Boot DevTools for automatic restarts during development.


Make sure you have the following software installed:

Java ( compatible version)
Maven 
MySQL
MongoDB
Visual Studio Code
Postman or any API testing tool (optional)

You also need to install the extensions for Java and Spring Boot in Visual Studio Code for better development experience.(Extension Pack for Java, Debbugger for java, Mevaen for Java,Project Manager for Java)

Steps to run the project:

1. Clone the repository
2. Open the project in Visual Studio Code
3. Create a database in MySQL and MongoDB

4. Specify the following properties in the application.properties file:

    spring.application.name
    spring.datasource.driver-class-name
    spring.datasource.url
    spring.datasource.username
    spring.datasource.password

    spring.jpa.show-sql=true
    spring.jpa.hibernate.ddl-auto= update

    security.jwt.secret-key
    security.jwt.expiration-time-ms
    security.jwt.issuer
    spring.data.mongodb.uri
    spring.data.mongodb.database

5. Open the src/main/java/com/example/demo/DemoApplication.java file.
6. Click on the Run button above the main method or right-click and choose Run Java.
Your Spring Boot application should be accessible at http://localhost:8080.



-Make sure that MySQL and MongoDB are running before starting the application.
-If you encounter any connection issues, double-check the credentials and connection strings in application.properties.

