# Settlers of Catan
This is an educational implementation of [Settlers of Catan](http://www.catan.com/) for [BYU CS 340 - Software Design and Testing](https://students.cs.byu.edu/~cs340ta/winter2016/). This project is completed in a 5-student team, with multiple project phases such as design (UML diagrams), model implementation/testing, client, server, etc.

### Technology
The project is built in [Java 8](http://www.oracle.com/technetwork/java/javase/overview/java8-2100321.html) - the client uses [Java Swing](https://docs.oracle.com/javase/tutorial/uiswing/start/index.html) and the server is built using the Java standard library. The only exception are some [Python](https://www.python.org/) integration tests. The [Ant](http://ant.apache.org/) build system is used for task automation.

### Libraries
This project makes use of the following open-source software:
- [Spark framework](http://sparkjava.com/)
- [Gson](https://github.com/google/gson)
- [Apache Commons IO](https://commons.apache.org/proper/commons-io/)
- [Requests: HTTP for Humans](http://docs.python-requests.org/en/master/)

### Usage
- Run the demo server using `ant server`
- Run the demo client using `ant client`
- Run the student-built client using `ant our-client`
- Run the student-built server using `ant our-server`

### Documentation
With the server running, navigate to localhost:8081/ to view the JavaDocs and the Swagger API.
