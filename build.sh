echo "clean and installing"
mvn clean install
if [ $? -ne 0 ]; 
then
   echo "clean and install failed"
   exit;
fi
echo "building jar"
mvn assembly:assembly
if [ $? -ne 0 ];
then
   echo "build jar failed"
   exit;
fi
echo "make debian package"
./makedeb.sh
