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
The stage script will install the created debian package and start the tokenidm server.
The following directories and files will have been created.

/opt/tokenidm Installed binaries, certificate files, logging properties, config, run script and sql schema

/var/log/tokenidm destination for logging files

/etc/init.d/tokenidm service start, stop and restart init.d script

8) Starting and stopping tokenidm
service tokenidm start | stop
