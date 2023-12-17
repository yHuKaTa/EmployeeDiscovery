# Employee Discovery

## Description

`Employee Discovery` is a small web-based employee workflow application using Spring Boot. It registers new employees and describes which projects they are working on. The application provides the following functionalities:

Functionalities for employees data
---

* Register new employee
* View employee data
* Edit employee data
* Fire employee

Functionalities for projects data
---

* Add new project
* View project summary
* Edit project team
* Delete unfinished project

## Launching

* Install PostgreSQL: <a href=https://www.enterprisedb.com/downloads/postgres-postgresql-downloads> Select your platform here </a>
* In pgAdmin4 create new database with name "employee" in your server
* Open "Terminal" or "Command Promt".
* Insert in terminal -> 'project_location'/java -jar employee.jar
* Test the app in following url: <a href=http://localhost:8080/employee> Employee Discovery </a>

## Technologies

* Java 21
* Spring Boot with Spring data JPA and Hibernate
* Maven 3
* PostgreSQL
