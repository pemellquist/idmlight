tokenidm server 
===============
TokenIDM is a simple stand-alone identity manager server and token generator/validator which can be used with a variety of REST services that require token based API authentication. TokenIDM is similar to Openstack's Keystone IDM service but it is meant to be simplier and it is implemented in Java. 

This README defines the required tools and steps to build and run tokenidm. There are other combinations of OSs, libraries and tools possible.

Getting and building tokenidm
------------------
1) Get Ubuntu OS, 12.04 64 bit bit OS is recommended<br/>
(other OSs are possible but these instructions assume this Ubuntu & Debian packages) 

2) Install tokenidm api sources, clone from git repo<br/>
$ git clone https://github.com/pemellquist/tokenidm.git <your local tokenidm location> 

3) Install maven, needed to building<br/>
$ sudo apt-get install maven2 

4) Install java 7 <br/>
$ sudo add-apt-repository ppa:webupd8team/java -y <br/>
$ sudo apt-get update <br/>
$ sudo apt-get install oracle-java7-installer </br>

5) Install Debian Package Make tool <br/>
$ sudo apt-get install dh-make

6) Build <br/> 
$ ./build.sh<br/>
This will build Java jar and from it a debian package. 

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

D
$<source install dir>/tests/./cleardb.sh
