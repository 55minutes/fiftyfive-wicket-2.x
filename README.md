# 55 Minutes Wicket Project

**Please note that this is a legacy version of the current [fiftyfive-wicket](https://github.com/55minutes/fiftyfive-wicket) project. Use this project if you need support for Wicket 1.4. New applications should use [fiftyfive-wicket](https://github.com/55minutes/fiftyfive-wicket), which builds atop Wicket 1.5.**

The 55 Minutes Wicket project is a set of tools and libraries we use for enhancing our productivity with the [Wicket Java web framework](http://wicket.apache.org/). We've made our code available as open source to share with the Wicket community.

## Feature Highlights

* Easily add HTML validation and xpath assertions to your Wicket unit tests ([WicketTestUtils Javadoc](http://opensource.55minutes.com/apidocs/fiftyfive-wicket/2.0-SNAPSHOT/index.html?fiftyfive/wicket/test/WicketTestUtils.html))
* Bootstrap your Wicket application with hot-deploy, Spring integration and other best practices ([FoundationApplication Javadoc](http://opensource.55minutes.com/apidocs/fiftyfive-wicket/2.0-SNAPSHOT/index.html?fiftyfive/wicket/FoundationApplication.html))
* Use shortcuts for declaring Wicket's most common components and behaviors ([Shortcuts Javadoc](http://opensource.55minutes.com/apidocs/fiftyfive-wicket/2.0-SNAPSHOT/index.html?fiftyfive/wicket/util/Shortcuts.html))
* Cleanly separate your Java and JavaScript code, use Sprocket-like syntax to manage dependencies, merge JS files for best performance ([fiftyfive.wicket.js package Javadoc](http://opensource.55minutes.com/apidocs/fiftyfive-wicket/2.0-SNAPSHOT/index.html?fiftyfive/wicket/js/package-summary.html))
* Generate all the scaffolding you need for a real world Wicket project, including [Compass stylesheet authoring](http://compass-style.org/), HTML5 starters, custom error pages, logging, unit tests, jQuery integration and more (see Getting Started section below)

## Getting Started

The quickest way to get started with the 55 Minutes Wicket library is use our Maven archetype. This assumes that you have maven installed, preferably version 2.2.

First, run the following command. This creates a project directory with all the Java, Maven POM and web.xml scaffolding you need for a Wicket application with Spring integration, plus high-quality starters for your HTML5, CSS and JavaScript. As with most maven archetypes, you'll be prompted for the project name, group ID, artifact ID, version and package.

    mvn archetype:generate -U \
        -DarchetypeGroupId=com.55minutes \
        -DarchetypeArtifactId=fiftyfive-wicket-archetype \
        -DarchetypeRepository=http://opensource.55minutes.com/maven-snapshots \
        -DarchetypeVersion=2.0-SNAPSHOT

Next, to run the resulting project, simply change into the project directory and run:

    mvn jetty:run

That's it! Your project is up and running at http://localhost:8080/. Explore the code included with the archetype and then dive into the [fiftyfive-wicket Javadoc](http://opensource.55minutes.com/apidocs/fiftyfive-wicket/2.0-SNAPSHOT/) to see what is available in the library.

## Maven Dependency

To add the core 55 Minutes Wicket library to an existing project, use the following maven dependency:

```xml
<dependency>
  <groupId>com.55minutes</groupId>
  <artifactId>fiftyfive-wicket</artifactId>
  <version>2.0-SNAPSHOT</version>
</dependency>
```

For the unit testing support:

```xml
<dependency>
  <groupId>com.55minutes</groupId>
  <artifactId>fiftyfive-wicket-test</artifactId>
  <version>2.0-SNAPSHOT</version>
  <scope>test</scope>
</dependency>
```

For the JavaScript portion of the library, include:

```xml
<dependency>
  <groupId>com.55minutes</groupId>
  <artifactId>fiftyfive-wicket-js</artifactId>
  <version>2.0-SNAPSHOT</version>
</dependency>
```

Finally, since our artifacts aren't in the root maven repository, you'll also need to include the following snippet:

```xml
<repository>
  <id>fiftyfive-opensource-snapshots</id>
  <name>55 Minutes Open Source Maven Snapshots Repository</name>
  <url>http://opensource.55minutes.com/maven-snapshots</url>
  <releases><enabled>false</enabled></releases>
  <snapshots><enabled>true</enabled></snapshots>
</repository>
<repository>
  <id>fiftyfive-opensource-releases</id>
  <name>55 Minutes Open Source Maven Releases Repository</name>
  <url>http://opensource.55minutes.com/maven-releases</url>
  <releases><enabled>true</enabled></releases>
  <snapshots><enabled>false</enabled></snapshots>
</repository>
```
