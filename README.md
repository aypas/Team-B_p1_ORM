# Team-B_p1_ORM

## Packaging and Installation
 
  ### Requirements
 
  In order to install you will need maven on your machine
  
  ### JAR Packaging

   Open the directory containing this project and type  
   ```mvn clean package```

  ### M.2 Installation
  
   Open the directory containing this project and type  
   ```mvn clean install```
  

## Demonstration Program
  [Web Application](https://github.com/210426-java-react-enterprise/Team-B_p1_WebApp)

## Overview

  This application will take advantage of Java's reflection library along with annotations to build prepared statements for persisting
  to PostgreSQL database.

  There are two main objects within...

## PostgresQueryBuilder

  This object will require a Connection object to be instantiated.

  Once instantiated it has a few public facing methods based on query types that only require a POJO to be passed to it...

### PostgresQueryBuilder Functions

  * insert (This will take in a POJO with a blank ID and persist it assuming the database will generate an ID)
  * insertWithPK (This will take in a POJO with an existing ID and persist it to the database with the given ID)
  * insertAndGetId (Much like the insert method but will return the databases generated ID upon completion)
  * update (Will take a POJO with an ID and update the existing persistent entry based on the changes)
  * delete (Will take a POJO with an ID and delete the database entry by ID)
  * selectByPrimaryKey (Will take in an uncomplete POJO only containing and ID and return a Map for use with the GenericObjectMaker)
  * loginByUsername (For used with authentication based systems, will check the databse for a username and password combination and 
                     return a map for the GenericObjectMaker to create a user POJO as defined by the parent application)
  * loginByEmail (Same as above but instead of username it will utilize an email address)
  * loginByUsernamePgCrypt (Much like the above but utilizes PostgreSQL's pgCrypt library for more secure authentication using passwords)
  * loginByEmailPgCrypt (Much like the above but utilizes PostgreSQL's pgCrypt library for more secure authentication using passwords)
  * getUsername (Will take in an uncomplete POJO only containing and ID and return an associated username)
  * getEmail (Will take in an uncomplete POJO only containing and ID and return an associated email)

## GenericObjectMaker

  This object requires no arguments to make and will build a POJO based on a list of one or more maps.

### GenericObjectMaker Functions

  * buildObject (Takes in a map and will return the first object contained as a fully completed POJO)
  * buildObjects (Takes in a map and will return a list of all objects contained as fully completed POJOs)

## Annotations
  
  As mentioned before this ORM relies on annotations, so here is a list of those included.

### Annotation List
  
  * Entity (Type level annotaion that let's the ORM know this POJO is worth persisting)
  * Table (Type level annotaion that takes an argument of "table_name" which will tell the ORM what table the POJO belongs too)
  * Primary (Field level annotation takes an argument of "name" which will tell the ORM what the column name the POJO's primary  
             key belongs too)
  * Foreign (Field level annotation takes an argument of "name" which will tell the ORM what the column name the POJO's foreign  
             key belongs too)
  * Column (Field level annotation takes an argument of "name" which will tell the ORM what the column name the POJO's annotated  
            field belongs too)
  * Username (Field level annotation takes an argument of "name" which will tell the ORM what the column name the POJO's username  
              field belongs too)
  * Email (Field level annotation takes an argument of "name" which will tell the ORM what the column name the POJO's email field  
           belongs too)
  * Password (Field level annotation takes an argument of "name" which will tell the ORM what the column name the POJO's password  
              field belongs too)
