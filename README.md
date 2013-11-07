## About javaonep

This project is a Java binding to the Exosite One Platform API. The API is
exposed over HTTP in a JSON RPC style interface.

Recommend using with JDK/JRE 1.6. This project was tested with Eclipse 3.5.2/JDK 1.6.0 (Linux).

License is BSD, Copyright 2011, Exosite LLC (see LICENSE file)

### Quick Start

The 'ant' package is required to build the source. The 'JSON.simple' toolkit is used to encode/decode json text, please refer to http://code.google.com/p/json-simple/.

To use this project in the Eclipse IDE, import the directory 'javaonep' as project.

To create a jar file in the bin directory:

```
$ ant jar
```

To run an example, first enter the CIK of a device (or other client) in examples/ClientOnepExamples.java in place of the text PUTA40CHARACTERCIKHERE. Then create a dataport in that CIK, e.g. using [Exoline](https://github.com/exosite/exoline), and run the example with ant.

```
$ exo create <your CIK> --type=dataport --format=integer --alias=X1 --name=X1
$ ant run
```

If the example worked, you should see output similar to this:

```
$ ant run
Buildfile: /Users/danw/prj/exosite/javaonep/build.xml

init:

clean:

build:
    [javac] Compiling 13 source files to /Users/danw/prj/exosite/javaonep/bin

build-examples:
    [javac] Compiling 1 source file to /Users/danw/prj/exosite/javaonep/bin

run:
     [java] Data: 41 is written.
     [java] Data: 41 is read.
     [java] (Alias,RID): (X1 , 7e5c6fea7393f40e5b8ce1b3e4729ffd327bd060)

BUILD SUCCESSFUL
Total time: 3 seconds
```

### Tests

To run junit-test cases, replace the strings that begin with PUTA40CHARACTER
with the CIK of a device (or other client) and the RID of a dataport owned by
that client. To get the RID for a dataport if you know the alias, you can 
use Exoline. For example, assuming the dataport created to run the example 
still exists, you could get the RID this way:

```
$ exo lookup <your CIK> X1
<RID for dataport with alias X1>
$ ant test
```

If the tests run successfully you should see something like this:

```
$ ant test
Buildfile: /Users/danw/prj/exosite/javaonep/build.xml

init:

clean:

build:
    [javac] Compiling 13 source files to /Users/danw/prj/exosite/javaonep/bin

build-test:
    [javac] Compiling 1 source file to /Users/danw/prj/exosite/javaonep/bin

test:
    [junit] Testsuite: OnepUnitTest
    [junit] Tests run: 12, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.826 sec
    [junit] 

BUILD SUCCESSFUL
Total time: 4 seconds
```

To use in your application, add the following jar files to your library directory:

- `./bin/onepv1.jar`
- `./libs/json_simple-1.1.jar`

For more information on the API and examples, reference the API
documentation at http://github.com/exosite/api

### TODO

- pass CIK/RID on ant command line so test end example files don't have to be modified

### Release Info

#### 0.2.0 (2013-11-06)

- added version
- removed deprecated comment and write group APIs

#### Unversioned release (2011-9-26)

- initial version
