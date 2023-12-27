# WADA - Web Application Delegate Administration
## Description
It's an web portal, that allows you to manage users stored in the Postgres DB and the Qlik FARMs data stored in the Oracle DB. 
### What it does? 
- Single creation of an user;
- Massive creation (upload) via well-defined CSV file. How? The code processes each line and looks for each userId (employeeId) in the MS ActiveDirectory, if the data are present for the userId then it is going to be stored on DataBase;
- User management (CRUD);
- QLIK Farm management (CRUD);
- PDF file documentation is available to see within the portal;
- Different access mode (MODER or ADMIN).
### Technologies used:
- Springboot MVC
- JSP
- DataTable API
- Bootstrap 4
- Java 7/8
