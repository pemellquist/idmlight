openidm 
===============
OpenIDM is a simple identity management system which can be used with a variety of systems which require IDM. OpenIDM is meant to be simple and effective providing a simple identity model which models domains, users, roles and grants. Together, these can be used to construct your own Role Based Access Control (RBAC). OpenIDM runs as a service with a set of RESTful interfaces allowing full CRUD operations on domain, user and role resources.  

Openidm is implemented in Java and uses an SQL database to persist the IDM model. The reference implementation uses SQLite but any compatible Java JBDC SQL database may also be used. Openidm can be run in any Java runtime environment but the reference implementation allows building a debian package which can be depolyed as a Linux service.

Design Overview
-----------------
Openidm is a stand-alone REST service which listens for REST requests on its own https port. Https is used for privacy when making REST requests allowing command line or GUI access. The keystore file provided is self signed but can be replaced with any properly signed keystore of your choosing. A configuration file, **openidm.config**, allows defining a number of run time settings of openidm including security settings and the specific JDBC driver to be used.<br>

There are three main REST resources projected through the openidm REST API. They are ..<br>
**1. Domain** A domain is a top level container for collecting users. E.g. this might be an organization's name like 'sales', 'R&D', or the name of a company like 'WidgetCo'.<br>
  
**2. User** A user defines an entity which has a name and set of properties such as contact, email address and name. A user may belong to more than one entity. For example, user Joe may belong to both domain 'sales' and 'R&D' while user Tom may only belong to 'R&D'. 

**3.Role** A role is a defined role name which openidm allows to be defined as any value but once defined can be associated with a domain and user. This association is termed as a 'grant'. For example, if we define a role of 'admin' we can then define a grant of domain 'R&D',user 'Joe' and role 'admin'.<br>   

The **api.md** file has details on the actual APIs and examples.<br>


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
```
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESSFUL
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 6 seconds
[INFO] Finished at: Tue Jun 17 13:50:53 PDT 2014
[INFO] Final Memory: 45M/749M
[INFO] ------------------------------------------------------------------------
make debian package
openidm server Debian Package Created in : openidm-1.00/openidm-1.00.deb
```

7) You may manually install the resultant debian package on your system using the linux **dpkg** tool and then perform a service start, stop or retsart **sudo service openidm start**. For convenience and staging script has been provided which will install and start everything up.<br/>
**$ ./stage.sh<br/>**


openidm runtime directories and files
--------------------------------
Once the debian package has been installed the following directories and files are created.<br/>

/opt/openidm<br/>
Installed binaries, certificate files, logging properties, config, run script and sql schema

/var/log/openidm<br/>
Destination for logging files. Logs can be helful when looking at problems.

/etc/init.d/openidm<br/>
Service start, stop and restart init.d script

Testing openidm
------------------------------
A set of system level tests are available to test an installed openidm server. These tests are meant to drive all the openidm REST APIs and check results. To run these tests, a clean database is required. <br/>

After having installed tokenidm, the database tables are required to be dropped. <br/>
$<installed source dir>/tests/cleardb.sh

Tests can be run and results reported. <br/>
$<installed source dir>/tests/test.sh

