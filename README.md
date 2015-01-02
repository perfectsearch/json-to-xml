# json-to-xml #

JSON-to-XML filter consisting of scanner, parser and generator. Requires
no schema POJO describing the JSON.


### How do I get set up? ###

* Standard Eclipse import project sequence.
* Eclipse Java project.
* Uses only external library log4j v2.x, already included.
* Standard JUnit 4.x testing.
* Deploys as a stand-alone JAR.
* Build runnable JAR: mvn clean compile assembly:single


### Whom do I talk to? ###

* Russell Bateman


### Problems

* Exception in thread "main" java.lang.UnsupportedClassVersionError

  This application was built using Java 8. You'll need to run it using Java 8.
  However, it does not actually make use of Java 8 features, it's just what I
  was running and I did not take the trouble to fix the problem arising from
  executing it using Java 7. You'll see something like:

    Exception in thread "main" java.lang.UnsupportedClassVersionError: com/perfectsearchcorp/filter/JsonToXmlFilter : Unsupported major.minor version 52.0
        at java.lang.ClassLoader.defineClass1(Native Method)
        at java.lang.ClassLoader.defineClass(ClassLoader.java:800)
        ...

  There is a helpful script, *json-to-xml.sh*, you can modify to suit your
  needs.


### Sample command line and execution
    $ ./json-to-xml.sh

      Please export path to Java 8 binary as variable JAVA8 before invoking, e.g:
      export JAVA8=/home/user/dev/jdk1.8.0_25/bin/java

    $ export JAVA8=/home/russ/dev/jdk1.8.0_25/bin/java

    $ ./json-to-xml.sh
    JSON-to-XML filter (json-to-xml)
    0.9
    December 2014
    You are free to use this utility as a binary or its sources in any way you please.
    Options
    --rootname <rootname>
    --xmldecl <custom XML Declaration>
    --dtd <DTD>
    --doctype <Document type>
    --pretty
    --tabwidth
    --version
    --logging
    --passive
    --help

    $ ./json-to-xml.sh --rootname fun --pretty --tabwidth 4 sample.json
    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <fun>
        <name>Russell Bateman</name>
        <listens>Neil Young</listens>
        <address>unknown</address>
    </fun>

### To read from stdin
    $ cat sample.json | java -jar ./target/json-to-xml-1.0.0-SNAPSHOT-jar-with-dependencies.jar
