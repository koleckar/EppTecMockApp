# EppTecMockApp

Console Java SpringBoot application with rest api using in-memory h2 database.
---------------------------------------------------------------------------------

- Console input offer 3 commands:'add', 'get', 'del' and 'help'.  
- REST api: at "/customers" offering GET, POST, DELETE.  
<div align="Left">
    <img src="/consoleGUI.png" width="500px" height="500px"</img> 
</div>

---------------------------------------------------------------------------------
Console api:

'get' expects nationalID in format yymmdd/xxxx or yymmddxxxx  
'del' expects nationalID in format yymmdd/xxxx or yymmddxxxx  
'add' expects name, surname and nationalID in format yymmdd/xxxx or yymmddxxxx  

---------------------------------------------------------------------------------
REST api:  

GET expects nationalID in format yymmdd/xxxx or yymmddxxxx.  
  Returns customer if found with calculated age from the date of birth and actual time and success/failiure status:  
 ```json
200 OK  
{  
    "customer": {  
        "name": "Pavel",  
        "surname": "Novak",  
        "nationalID": "9101060010"  
    },  
    "age": 32  
}  
```

POST expects following fields. 
```json
{  
    "name" : "Pavel",  
    "surname": "Novak",  
    "nationalID": "910106/0010"  
}  
```

  Returns eithers success/failiure msg and status.  
 
 DELETE expects nationalID in format yymmdd/xxxx or yymmddxxxx.  
 
 ```json
 Successfully deleted Customer[name='Pavel', surname='Novak', nationalID=9101060010]  
  ```    
  Returns eithers success/failiure message with deleted customer credentials and status.  

---------------------------------------------------------------------------------

tomcat server running on localhost:8080  

H2 console at http://localhost:8080/h2-console  
Driver Class: org.h2.Driver  
JDBC URL: jdbc:h2:mem:requestsDB  
User Name: dk  
Password: password  

---------------------------------------------------------------------------------

issues/TODOs:  
- unit and integration tests not finished  
- age calculation span only 100 years  
- not handling invalid months and days also not handling czech female nationalID format (mm + 50)  
