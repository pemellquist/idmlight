idmlight 
===============
IDMLight is a simple identity management server which can be used with a variety of systems which require Identity management and Role Based Access Control (RBAC). IDMLight is meant to be simple and effective providing a simple identity model which models domains, users, roles and grants. Together, these can be used to enforce your own (RBAC). Idmlight has a set of RESTful interfaces allowing full CRUD operations on IDM resources.  

Role based access controls (RBAC) simplify routine account management operations and facilitate security audits. System administrators do not assign permissions directly to individual user accounts. Instead, individuals acquire access through their roles within an organization, which eliminates the need to edit a potentially large (and frequently changing) number of resource permissions and user rights assignments when creating, modifying, or deleting user accounts. Unlike traditional access control lists, permissions in RBAC describe meaningful operations within a particular application or system instead of the underlying low-level data object access methods. Storing roles and permissions in a centralized database or directory service simplifies the process of ascertaining and controlling role memberships and role permissions.


Design Overview
-----------------
IDMLight is implemented in Java and uses an SQL database to persist the IDM model. The reference implementation uses SQLite but any compatible Java JBDC SQL database may also be used. Openidm can be run in any Java runtime environment but the reference implementation allows building a debian package which can be depolyed as a Linux service.
idmlight is a stand-alone REST service which listens for REST requests on its own https port. Https is used for privacy when making REST requests allowing command line or GUI access. The keystore file provided is self signed but can be replaced with any properly signed keystore of your choosing. A configuration file, **idmlight.config**, allows defining a number of run time settings of idmlight including security settings and the specific JDBC driver to be used.<br>

There are three main REST resources projected through the idmlight REST API. They are ..<br>
**1. Domain** A domain is a top level container for collecting users. E.g. this might be an organization's name like 'sales', 'R&D', or the name of a company like 'WidgetCo'.<br>
  
**2. User** A user defines an entity which has a name and set of properties such as contact, email address and name. A user may belong to more than one entity. For example, user Joe may belong to both domain 'sales' and 'R&D' while user Tom may only belong to 'R&D'. 

**3.Role** A role is a defined role name which idmlight allows to be defined as any value but once defined can be associated with a domain and user. This association is termed as a 'grant'. For example, if we define a role of 'admin' we can then define a grant of domain 'R&D',user 'Joe' and role 'admin'.<br>   

The **api.md** file has details on the actual APIs and examples.<br>


Getting and building idmlight
------------------
1) Get your Operationg system(Ubuntu OS, 12.04 64 bit bit OS is recommended)<br/>
Other OSs are possible but these instructions assume Debian packages for installing all dependencies. 

2) Install idmlight sources, clone these from github repo. This assumes that you already have the git client installed.<br/>
**$ git clone https://github.com/pemellquist/idmlight.git <your local dorectory>** 

3) Install maven, needed to building. Maven2 has been verified to work, new versions may also work.<br/>
**$ sudo apt-get install maven2** 

4) Install java 7. There are a variety of places where you can pick up the JDK. One is listed below.<br/>
**$ sudo add-apt-repository ppa:webupd8team/java -y <br/>**
**$ sudo apt-get update <br/>**
**$ sudo apt-get install oracle-java7-installer </br>**

5) Install Debian Package Make tool. This will you to build a debian package which can be installed on your Debian based system.<br/>
**$ sudo apt-get install dh-make**

6) Building idmlight<br/> 
idmlight uses maven for building and resolving dependencies hence everything is defined in the standard maven pom.xml file. You may choose to run **mvn clean install** and **mvn assembly:assembly** and then a provided script to build the debian package or you can simply run the provided build shell script which will build and package everything.<br>
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
idmlight server Debian Package Created in : idmlight-1.00/idmlightm-1.00.deb
```

7) You may manually install the resultant debian package on your system using the linux **dpkg** tool and then perform a service start, stop or restart **sudo service idmlight start**. For convenience and staging script has been provided which will install and start everything up.<br/>
**$ ./stage.sh<br/>**


idmlight runtime directories and files
--------------------------------
Once the debian package has been installed the following directories and files are created.<br/>

/opt/idmlight<br/>
Installed binaries, certificate files, logging properties, config, run script and sql schema

/var/log/idmlight<br/>
Destination for logging files. Logs can be helful when looking at problems.

/etc/init.d/idmlight<br/>
Service start, stop and restart init.d script

Testing idmlight
------------------------------
A set of system level tests are available to test an installed idmlight server. These tests are meant to drive all the idmlight REST APIs and check results. To run these tests, a clean database is required. <br/>

After having installed idmlight, the database tables are required to be dropped. <br/>
$<installed source dir>/tests/cleardb.sh

Tests can be run and results reported. <br/>
$<installed source dir>/tests/test.sh

