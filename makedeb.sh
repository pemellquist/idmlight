# build debian package for binaries and related files
PACKAGE_NAME=idmlight
PACKAGE_VERSION=1.00
PACKAGE_OWNER=pemellquist@gmail.com
PACKAGE_CONTROL=debian/DEBIAN/control
PACKAGE_DESCRIPT="IDMLight IDentity Management(IDM) Server"
PACKAGE_LOCATION="opt/idmlight"
PACKAGE_JAR="/target/idmlight-0.1.00-jar-with-dependencies.jar"

# create debian package directory and initialize
rm -r -f $PACKAGE_NAME-$PACKAGE_VERSION
mkdir -p $PACKAGE_NAME-$PACKAGE_VERSION
cd $PACKAGE_NAME-$PACKAGE_VERSION 
yes | dh_make -n -s -i -e $PACKAGE_OWNER  > dh_make.txt 
rm debian/README* debian/*.ex debian/*.EX debian/copyright debian/docs debian/control
mkdir debian/DEBIAN
mkdir debian/opt
mkdir -p debian/var/log/idmlight
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

# copy idmlight specific files to target location
cp ../idmlight.config debian/$PACKAGE_LOCATION
cp ../log4j.properties debian/$PACKAGE_LOCATION
cp ../idmlight.sh debian/$PACKAGE_LOCATION
cp ../keystore.jks debian/$PACKAGE_LOCATION
cp ../$PACKAGE_JAR debian/$PACKAGE_LOCATION
cp ../sql/idmlight.db debian/$PACKAGE_LOCATION
cp ../idmlight.initd debian/etc/init.d/idmlight

# make the package
dpkg --build debian >> dh_make.txt
mv debian.deb $PACKAGE_NAME-$PACKAGE_VERSION.deb
echo "idmlight server Debian Package Created in : "$PACKAGE_NAME-$PACKAGE_VERSION/$PACKAGE_NAME-$PACKAGE_VERSION.deb

cd ..


