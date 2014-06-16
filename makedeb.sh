# build debian package for binaries and related files
PACKAGE_NAME=openidm
PACKAGE_VERSION=1.00
PACKAGE_OWNER=pemellquist@gmail.com
PACKAGE_CONTROL=debian/DEBIAN/control
PACKAGE_DESCRIPT="openidm Open IDentity Management(IDM) Server"
PACKAGE_LOCATION="opt/openidm"
PACKAGE_JAR="/target/openidm-0.1.00-jar-with-dependencies.jar"

# create debian package directory and initialize
rm -r -f $PACKAGE_NAME-$PACKAGE_VERSION
mkdir -p $PACKAGE_NAME-$PACKAGE_VERSION
cd $PACKAGE_NAME-$PACKAGE_VERSION 
yes | dh_make -n -s -i -e $PACKAGE_OWNER  > dh_make.txt 
rm debian/README* debian/*.ex debian/*.EX debian/copyright debian/docs debian/control
mkdir debian/DEBIAN
mkdir debian/opt
mkdir -p debian/var/log/openidm
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

# copy openidm specific files to target location
cp ../openidm.config debian/$PACKAGE_LOCATION
cp ../log4j.properties debian/$PACKAGE_LOCATION
cp ../openidm.sh debian/$PACKAGE_LOCATION
cp ../keystore.jks debian/$PACKAGE_LOCATION
cp ../$PACKAGE_JAR debian/$PACKAGE_LOCATION
cp ../sql/openidm.db debian/$PACKAGE_LOCATION
cp ../openidm.initd debian/etc/init.d/openidm

# make the package
dpkg --build debian >> dh_make.txt
mv debian.deb $PACKAGE_NAME-$PACKAGE_VERSION.deb
echo "openidm server Debian Package Created in : "$PACKAGE_NAME-$PACKAGE_VERSION/$PACKAGE_NAME-$PACKAGE_VERSION.deb

cd ..


