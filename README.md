tokenidm server environment and build information 
--------------------------------------------------
This README defines the required tools and steps to build and run tokenidm. There are other combinations of OSs, libraries and tools possible in addition to what is specified here.

1) Get Ubuntu OS, 12.04 64 bit bit OS is recommended
(other OSs are possible but these instructions assume this Ubuntu & Debian packages) 

2) Install tokenidm api sources, clone from git repo
*** FIX ***
git clone https://github.com/LBaaS/lbaas-api.git your-lbaas-directory 

3) Install maven, needed to building
sudo apt-get install maven2 ( Note maven 2 is required )

4) Install java 7
sudo add-apt-repository ppa:webupd8team/java -y
sudo apt-get update
sudo apt-get install oracle-java7-installer

5) Install Debian Package Make tool 
sudo apt-get install dh-make

6) Build  
./build.sh
This will build Java jar and from it a debian package

7) Install deb package
sudo dpkg -i tokenidm-1.00/tokenidm-1.00.deb ( or whatever version was built )
The following directories and files will have been created.

/opt/tokenidm Installed binaries, certificate files, logging properties, config, run script and sql schema

/var/log/tokenidm destination for logging files

/etc/init.d/tokenidm service start, stop and restart init.d script
