# build debian package for binaries and related files
PACKAGE_NAME=tokenidm
PACKAGE_VERSION=1.00
PACKAGE_OWNER=pemellquist@gmail.com
PACKAGE_CONTROL=debian/DEBIAN/control
PACKAGE_DESCRIPT="Token based IDentity Management(IDM) System binaries"
PACKAGE_LOCATION="opt/tokenidm"
PACKAGE_JAR="/target/tokenidm-0.1.00-jar-with-dependencies.jar"

# create debian package directory and initialize
rm -r -f $PACKAGE_NAME-$PACKAGE_VERSION
mkdir -p $PACKAGE_NAME-$PACKAGE_VERSION
cd $PACKAGE_NAME-$PACKAGE_VERSION 
yes | dh_make -n -s -i -e $PACKAGE_OWNER  > dh_make.txt 
rm debian/README* debian/*.ex debian/*.EX debian/copyright debian/docs debian/control
mkdir debian/DEBIAN
mkdir debian/opt
mkdir -p debian/var/log/tokenidm
mkdir -p debian/etc/init.d
mkdir debian/$PACKAGE_LOCATION

# create control file
echo "Package: "$PACKAGE_NAME >> $PACKAGE_CONTROL 
echo "Version: "$PACKAGE_VERSION >> $PACKAGE_CONTROL 
echo "Section: web" >> $PACKAGE_CONTROL
echo "Priority: optional" >> $PACKAGE_CONTROL
echo "Architecture: all" >> $PACKAGE_CONTROL
echo "Maintainer: "$PACKAGE_OWNER >> $PACKAGE_CONTROL 
echo "Description: "$PACKAGE_DESCRIPT >> $PACKAGE_CONTROL

# copy tokenIDM specific files to target location
cp ../tokenidm.config debian/$PACKAGE_LOCATION
cp ../log4j.properties debian/$PACKAGE_LOCATION
cp ../tokenidm.sh debian/$PACKAGE_LOCATION
cp ../keystore.jks debian/$PACKAGE_LOCATION
cp ../$PACKAGE_JAR debian/$PACKAGE_LOCATION
cp ../sql/tokenidm.db debian/$PACKAGE_LOCATION
cp ../tokenidm.initd debian/etc/init.d/tokenidm

# make the package
dpkg --build debian >> dh_make.txt
mv debian.deb $PACKAGE_NAME-$PACKAGE_VERSION.deb
echo "Token IDM server Debian Package Created in : "$PACKAGE_NAME-$PACKAGE_VERSION/$PACKAGE_NAME-$PACKAGE_VERSION.deb

cd ..

