# Car rental application

## Setup

You can change settings in the 
[application.properties](https://github.com/Dadujex/car_rental/blob/master/src/main/resources/application.properties) 
and [application-test.properties](https://github.com/Dadujex/car_rental/blob/master/src/test/resources/application-test.properties) 
files.

### PostgreSQL connection

#### Main
Create a database named car_rental and change username and password to your own.

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/car_rental
spring.datasource.username=username
spring.datasource.password=password
```

#### Test
Create a database named car_rental_test and change username and password
to your own.

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/car_rental_test
spring.datasource.username=username
spring.datasource.password=password
```

### Admin mode

You can log in as admin on the website if you leave the username as admin
and the password as password.
```properties
admin.username=admin
admin.password=password
```

