# EppTecMockApp

Console Java SpringBoot application with rest api using in-memory h2 db


Console input has 3 options 'add', 'get', 'del'. 
REST api: at "/customers" GET, POST, DELETE

Following holds for both console and rest apis.
get/Get() expects nationalID in format yymmdd/xxxx or yymmddxxxx
del/Delete() expects nationalID in format yymmdd/xxxx or yymmddxxxx
add/Post() expects name, surname and nationalID in format yymmdd/xxxx or yymmddxxxx


tomcat server running on localhost:8080

H2 console at http://localhost:8080/h2-console
Driver Class: org.h2.Driver
JDBC URL: jdbc:h2:mem:requestsDB
User Name: dk
Password: password


issues/TODOs:
test not finished
