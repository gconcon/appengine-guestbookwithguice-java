App Engine Java Guestbook with Guice and Jersey RESTful framework

## Google's Guestbook AppEngine sample app converted to a RESTful app using Guice and Jersey frameworks
### Also removed all scriplets on guestbook.jsp using JSTL

My intention here is to provide a starting app for who wants to use Guice to build appengine applications.

In my oppinion, Guice is recommended because it is lightweight Dependency Injection framework for context initialization for 
faster loading times on AppEngine (Compared with Spring)


-
Requires [Apache Maven](http://maven.apache.org) 3.1 or greater, and JDK 7+ in order to run.

To build, run

    mvn package

Building will run the tests, but to explicitly run tests you can use the test target

    mvn test

To start the app, use the [App Engine Maven Plugin](http://code.google.com/p/appengine-maven-plugin/) that is already included in this demo.  Just run the command.

    mvn appengine:devserver

For further information, consult the [Java App Engine](https://developers.google.com/appengine/docs/java/overview) documentation.

To see all the available goals for the App Engine plugin, run

    mvn help:describe -Dplugin=appengine