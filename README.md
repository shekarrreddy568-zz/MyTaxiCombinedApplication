# Data Infrastructure Engineering Test

We as a data engineering team want to gather data from various sources in our company, enrich the collected data and deliver it to our stakeholders.
One of our backend teams developed a new microservice for payment transactions and we would like to utilize the data for analysis.

The microservice publishes a new message into a kafka topic for every completed payment done through the mytaxi app.


In order to make this data available we need to consume the message and store it in one of our databases. Please use the given docker setup to launch your environment and work on the tasks.

## Tasks
For any of the following tasks please use git to track the status and progress of your development.
You may use any programming language for your new service. Feel free to pick whatever you feel comfortable with.
The paymentservice project will be build during the `docker build`. Make sure code changes are properly reflected, when restarting your docker container.

### 1. Consume and Print
 As initial task we would like to consume the payment events from kafka and simply print them to the log. 
 
 Please create a new project and add it to the `docker-compose.yml`. 

### 2. Store Events
 We would like to store the payment events consumed in a database for future analysis. Our most important use-cases for payment related data is fraud prevention and revenue forecasting.

 Choose a database and reason, why you decided to go for this database. Setup the database within the `docker-compose.yml`.
 
 Extend the project created in task 1. with a database module and write all incoming events to the new database.

### 3. Utilize Avro
 After working on this project for some time, we realised that sending raw json messages through the pipelines is not the most efficient way and would like to switch to a more performant format like Avro.

 Please refactor the paymentservice to publish messages as avro instead of json into the payment queue and update your project from task 1. to read these messages as avro.
