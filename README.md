# Drescription

This project consists of an application capable of managing drivers and trips using 
a layered design. In it, we must be able to add drivers, update their data and 
search for them by different criteria (search by identifier or search by city in 
which it operates). In addition, the application will allow users to hire trips 
with these drivers, search for available drivers in their city, rate the trips made, 
consult them and see a list of drivers with whom they have traveled. The management 
is done through a database that we will manipulate with a model layer. In turn, in 
this model layer we have two main sublayers: a data access layer that works directly 
with the database executing the SQL queries for which we will use MySQL, and a 
business logic layer that implements the use cases of our application. We will allow 
our application to be invoked remotely through the network. To implement a service 
layer with a REST implementation. In addition, we will use: ·-Maven: for the 
construction and execution of the software. -Subversion: For the version repository. 
-Eclipse: as a work environment.

# Running the project example
---------------------------------------------------------------------

## Running the ws-app service with Maven/Jetty.

	cd ws-app/ws-app-service
	mvn jetty:run

## Running the client application

- Configure `ws-app/ws-app-client/src/main/resources/ConfigurationParameters.properties`
  for specifying the client project service implementation (Rest or Soap) and 
  the port number of the web server in the endpoint address (9090 for Jetty, 8080
  for Tomcat)
