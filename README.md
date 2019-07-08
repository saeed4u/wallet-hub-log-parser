# wallet-hub-log-parser
My solution to WalletHub's log parser test

##Build
The project was built with Spring boot using Maven.Please update the `application.properties` file with your DB configurations and run `mvn package` to build a fat jar of the project.

##Run
java -jar /path/to/the/fat/jar --accesslog=/path/to/file --startDate=startdate --duration=duration --threshold=threshold
 
