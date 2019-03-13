# bookstore

This GitHub repo serves as a template to start your web app projects. Please feel free to fork 
and reference in your own projects.

## Background

This documentation is meant to take you through my thought process in creating a project template
 as well as giving you a starting point for your projects.

We know that we are using Java and Maven to compile and package our projects. We want to make sure at a minimum, Maven can handle the following:

- Resolving dependencies
- Compiling the project so we can run locally
- Running unit and integration tests
- Packaging the project into a JAR so we can run remotely

There are many powerful features and plugins for Maven which allow you to do much more. Out of scope for the semester project (but arguably very important in an enterprise setting) include:

- Running code linters (i.e. checkstyle) (maven-checkstyle-plugin)
- Making sure transitive dependencies are resolved deterministically (maven-enforcer-plugin)
- Inheriting build settings from a parent POM
- Generating code coverage reports (maven-jacoco-plugin)
- Defining where to find dependencies (many companies use internal “artifactories”
- Defining how to evolve versions of your software (particularly important for libraries which 
other code and services depend on) (maven-buildnumber-plugin)
- Defining and enforcing other company standards and best practices

## Prerequisites

- Install JDK 8
- Install Maven
- Download IntelliJ

## Steps to creating template

When creating a new Maven project/module in IntelliJ, following the prompt will give you a bare 
minimum 
pom.xml file which looks like the following:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>edu.cooper.ece366</groupId>
  <artifactId>bookstore</artifactId>
  <version>1.0-SNAPSHOT</version>
</project>

```

You can add the following to `<project>` in order to tell Maven to compile using Java 1.8 (more 
detail found [here](http://tutorials.jenkov.com/maven/java-compiler.html)):

```xml
<project>
  ...
  <properties>
    <maven.compiler.target>1.8</maven.compiler.target>
    <maven.compiler.source>1.8</maven.compiler.source>
  </properties>
</project>
```

You can then create a `Main.java` source file under path `
./src/main/java/edu/cooper/ece366/bookstore/Main.java` to verify that the project compiles 
properly when you run `mvn compile`:

```java
package edu.cooper.ece366.bokstore;

public class Main {
  public static void main(String[] args) {
    System.out.println("hello world");
  }
}
``` 

You can then run the following:

```bash
$ cd target/classes
$ java edu.cooper.ece366.bookstore.Main
hello world
```

If you want to package the app, you can run `mvn package`. You'll notice a JAR file appears in 
the `target` directory. However, you cannot run it. You'll see the following:

```bash
$ java -jar target/bookstore-1.0-SNAPSHOT.jar 
no main manifest attribute, in target/bookstore-1.0-SNAPSHOT.jar
```

We need to specify a "main manifest attribute" so that the JVM knows which class holds the `main
()` to run: (see [here](https://www.baeldung.com/executable-jar-with-maven) for more details):

You can do this implicitly using 

```
jar cfe Main.jar edu.cooper.ece366.bookstore.Main edu/cooper/ece366/bookstore/Main.class
```

Since a JAR file is a glorified zip file, you can unzip and view its contents:

```
$ unzip Main.jar -d jarrr
Archive:  Main.jar
   creating: jarrr/META-INF/
  inflating: jarrr/META-INF/MANIFEST.MF
  inflating: jarrr/edu/cooper/ece366/bookstore/Main.class
$ ls
META-INF     Main.jar     edu          jarrr        manifest.txt
$ tree jarrr
jarrr
├── META-INF
│   └── MANIFEST.MF
└── edu
    └── cooper
        └── ece366
            └── bookstore
                └── Main.class
```

Of note is the `MANIFEST.MF` file with the following contents:

```
Manifest-Version: 1.0
Created-By: 1.8.0_144 (Oracle Corporation)
Main-Class: edu.cooper.ece366.bookstore.Main
``` 

You can then run the JAR:

```bash
$ java -jar Main.jar
hello world
```

Typically, we would use `maven-jar-plugin` to manage this for us. We would declare in our `pom
.xml` that a particular Java class has our backend service entrypoint `main()` method.

In addition, since we want to produce a JAR artifact that can be copied and run with minimal 
configuration on any JVM, we use `maven-shade-plugin` to package all of the dependencies in a 
"fat jar" or "uber jar." See `pom.xml` in this repo for usage of both `jar` and `shade` plugins. 

N.B. It's not as useful to use the shade plugin for development because (use for deployment in 
most cases) because maven provides powerful support for resolving dependencies. Projects which 
are deployed without this plugin must define their classpath, which requires more setup. In class, 
when we run a main method on a class in IntelliJ, you'll notice if you hover over the command 
that the classpath is being defined for you.


