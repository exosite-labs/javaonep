========================================
About javaonep
========================================
This project is a Java binding  to the Exosite One Platform API. The API is
exposed over HTTP in a JSON RPC style interface.

Recommend using with JDK/JRE 1.6.

License is BSD, Copyright 2011, Exosite LLC (see LICENSE file)

========================================
Quick Start
========================================
Tested with Eclipse 3.5.2/JDK 1.6.0 (Linux).

1) The 'ant' package is required to build the source.<br>
2) The 'JSON.simple' toolkit is used to encode/decode json text, please refer to http://code.google.com/p/json-simple/.<br>
3) For Eclipse IDE, import the directory 'javaonep' as project.<br>
4) For command line:<br>
 &nbsp; &nbsp;4.1) To create jar file in bin directory, type<br>
 &nbsp; &nbsp; &nbsp; &nbsp; ant jar<br>
 &nbsp; &nbsp;4.2) To run an example, type<br>
 &nbsp; &nbsp; &nbsp; &nbsp; ant run<br>
 &nbsp; &nbsp;4.3) To run junit-test cases, type<br>
 &nbsp; &nbsp; &nbsp; &nbsp; ant test<br>
5) To use in your application, add below jar files to your library directory:<br>
 &nbsp; &nbsp; ./bin/onepv1.jar<br>
 &nbsp; &nbsp; ./libs/json_simple-1.1.jar<br>

For more information on the API and examples, reference Exosite online 
documentation at http://exosite.com/developers/documentation.

========================================
Release Info
========================================
----------------------------------------
Release 2011-09-26
----------------------------------------
--) initial version<br>
