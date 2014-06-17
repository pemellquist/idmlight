openidm 
===============
OpenIDM is a simple identity management system which can be used with a variety of systems which require IDM. OpenIDM is meant to be simple and effective providing a simple identity model which models domains, users, roles and grants. Together, these can be used to construct your own Role Based Access Control (RBAC). OpenIDM runs as a service with a set of RESTful interfaces allowing full CRUD operations on domain, user and role resources.  

Openidm is implemented in Java and uses an SQL database to persist the IDM model. The reference implementation uses SQLite but any compatible Java JBDC SQL database may also be used. Openidm can be run in any Java runtime environment but the reference implementation allows building a debian package which can be depolyed as a Linux service.


Getting and building openidm
------------------
1) Get your Operationg system(Ubuntu OS, 12.04 64 bit bit OS is recommended)<br/>
Other OSs are possible but these instructions assume Debian packages for installing all dependencies. 

2) Install openidm sources, clone these from github repo. This assumes that you already have the git client installed.<br/>
**$ git clone https://github.com/pemellquist/openidm.git <your local dorectory>** 

3) Install maven, needed to building. Maven2 has been verified to work, new versions may also work.<br/>
**$ sudo apt-get install maven2** 

4) Install java 7. There are a variety of places where you can pick up the JDK. One is listed below.<br/>
**$ sudo add-apt-repository ppa:webupd8team/java -y <br/>**
**$ sudo apt-get update <br/>**
**$ sudo apt-get install oracle-java7-installer </br>**

5) Install Debian Package Make tool. This will you to build a debian package which can be installed on your Debian based system.<br/>
**$ sudo apt-get install dh-make**

6) Building openidm<br/> 
Openidm uses maven for building and resolving dependencies hence everything is defined in the standard maven pom.xml file. You may choose to run **mvn clean install** and **mvn assembly:assembly** and then a provided script to build the debian package or you can simply run the provided build shell script which will build and package everything.<br>
**$ <your local directory>./build.sh<br/>**
If the build was a success you should see something like ...<br>
'''
     [INFO] ------------------------------------------------------------------------
     [INFO] BUILD SUCCESSFUL
     [INFO] ------------------------------------------------------------------------
     [INFO] Total time: 6 seconds
     [INFO] Finished at: Tue Jun 17 13:50:53 PDT 2014
     [INFO] Final Memory: 45M/749M
     [INFO] ------------------------------------------------------------------------
     make debian package
     openidm server Debian Package Created in : openidm-1.00/openidm-1.00.deb
'''


7) Install deb package and run it <br/>
$ ./stage.sh<br/>
The stage script will install the created debian package and start the tokenidm server. You may also start and stop the tokenidm server as a service.<br/>
$ service tokenidm start | stop



tokenidm runtime directories and files
--------------------------------
Once the debian package has been installed the following directories and files are created.<br/>

/opt/tokenidm<br/>
Installed binaries, certificate files, logging properties, config, run script and sql schema

/var/log/tokenidm<br/>
Destination for logging files

/etc/init.d/tokenidm,br/>
Service start, stop and restart init.d script

Testing tokenidm
------------------------------
A set of system level tests are available to test an installed tokenidm serve. These tests are meant to drive all the tokenidm REST APIs and check results. To run these tests, a clean database is required. <br/>

After having installed tokenidm, the database tables are required to be dropped. <br/>
$<installed source dir>/tests/cleardb.sh

Tests can be run and results reported. <br/>
$<installed source dir>/tests/test.sh

