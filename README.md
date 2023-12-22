# Employee Discovery

## Description

`Employee Discovery` is a small web API employee workflow application using Spring Boot. It registers new employees and describes for which projects they are working on with timestamps. The application provides the following functionalities:

Functionalities for employees data
---

* Register new employee
* View employee data
* View two top teamwork employees
* Edit employee data
* Fire employee

Functionalities for projects data
---

* Add new project
* View project summary
* View all projects
* Edit project's end date
* Delete project

Functionalities for jobs data
---

* Add new job for employee
* View job summary
* View all jobs for employee
* View all jobs for project
* View job's of two top teamwork employees
* Edit job's description
* Delete job
* 

## Launching

* Install PostgreSQL: <a href=https://www.enterprisedb.com/downloads/postgres-postgresql-downloads> Select your platform here </a>
* In pgAdmin4 create new database with name "employee" in your server
* Open "Terminal" or "Command Promt".
* Insert in terminal -> 'project_location'/java -jar employee.jar
* Test the app in Postman with following requests:

## Requests

### Employee Layer

#### Find employee data by ID
* GET -> http://localhost:8080/employees/{id}

#### Get employee by ID
* GET -> http://localhost:8080/employees/get

```
    Required Header: passportId = {passportID}
```

#### Get all employees data
* GET -> http://localhost:8080/employees

#### Add new employee
* POST -> http://localhost:8080/employees

```
    Required Body:
    {
        "firstName" : "FirstName",
        "lastName" : "LastName",
        "passportId" : "000000000"
    }
```

#### Fire employee by ID and passport ID
* POST -> http://localhost:8080/employees/{id}/fire

```
    Required Body:
    {
        "id" : "2525",
        "passportId" : "000000000"
    }
```

#### Delete employee by ID
* DELETE -> http://localhost:8080/employees/{id}

#### Edit employee by passport ID
* PUT -> http://localhost:8080/employees
    
```
    Required Header: passportId = {passportId}

    Required Body:
    {
        "firstName" : "FirstName",
        "lastName" : "LastName"
    }
```

### Project Layer

#### Find project data by ID
* GET -> http://localhost:8080/projects/{id}

#### Get project by ID
* GET -> http://localhost:8080/projects/get

```
    *Required Header: passportId = {passportID}
```

#### Find all projects data
* GET -> http://localhost:8080/projects

#### Add new project
* POST -> http://localhost:8080/projects

```
    Required Body:
    {
        "projectName" : "EmployeeDiscovery",
        "startDate" : "2023-12-16",
        "endDate" : "2023-12-23"
    }
```

#### Delete project by ID
* DELETE -> http://localhost:8080/projects/{id}

#### Edit project end date by employee's passport ID
* PUT -> http://localhost:8080/projects

```
    Required Header: passportId = {passportId}

    Required Body:
    {
        "endDate" : "2024-01-04"
    }
```

### Job Layer

#### Find job data by ID
* GET -> http://localhost:8080/jobs/{id}

#### Find employee's jobs by employee ID
* GET -> http://localhost:8080/jobs/employee/{id}

#### Find project's jobs by project ID
* GET -> http://localhost:8080/jobs/project/{id}

#### Find two top teamwork employees data
* GET -> http://localhost:8080/topPair

#### Find jobs data of two top teamwork employees
* GET -> http://localhost:8080/topPair/jobs

#### Add new job to employee
* POST -> http://localhost:8080/jobs

```
    Required Header: passportId = {pasportId}

    Required Body:
    {
        "description" : "Full text here...",
        "employeeId" : "2525",
        "projectId" : "203",
        "startDate" : "2023-12-16",
        "endDate"   : "null"
    }
```

#### Delete job by ID
* DELETE -> http://localhost:8080/jobs/{id}

#### Edit job's description by ID
* PUT -> http://localhost:8080/jobs/{id}

```
    Required Body:
    {
        "description" : "New text here!"
    }
```

## Technologies

* Java 21
* Spring Boot with Spring data JPA and Hibernate
* Maven 3
* PostgreSQL
